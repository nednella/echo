package com.example.echo_api.modules.clerk.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.modules.clerk.dto.ClerkUser;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.shared.service.SessionService;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for onboarding Clerk users into the application.
 */
@Service
@RequiredArgsConstructor
class ClerkOnboardingServiceImpl implements ClerkOnboardingService {

    private final SessionService sessionService;
    private final ClerkSdkService sdkService;
    private final ClerkSyncService syncSerivce;

    @Override
    @Transactional
    public User onboardAuthenticatedUser() {
        if (sessionService.isAuthenticatedUserOnboarded()) {
            return null;
        }

        String clerkId = sessionService.getAuthenticatedUserClerkId();
        ClerkUser clerkUser = sdkService.getUser(clerkId);

        User user = syncSerivce.ingestUserUpserted(clerkUser);
        sdkService.completeOnboarding(clerkUser, user.getId().toString());

        return user;
    }

}
