package com.example.echo_api.testing.support;

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
import com.clerk.backend_api.models.operations.GetUserListResponse;
import com.clerk.backend_api.models.operations.UpdateUserRequestBody;
import com.example.echo_api.config.ClerkConfig;
import com.example.echo_api.modules.clerk.dto.ClerkUser;
import com.example.echo_api.modules.clerk.mapper.ClerkUserMapper;

@Component
public class ClerkTestUtils {

    public static enum Template {

        VALID_TOKEN("echo-api-test"),
        MISSING_ONBOARDED_CLAIM("missing-onboarded-claim"),
        MALFORMED_ONBOARDED_CLAIM("malformed-onboarded-claim"),
        MISSING_ECHO_ID_CLAIM("missing-echo_id-claim"),
        MALFORMED_ECHO_ID_CLAIM("malformed-echo_id-claim");

        private String templateName;

        Template(String templateName) {
            this.templateName = templateName;
        }

        public String getTemplateName() {
            return this.templateName;
        }

    }

    @Autowired
    private Clerk clerk;

    public void deleteAllExistingUsers() {
        try {
            GetUserListResponse res = clerk.users().list().call();
            List<User> users = res.userList().orElseThrow();
            users.forEach(this::deleteUser);
        } catch (Exception ex) {
            throw new RuntimeException("There was an issue cleaning the Clerk user db: " + ex.getMessage());
        }
    }

    public ClerkUser createUser(String email, String username) {
        CreateUserRequestBody request = CreateUserRequestBody.builder()
            .emailAddress(List.of(email))
            .username(username)
            .build();

        try {
            CreateUserResponse response = clerk.users().create().request(request).call();
            User user = response.user().orElseThrow();
            return ClerkUserMapper.fromSDK(user);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create Clerk user: " + username);
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
            throw new RuntimeException("Could not complete onboarding for Clerk user: " + clerkUserId);
        }
    }

    public String getSessionTokenFromTemplate(String clerkUserId, Template template) {
        CreateSessionRequestBody request = CreateSessionRequestBody.builder()
            .userId(clerkUserId)
            .build();

        try {
            var createSessionResponse = clerk.sessions().create().request(request).call();
            String sessId = createSessionResponse.session().orElseThrow().id();

            var createTokenResponse = clerk.sessions().createTokenFromTemplate(sessId, template.getTemplateName());
            return createTokenResponse.object().get().jwt().orElseThrow();
        } catch (Exception ex) {
            throw new RuntimeException("Could not generate token for Clerk user: " + clerkUserId);
        }
    }

    // ---- helpers ----

    private void deleteUser(User user) {
        try {
            clerk.users().delete(user.id().get());
        } catch (Exception ex) {
            throw new RuntimeException("Could not delete Clerk user: " + user.username().get());
        }
    }

}
