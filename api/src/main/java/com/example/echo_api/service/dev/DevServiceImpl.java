package com.example.echo_api.service.dev;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.modules.clerk.service.ClerkSdkService;
import com.example.echo_api.persistence.dto.adapter.ClerkUserDTO;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.service.user.UserService;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation intended for development and testing environments
 * only.
 */
@Profile({ "dev", "test" })
@Service
@RequiredArgsConstructor
public class DevServiceImpl implements DevService {

    private final ClerkSdkService clerkSdkService;
    private final UserService userService;

    @Override
    @Transactional
    public User persistClerkUser(ClerkUserDTO user) {
        return userService.upsertFromExternalSource(
            user.id(),
            user.username(),
            user.imageUrl());
    }

    @Override
    @Transactional
    public List<User> persistAllClerkUsers() {
        List<ClerkUserDTO> users = clerkSdkService.getAllUsers();
        return users.stream()
            .map(this::persistClerkUser)
            .toList();
    }

    @Override
    public void unsyncAllClerkUsers() {
        clerkSdkService.getAllUsers().forEach(clerkSdkService::revertOnboarding);
    }

}
