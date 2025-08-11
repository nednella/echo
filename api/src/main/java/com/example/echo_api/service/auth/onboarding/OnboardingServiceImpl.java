package com.example.echo_api.service.auth.onboarding;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.badrequest.OnboardingCompleteException;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.service.auth.session.SessionService;
import com.example.echo_api.service.clerk.sdk.ClerkSdkService;
import com.example.echo_api.service.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing {@link User} synchronisation between
 * Clerk and the local application.
 */
@Service
@RequiredArgsConstructor
public class OnboardingServiceImpl implements OnboardingService {

    private final UserService userService;
    private final SessionService sessionService;
    private final ClerkSdkService clerkSdkService;

    @Override
    @Transactional
    public void onboard() {
        validateOnboardingNotComplete();

        String clerkId = sessionService.getAuthenticatedUserClerkId();
        var clerkUser = clerkSdkService.getUser(clerkId);
        String clerkUsername = clerkUser.username().get();
        String clerkImageUrl = clerkUser.imageUrl().orElse(null);

        User user = userService.createUserWithProfile(clerkId, clerkUsername, clerkImageUrl);

        clerkSdkService.setExternalId(clerkId, user.getId().toString());
        clerkSdkService.completeOnboarding(clerkId);
    }

    /**
     * Validate that the authenticated user session token does not contain a boolean
     * flag indicating that the onboarding process has already been completed.
     * 
     * @throws OnboardingCompleteException If the authenticated user already has a
     *                                     synchronised {@link User} entity in the
     *                                     local system.
     */
    private void validateOnboardingNotComplete() throws OnboardingCompleteException {
        if (sessionService.isAuthenticatedUserOnboarded()) {
            throw new OnboardingCompleteException();
        }
    }

}
