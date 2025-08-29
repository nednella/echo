package com.example.echo_api.modules.clerk.service;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.modules.clerk.dto.ClerkUser;
import com.example.echo_api.persistence.model.user.User;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation intended for development and testing environments
 * only.
 */
@Profile({ "dev", "test" })
@Service
@RequiredArgsConstructor
public class ClerkDevService {

    private final ClerkSdkService clerkSdkService;
    private final ClerkSyncService clerkSyncService;

    @Transactional
    public User persistClerkUser(ClerkUser user) {
        return clerkSyncService.ingestUserUpserted(user);
    }

    @Transactional
    public List<User> persistAllClerkUsers() {
        List<ClerkUser> users = clerkSdkService.getAllUsers();
        return users.stream()
            .map(this::persistClerkUser)
            .toList();
    }

    public void unsyncAllClerkUsers() {
        clerkSdkService.getAllUsers().forEach(clerkSdkService::revertOnboarding);
    }

}
