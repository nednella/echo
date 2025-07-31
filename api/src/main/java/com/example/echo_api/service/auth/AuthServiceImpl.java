package com.example.echo_api.service.auth;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.badrequest.OnboardingCompleteException;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.persistence.repository.UserRepository;
import com.example.echo_api.service.clerk.ClerkService;
import com.example.echo_api.service.session.SessionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SessionService sessionService;
    private final ClerkService clerkService;

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public User onboard() {
        String clerkId = sessionService.getAuthenticatedUserClerkId();
        validateOnboardingNotComplete(clerkId);

        var clerkUser = clerkService.getUser(clerkId);
        String username = clerkUser.username().get();
        String imageUrl = clerkUser.imageUrl().orElse(null);

        User user = new User(clerkId, username);
        userRepository.save(user);

        Profile profile = new Profile(user.getId(), user.getUsername());
        profile.setAvatarUrl(imageUrl);
        profileRepository.save(profile);

        clerkService.setExternalId(clerkId, user.getId().toString());
        clerkService.completeOnboarding(clerkId);

        return user;
    }

    private void validateOnboardingNotComplete(String clerkId) {
        if (userRepository.existsByClerkId(clerkId)) {
            throw new OnboardingCompleteException();
        }
    }

}
