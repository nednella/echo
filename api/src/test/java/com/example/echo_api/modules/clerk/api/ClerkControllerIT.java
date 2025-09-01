package com.example.echo_api.modules.clerk.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.integration.util.ClerkTestUtils.Template;
import com.example.echo_api.modules.user.entity.User;

/**
 * Integration test class for {@link ClerkController}.
 */
class ClerkControllerIT extends IntegrationTest {

    private static final String ONBOARDING_PATH = ApiRoutes.CLERK.ONBOARDING;

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
