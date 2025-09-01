package com.example.echo_api.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.integration.util.ClerkTestUtils.Template;
import com.example.echo_api.shared.dto.ErrorDTO;

/**
 * Integration test class for {@link ClerkOnboardingFilter}.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ClerkOnboardingFilterIT extends IntegrationTest {

    private static final String PROTECTED_ENDPOINT = ApiRoutes.PROFILE.ME; // random protected endpoint

    @Test
    void protectedEndpoint_Returns403Forbidden_WhenMissingOnboardedClaim() {
        String clerkId = mockUser.getExternalId();
        String token = clerkTestUtils.getSessionTokenFromTemplate(clerkId, Template.MISSING_ONBOARDED_CLAIM);

        assertThat(token).isNotBlank();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.FORBIDDEN,
            ErrorMessageConfig.Forbidden.ONBOARDED_CLAIM_MISSING,
            null,
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer ").append(token).toString())
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test
    void protectedEndpoint_Returns403Forbidden_WhenOnboardedClaimIsMalformed() {
        String clerkId = mockUser.getExternalId();
        String token = clerkTestUtils.getSessionTokenFromTemplate(clerkId, Template.MALFORMED_ONBOARDED_CLAIM);

        assertThat(token).isNotBlank();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.FORBIDDEN,
            ErrorMessageConfig.Forbidden.ONBOARDED_CLAIM_MALFORMED,
            null,
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer ").append(token).toString())
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test
    void protectedEndpoint_Returns403Forbidden_WhenMissingEchoIdClaim() {
        String clerkId = mockUser.getExternalId();
        String token = clerkTestUtils.getSessionTokenFromTemplate(clerkId, Template.MISSING_ECHO_ID_CLAIM);

        assertThat(token).isNotBlank();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.FORBIDDEN,
            ErrorMessageConfig.Forbidden.ECHO_ID_CLAIM_MISSING,
            null,
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer ").append(token).toString())
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test
    void protectedEndpoint_Returns403Forbidden_WhenEchoIdClaimIsMalformed() {
        String clerkId = mockUser.getExternalId();
        String token = clerkTestUtils.getSessionTokenFromTemplate(clerkId, Template.MALFORMED_ECHO_ID_CLAIM);

        assertThat(token).isNotBlank();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.FORBIDDEN,
            ErrorMessageConfig.Forbidden.ECHO_ID_CLAIM_MALFORMED,
            null,
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer ").append(token).toString())
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test
    void protectedEndpoint_Returns403Forbidden_WhenOnboardedIsFalse() {
        String clerkId = mockUser.getExternalId();
        String token = clerkTestUtils.getSessionTokenFromTemplate(clerkId, Template.VALID_TOKEN);

        assertThat(token).isNotBlank();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.FORBIDDEN,
            ErrorMessageConfig.Forbidden.ONBOARDING_NOT_COMPLETED,
            null,
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, new StringBuilder("Bearer ").append(token).toString())
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

}
