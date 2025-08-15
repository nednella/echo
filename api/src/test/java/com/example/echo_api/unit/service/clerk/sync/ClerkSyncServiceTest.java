package com.example.echo_api.unit.service.clerk.sync;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEvent;
import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEventData;
import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEventType;
import com.example.echo_api.persistence.dto.request.clerk.webhook.data.UserDelete;
import com.example.echo_api.persistence.dto.request.clerk.webhook.data.UserUpsert;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.service.clerk.sdk.ClerkSdkService;
import com.example.echo_api.service.clerk.sync.ClerkSyncServiceImpl;
import com.example.echo_api.service.session.SessionService;
import com.example.echo_api.service.user.UserService;

@ExtendWith(MockitoExtension.class)
class ClerkSyncServiceTest {

    @Mock
    private ClerkSdkService clerkSdkService;

    @Mock
    private SessionService sessionService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ClerkSyncServiceImpl clerkSyncService;

    private com.clerk.backend_api.models.components.User createMockClerkUser(
        String id,
        String username,
        String imageUrl) {
        return com.clerk.backend_api.models.components.User.builder()
            .id(id)
            .username(username)
            .imageUrl(imageUrl)
            .build();
    }

    @Test
    void onboardAuthenticatedUser_ReturnsNull_WhenAuthenticatedTokenHasOnboardingClaims() {
        // arrange
        User expected = null;
        when(sessionService.isAuthenticatedUserOnboarded()).thenReturn(true);

        // act
        User actual = clerkSyncService.onboardAuthenticatedUser();

        // assert
        assertEquals(expected, actual);
        verify(sessionService, never()).getAuthenticatedUserClerkId();
        verify(clerkSdkService, never()).getUser(anyString());
        verify(userService, never()).upsertFromExternalSource(anyString(), anyString(), anyString());
        verify(clerkSdkService, never()).completeOnboarding(anyString(), anyString());
    }

    @Test
    void onboardAuthenticatedUser_ReturnsUser_WhenOnboardingProcessIsCompleted() {
        // arrange
        UUID id = UUID.randomUUID();
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        var clerkUser = createMockClerkUser(externalId, username, imageUrl);
        User expected = User.forTest(id, externalId);

        when(sessionService.isAuthenticatedUserOnboarded()).thenReturn(false);
        when(sessionService.getAuthenticatedUserClerkId()).thenReturn(externalId);
        when(clerkSdkService.getUser(externalId)).thenReturn(clerkUser);
        when(userService.upsertFromExternalSource(externalId, username, imageUrl)).thenReturn(expected);

        // act
        User actual = clerkSyncService.onboardAuthenticatedUser();

        // assert
        assertEquals(expected, actual);
        verify(sessionService).isAuthenticatedUserOnboarded();
        verify(sessionService).getAuthenticatedUserClerkId();
        verify(clerkSdkService).getUser(externalId);
        verify(userService).upsertFromExternalSource(externalId, username, imageUrl);
        verify(clerkSdkService).completeOnboarding(externalId, id.toString());
    }

    @Test
    void handleWebhookEvent_HandlesUserCreated() {
        // arrange
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";

        ClerkWebhookEventType type = ClerkWebhookEventType.USER_CREATED;
        ClerkWebhookEventData data = new UserUpsert(externalId, username, imageUrl);
        ClerkWebhookEvent event = new ClerkWebhookEvent(data, type);

        // act
        clerkSyncService.handleWebhookEvent(event);

        // assert
        verify(userService).upsertFromExternalSource(externalId, username, imageUrl);
    }

    @Test
    void handleWebhookEvent_HandlesUserUpdated() {
        // arrange
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";

        ClerkWebhookEventType type = ClerkWebhookEventType.USER_UPDATED;
        ClerkWebhookEventData data = new UserUpsert(externalId, username, imageUrl);
        ClerkWebhookEvent event = new ClerkWebhookEvent(data, type);

        // act
        clerkSyncService.handleWebhookEvent(event);

        // assert
        verify(userService).upsertFromExternalSource(externalId, username, imageUrl);
    }

    @Test
    void handleWebhookEvent_HandlesUserDeleted() {
        // arrange
        String externalId = "user_someRandomStringThatIsUniqueApparently";

        ClerkWebhookEventType type = ClerkWebhookEventType.USER_DELETED;
        ClerkWebhookEventData data = new UserDelete(externalId, true);
        ClerkWebhookEvent event = new ClerkWebhookEvent(data, type);

        // act
        clerkSyncService.handleWebhookEvent(event);

        // assert
        verify(userService).deleteFromExternalSource(externalId);
    }

}
