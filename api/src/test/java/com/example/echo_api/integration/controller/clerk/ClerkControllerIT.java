package com.example.echo_api.integration.controller.clerk;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.controller.clerk.ClerkController;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.integration.util.ClerkTestUtils.Template;
import com.example.echo_api.persistence.model.user.User;

/**
 * Integration test class for {@link ClerkController}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ClerkControllerIT extends IntegrationTest {

    private static final String ONBOARDING_PATH = ApiConfig.Clerk.ONBOARDING;

    @Test
    void clerkOnboarding_Returns201User_WhenExternalUserNotOnboarded() {
        // api: POST /api/v1/clerk/onboarding ==> 201 Created : User

        // onboard mock_user as not onboarded as part of the IntegrationTest setup
        String token = clerkTestUtils.getSessionTokenFromTemplate(mockUser.getExternalId(), Template.VALID_TOKEN);
        assertThat(token).isNotBlank();

        User user = unauthenticatedClient.post()
            .uri(ONBOARDING_PATH)
            .header(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer ").append(token).toString())
            .exchange()
            .expectStatus().isCreated()
            .expectBody(User.class)
            .returnResult()
            .getResponseBody();

        assertThat(user.getId()).isEqualTo(mockUser.getId());
        assertThat(user.getExternalId()).isEqualTo(mockUser.getExternalId());
    }

    @Test
    void clerkOnboarding_Returns201Empty_WhenExternalUserAlreadyOnboarded() {
        // api: POST /api/v1/clerk/onboarding ==> 201 Created : []

        // onboard auth_user as already onboarded as part of the IntegrationTest setup
        authenticatedClient.post()
            .uri(ONBOARDING_PATH)
            .exchange()
            .expectStatus().isCreated()
            .expectBody().isEmpty();
    }

}
