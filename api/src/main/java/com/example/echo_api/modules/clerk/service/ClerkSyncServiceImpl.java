package com.example.echo_api.modules.clerk.service;

import org.springframework.stereotype.Service;

import com.clerk.backend_api.Clerk;
import com.example.echo_api.modules.clerk.dto.ClerkUser;
import com.example.echo_api.modules.user.entity.User;
import com.example.echo_api.modules.user.service.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for maintaining local application synchronisation with
 * {@link Clerk}.
 */
@Service
@RequiredArgsConstructor
class ClerkSyncServiceImpl implements ClerkSyncService {

    private final UserService userService;

    @Override
    public User ingestUserUpserted(ClerkUser user) {
        return userService.upsertFromExternalSource(user.id(), user.username(), user.imageUrl());
    }

    @Override
    public void ingestUserDeleted(String clerkId) {
        userService.deleteFromExternalSource(clerkId);
    }

}