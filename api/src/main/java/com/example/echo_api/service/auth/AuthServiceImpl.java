package com.example.echo_api.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clerk.backend_api.Clerk;
import com.example.echo_api.exception.custom.badrequest.OnboardingCompleteException;
import com.example.echo_api.exception.custom.internalserver.ClerkException;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.persistence.repository.UserRepository;
import com.example.echo_api.service.clerk.sdk.ClerkSdkService;
import com.example.echo_api.service.session.SessionService;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing {@link User} synchronisation between
 * Clerk and the local application.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SessionService sessionService;
    private final ClerkSdkService clerkSdkService;

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public User onboard() throws ClerkException, OnboardingCompleteException {
        String clerkId = sessionService.getAuthenticatedUserClerkId();
        validateOnboardingNotComplete(clerkId);

        var clerkUser = clerkSdkService.getUser(clerkId);
        String username = clerkUser.username().get();
        String imageUrl = clerkUser.imageUrl().orElse(null);

        User user = User.fromClerk(clerkId, username);
        userRepository.save(user);

        Profile profile = new Profile(user.getId(), user.getUsername());
        profile.setAvatarUrl(imageUrl);
        profileRepository.save(profile);

        clerkSdkService.setExternalId(clerkId, user.getId().toString());
        clerkSdkService.completeOnboarding(clerkId);

        return user;
    }

    /**
     * Validate that the authenticated user's Clerk ID does not already have a
     * synchronised {@link User} in the database.
     * 
     * @param clerkId The {@link Clerk} user ID to validate.
     * @throws OnboardingCompleteException If the authenticated user's Clerk ID
     *                                     already has a synchronised {@link User}
     *                                     object in the database.
     */
    private void validateOnboardingNotComplete(String clerkId) throws OnboardingCompleteException {
        if (userRepository.existsByClerkId(clerkId)) {
            throw new OnboardingCompleteException();
        }
    }

}
