package com.example.echo_api.modules.clerk.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.clerk.backend_api.Clerk;
import com.example.echo_api.persistence.dto.adapter.ClerkUserDTO;
import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEvent;
import com.example.echo_api.persistence.dto.request.clerk.webhook.data.UserDelete;
import com.example.echo_api.persistence.dto.request.clerk.webhook.data.UserUpsert;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.service.user.UserService;
import com.example.echo_api.shared.service.SessionService;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for maintaining local database synchronisation with
 * {@link Clerk}.
 */
@Service
@RequiredArgsConstructor
public class ClerkSyncServiceImpl implements ClerkSyncService {

    private final SessionService sessionService;
    private final ClerkSdkService clerkSdkService;
    private final UserService userService;

    @Override
    @Transactional
    public User onboardAuthenticatedUser() {
        if (sessionService.isAuthenticatedUserOnboarded()) {
            return null;
        }

        String clerkId = sessionService.getAuthenticatedUserClerkId();
        ClerkUserDTO clerkUser = clerkSdkService.getUser(clerkId);

        User user = userService.upsertFromExternalSource(clerkId, clerkUser.username(), clerkUser.imageUrl());
        clerkSdkService.completeOnboarding(clerkUser, user.getId().toString());

        return user;
    }

    @Override
    public void handleWebhookEvent(ClerkWebhookEvent event) {
        switch (event.type()) {
            case USER_CREATED, USER_UPDATED -> handleUserUpsert((UserUpsert) event.data());
            case USER_DELETED -> handleUserDelete((UserDelete) event.data());
        }
    }

    private User handleUserUpsert(UserUpsert data) {
        return userService.upsertFromExternalSource(data.id(), data.username(), data.imageUrl());
    }

    private int handleUserDelete(UserDelete data) {
        return userService.deleteFromExternalSource(data.id());
    }

}
