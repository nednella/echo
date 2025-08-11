package com.example.echo_api.unit.service.auth.onboarding;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.exception.custom.badrequest.ClerkIdAlreadyExistsException;
import com.example.echo_api.exception.custom.badrequest.OnboardingCompleteException;
import com.example.echo_api.exception.custom.internalserver.ClerkException;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.service.auth.onboarding.OnboardingServiceImpl;
import com.example.echo_api.service.auth.session.SessionService;
import com.example.echo_api.service.clerk.sdk.ClerkSdkService;
import com.example.echo_api.service.user.UserService;

@ExtendWith(MockitoExtension.class)
class OnboardingServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private SessionService sessionService;

    @Mock
    private ClerkSdkService clerkSdkService;

    @InjectMocks
    private OnboardingServiceImpl onboardingService;

    com.clerk.backend_api.models.components.User createClerkUser(String id, String username, String imgUrl) {
        return com.clerk.backend_api.models.components.User
            .builder()
            .id(id)
            .username(username)
            .imageUrl(imgUrl)
            .build();
    }

    @Test
    void onboard_CompletesOnboarding() {
        // arrange
        String clerkId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        var clerkUser = createClerkUser(clerkId, username, imageUrl);

        UUID userId = UUID.randomUUID();
        User user = User.forTest(userId, clerkId, username);

        when(sessionService.isAuthenticatedUserOnboarded()).thenReturn(false);
        when(sessionService.getAuthenticatedUserClerkId()).thenReturn(clerkId);
        when(clerkSdkService.getUser(clerkId)).thenReturn(clerkUser);
        when(userService.createUserWithProfile(clerkId, username, imageUrl)).thenReturn(user);
        doNothing().when(clerkSdkService).setExternalId(clerkId, userId.toString());
        doNothing().when(clerkSdkService).completeOnboarding(clerkId);

        // act
        assertDoesNotThrow(() -> onboardingService.onboard());

        // assert
        verify(sessionService).isAuthenticatedUserOnboarded();
        verify(sessionService).getAuthenticatedUserClerkId();
        verify(clerkSdkService).getUser(clerkId);
        verify(userService).createUserWithProfile(clerkId, username, imageUrl);
        verify(clerkSdkService).setExternalId(clerkId, userId.toString());
        verify(clerkSdkService).completeOnboarding(clerkId);
    }

    @Test
    void onboard_ThrowsWhenUserAlreadyOnboarded() {
        // arrange
        when(sessionService.isAuthenticatedUserOnboarded()).thenReturn(true);

        // act & assert
        assertThrows(OnboardingCompleteException.class, () -> onboardingService.onboard());
    }

    @Test
    void onboard_ThrowsWhenClerkIdAlreadyExists() {
        // arrange
        String clerkId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        var clerkUser = createClerkUser(clerkId, username, imageUrl);

        when(sessionService.isAuthenticatedUserOnboarded()).thenReturn(false);
        when(sessionService.getAuthenticatedUserClerkId()).thenReturn(clerkId);
        when(clerkSdkService.getUser(clerkId)).thenReturn(clerkUser);
        when(userService.createUserWithProfile(clerkId, username, imageUrl))
            .thenThrow(ClerkIdAlreadyExistsException.class);

        // act & assert
        assertThrows(ClerkIdAlreadyExistsException.class, () -> onboardingService.onboard());
    }

    @Test
    void onboard_ThrowsWhenClerkApiCallFails() {
        // arrange
        String clerkId = "user_someRandomStringThatIsUniqueApparently";

        when(sessionService.isAuthenticatedUserOnboarded()).thenReturn(false);
        when(sessionService.getAuthenticatedUserClerkId()).thenReturn(clerkId);
        when(clerkSdkService.getUser(clerkId)).thenThrow(ClerkException.class);

        // act & assert
        assertThrows(ClerkException.class, () -> onboardingService.onboard());
    }

}
