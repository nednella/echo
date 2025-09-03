package com.example.echo_api.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.testing.support.AbstractIntegrationTest;
import com.example.echo_api.testing.support.ClerkTestUtils.Template;

/**
 * Integration test class for {@link ClerkOnboardingFilter}.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ClerkOnboardingFilterIT extends AbstractIntegrationTest {

    private static final String PROTECTED_ENDPOINT = ApiRoutes.PROFILE.ME; // random protected endpoint

    @Test
    void protectedEndpoint_Returns403Forbidden_WhenMissingOnboardedClaim() {
        String clerkId = mockUser.getExternalId();
        String token = clerkTestUtils.getSessionTokenFromTemplate(clerkId, Template.MISSING_ONBOARDED_CLAIM);

        assertThat(token).isNotBlank();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.FORBIDDEN,
            "Required token claim 'onboarded' is missing",
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer ").append(token).toString())
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void protectedEndpoint_Returns403Forbidden_WhenOnboardedClaimIsMalformed() {
        String clerkId = mockUser.getExternalId();
        String token = clerkTestUtils.getSessionTokenFromTemplate(clerkId, Template.MALFORMED_ONBOARDED_CLAIM);

        assertThat(token).isNotBlank();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.FORBIDDEN,
            "Token claim 'onboarded' contains an unexpected value",
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer ").append(token).toString())
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void protectedEndpoint_Returns403Forbidden_WhenMissingEchoIdClaim() {
        String clerkId = mockUser.getExternalId();
        String token = clerkTestUtils.getSessionTokenFromTemplate(clerkId, Template.MISSING_ECHO_ID_CLAIM);

        assertThat(token).isNotBlank();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.FORBIDDEN,
            "Required token claim 'echo_id' is missing",
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer ").append(token).toString())
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void protectedEndpoint_Returns403Forbidden_WhenEchoIdClaimIsMalformed() {
        String clerkId = mockUser.getExternalId();
        String token = clerkTestUtils.getSessionTokenFromTemplate(clerkId, Template.MALFORMED_ECHO_ID_CLAIM);

        assertThat(token).isNotBlank();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.FORBIDDEN,
            "Token claim 'echo_id' contains an unexpected value",
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer ").append(token).toString())
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void protectedEndpoint_Returns403Forbidden_WhenOnboardedIsFalse() {
        String clerkId = mockUser.getExternalId();
        String token = clerkTestUtils.getSessionTokenFromTemplate(clerkId, Template.VALID_TOKEN);

        assertThat(token).isNotBlank();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.FORBIDDEN,
            "User has not completed the onboarding process",
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer ").append(token).toString())
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

}
