package com.example.echo_api.modules.clerk.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.modules.clerk.dto.ClerkUser;
import com.example.echo_api.modules.user.entity.User;
import com.example.echo_api.shared.service.SessionService;

/**
 * Unit test class for {@link ClerkOnboardingService}.
 */
@ExtendWith(MockitoExtension.class)
class ClerkOnboardingServiceTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private ClerkSdkService clerkSdkService;

    @Mock
    private ClerkSyncService clerkSyncService;

    @InjectMocks
    private ClerkOnboardingServiceImpl clerkOnboardingService;

    @Test
    void onboardAuthenticatedUser_ReturnsUser_WhenOnboardingProcessIsCompleted() {
        // arrange
        UUID id = UUID.randomUUID();
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        Map<String, Object> metadata = new HashMap<>();
        ClerkUser clerkUser = new ClerkUser(externalId, username, null, imageUrl, metadata);
        User expected = User.forTest(id, externalId);

        when(sessionService.isAuthenticatedUserOnboarded()).thenReturn(false);
        when(sessionService.getAuthenticatedUserClerkId()).thenReturn(externalId);
        when(clerkSdkService.getUser(externalId)).thenReturn(clerkUser);
        when(clerkSyncService.ingestUserUpserted(clerkUser)).thenReturn(expected);

        // act
        User actual = clerkOnboardingService.onboardAuthenticatedUser();

        // assert
        assertEquals(expected, actual);
        verify(sessionService).isAuthenticatedUserOnboarded();
        verify(sessionService).getAuthenticatedUserClerkId();
        verify(clerkSdkService).getUser(externalId);
        verify(clerkSyncService).ingestUserUpserted(clerkUser);
        verify(clerkSdkService).completeOnboarding(clerkUser, id.toString());
    }

    @Test
    void onboardAuthenticatedUser_ReturnsNull_WhenAlreadyExistsByClerkId() {
        // arrange
        User expected = null;
        when(sessionService.isAuthenticatedUserOnboarded()).thenReturn(true);

        // act
        User actual = clerkOnboardingService.onboardAuthenticatedUser();

        // assert
        assertEquals(expected, actual);
        verify(sessionService).isAuthenticatedUserOnboarded();
        verify(sessionService, never()).getAuthenticatedUserClerkId();
        verify(clerkSdkService, never()).getUser(anyString());
        verify(clerkSyncService, never()).ingestUserUpserted(any(ClerkUser.class));
        verify(clerkSdkService, never()).completeOnboarding(any(ClerkUser.class), anyString());
    }

}
