package com.example.echo_api.service.clerk.sdk;

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
import com.example.echo_api.util.Utils;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for interacting with the {@link Clerk} SDK.
 * 
 * @see https://github.com/clerk/clerk-sdk-java
 */
@Service
@RequiredArgsConstructor
public class ClerkSdkServiceImpl implements ClerkSdkService {

    private final Clerk clerk;

    @Override
    public User getUser(String clerkUserId) throws ClerkException {
        Utils.checkNotNull(clerkUserId, "Clerk User ID");

        try {
            GetUserResponse res = clerk.users().get(clerkUserId);
            return res.user().orElseThrow();
        } catch (Exception ex) {
            throw new ClerkException(ex.getMessage());
        }
    }

    @Override
    public void completeOnboarding(User user, String externalId) throws ClerkException {
        Utils.checkNotNull(user, "Clerk User");
        Utils.checkNotNull(externalId, "External ID");

        String userId = user.id().orElseThrow();
        Map<String, Object> metadata = user.publicMetadata().orElseThrow();
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

}
