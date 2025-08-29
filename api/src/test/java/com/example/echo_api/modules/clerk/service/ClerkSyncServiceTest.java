package com.example.echo_api.modules.clerk.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.modules.clerk.dto.sdk.ClerkUserDTO;
import com.example.echo_api.modules.clerk.dto.webhook.ClerkWebhook;
import com.example.echo_api.modules.clerk.dto.webhook.ClerkWebhookData;
import com.example.echo_api.modules.clerk.dto.webhook.ClerkWebhookType;
import com.example.echo_api.modules.clerk.dto.webhook.UserDelete;
import com.example.echo_api.modules.clerk.dto.webhook.UserUpsert;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.service.user.UserService;
import com.example.echo_api.shared.service.SessionService;

/**
 * Unit test class for {@link ClerkSyncService}.
 */
@ExtendWith(MockitoExtension.class)
class ClerkSyncServiceTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private ClerkSdkService clerkSdkService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ClerkSyncServiceImpl clerkSyncService;

    @Test
    void onboardAuthenticatedUser_ReturnsUser_WhenOnboardingProcessIsCompleted() {
        // arrange
        UUID id = UUID.randomUUID();
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        Map<String, Object> metadata = new HashMap<>();
        ClerkUserDTO clerkUser = new ClerkUserDTO(externalId, username, null, imageUrl, metadata);
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
        verify(clerkSdkService).completeOnboarding(clerkUser, id.toString());
    }

    @Test
    void onboardAuthenticatedUser_ReturnsNull_WhenAlreadyExistsByClerkId() {
        // arrange
        User expected = null;
        when(sessionService.isAuthenticatedUserOnboarded()).thenReturn(true);

        // act
        User actual = clerkSyncService.onboardAuthenticatedUser();

        // assert
        assertEquals(expected, actual);
        verify(sessionService).isAuthenticatedUserOnboarded();
        verify(sessionService, never()).getAuthenticatedUserClerkId();
        verify(clerkSdkService, never()).getUser(anyString());
        verify(userService, never()).upsertFromExternalSource(anyString(), anyString(), anyString());
        verify(clerkSdkService, never()).completeOnboarding(any(ClerkUserDTO.class), anyString());
    }

    @Test
    void handleWebhookEvent_HandlesUserCreated() {
        // arrange
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";

        ClerkWebhookType type = ClerkWebhookType.USER_CREATED;
        ClerkWebhookData data = new UserUpsert(externalId, username, imageUrl);
        ClerkWebhook event = new ClerkWebhook(data, type);

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

        ClerkWebhookType type = ClerkWebhookType.USER_UPDATED;
        ClerkWebhookData data = new UserUpsert(externalId, username, imageUrl);
        ClerkWebhook event = new ClerkWebhook(data, type);

        // act
        clerkSyncService.handleWebhookEvent(event);

        // assert
        verify(userService).upsertFromExternalSource(externalId, username, imageUrl);
    }

    @Test
    void handleWebhookEvent_HandlesUserDeleted() {
        // arrange
        String externalId = "user_someRandomStringThatIsUniqueApparently";

        ClerkWebhookType type = ClerkWebhookType.USER_DELETED;
        ClerkWebhookData data = new UserDelete(externalId, true);
        ClerkWebhook event = new ClerkWebhook(data, type);

        // act
        clerkSyncService.handleWebhookEvent(event);

        // assert
        verify(userService).deleteFromExternalSource(externalId);
    }

}
