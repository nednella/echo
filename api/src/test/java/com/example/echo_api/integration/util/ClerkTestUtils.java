package com.example.echo_api.integration.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.clerk.backend_api.Clerk;
import com.clerk.backend_api.models.components.User;
import com.clerk.backend_api.models.operations.CreateSessionRequestBody;
import com.clerk.backend_api.models.operations.CreateUserRequestBody;
import com.clerk.backend_api.models.operations.CreateUserResponse;
import com.clerk.backend_api.models.operations.UpdateUserRequestBody;
import com.example.echo_api.config.ClerkConfig;
import com.example.echo_api.exception.custom.internalserver.ClerkException;
import com.example.echo_api.persistence.dto.adapter.ClerkUserDTO;
import com.example.echo_api.persistence.mapper.ClerkUserMapper;

@Component
public class ClerkTestUtils {

    private static final String TOKEN_TEMPLATE = "echo-api-test";

    @Autowired
    private Clerk clerk;

    public ClerkUserDTO createUser(String email, String username) {
        CreateUserRequestBody request = CreateUserRequestBody.builder()
            .emailAddress(List.of(email))
            .username(username)
            .build();

        try {
            CreateUserResponse response = clerk.users().create().request(request).call();
            User user = response.user().orElseThrow();
            return ClerkUserMapper.toDTO(user);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create Clerk user: " + username);
        }
    }

    public void deleteUser(String clerkUserId) {
        try {
            clerk.users().delete(clerkUserId);
        } catch (Exception ex) {
            throw new RuntimeException("Could not delete Clerk user: " + clerkUserId);
        }
    }

    public void completeOnboarding(String clerkUserId, String externalId) {
        Map<String, Object> metadata = new HashMap<>();
        metadata.put(ClerkConfig.ONBOARDING_COMPLETE_METADATA_KEY, ClerkConfig.ONBOARDING_COMPLETE_METADATA_VALUE);

        try {
            clerk.users().update(
                clerkUserId,
                UpdateUserRequestBody.builder()
                    .externalId(externalId)
                    .publicMetadata(metadata)
                    .build());
        } catch (Exception ex) {
            throw new ClerkException(ex.getMessage());
        }
    }

    public String getSessionTokenForUser(String clerkUserId) {
        CreateSessionRequestBody request = CreateSessionRequestBody.builder()
            .userId(clerkUserId)
            .build();

        try {
            var createSessionResponse = clerk.sessions().create().request(request).call();
            String sessId = createSessionResponse.session().orElseThrow().id();

            var createTokenResponse = clerk.sessions().createTokenFromTemplate(sessId, TOKEN_TEMPLATE);
            return createTokenResponse.object().get().jwt().orElseThrow();
        } catch (Exception ex) {
            throw new RuntimeException("Could not generate token for Clerk user: " + clerkUserId);
        }
    }

}
