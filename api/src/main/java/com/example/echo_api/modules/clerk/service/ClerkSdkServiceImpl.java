package com.example.echo_api.modules.clerk.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.clerk.backend_api.Clerk;
import com.clerk.backend_api.models.components.User;
import com.clerk.backend_api.models.operations.GetUserListResponse;
import com.clerk.backend_api.models.operations.GetUserResponse;
import com.clerk.backend_api.models.operations.UpdateUserRequestBody;
import com.example.echo_api.config.ClerkConfig;
import com.example.echo_api.exception.custom.internalserver.ClerkException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.modules.clerk.dto.ClerkUser;
import com.example.echo_api.modules.clerk.mapper.ClerkUserMapper;
import com.example.echo_api.util.Utils;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for interacting with the {@link Clerk} SDK.
 * 
 * @see https://github.com/clerk/clerk-sdk-java
 */
@Service
@RequiredArgsConstructor
class ClerkSdkServiceImpl implements ClerkSdkService {

    private final Clerk clerk;

    @Override
    public ClerkUser getUser(String clerkUserId) throws ClerkException {
        Utils.checkNotNull(clerkUserId, "Clerk User ID");

        try {
            GetUserResponse res = clerk.users().get(clerkUserId);
            User user = res.user().orElseThrow(ResourceNotFoundException::new);
            return ClerkUserMapper.fromSDK(user);
        } catch (Exception ex) {
            throw new ClerkException(ex.getMessage());
        }
    }

    @Override
    public List<ClerkUser> getAllUsers() {
        try {
            GetUserListResponse res = clerk.users().list().call();
            List<User> users = res.userList().orElseThrow(ResourceNotFoundException::new);
            return users.stream()
                .map(ClerkUserMapper::fromSDK)
                .toList();
        } catch (Exception ex) {
            throw new ClerkException(ex.getMessage());
        }
    }

    @Override
    public void completeOnboarding(ClerkUser user, String externalId) throws ClerkException {
        Utils.checkNotNull(user, "Clerk User");
        Utils.checkNotNull(externalId, "External ID");

        String userId = user.id();
        Map<String, Object> metadata = user.publicMetadata();
        metadata.put(ClerkConfig.ONBOARDING_COMPLETE_METADATA_KEY, ClerkConfig.ONBOARDING_COMPLETE_METADATA_VALUE);

        try {
            clerk.users().update(
                userId,
                UpdateUserRequestBody.builder()
                    .externalId(externalId)
                    .publicMetadata(metadata)
                    .build());
        } catch (Exception ex) {
            throw new ClerkException(ex.getMessage());
        }
    }

    @Override
    public void revertOnboarding(ClerkUser user) {
        Utils.checkNotNull(user, "Clerk User");

        String userId = user.id();
        String externalId = ""; // set to empty string
        Map<String, Object> metadata = user.publicMetadata();
        metadata.remove(ClerkConfig.ONBOARDING_COMPLETE_METADATA_KEY);

        try {
            clerk.users().update(
                userId,
                UpdateUserRequestBody.builder()
                    .externalId(externalId)
                    .publicMetadata(metadata)
                    .build());
        } catch (Exception ex) {
            throw new ClerkException(ex.getMessage());
        }
    }

}
