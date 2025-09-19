package com.example.echo_api.exception.handler;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.testing.support.AbstractIntegrationTest;

class AuthenticationExceptionHandlerIT extends AbstractIntegrationTest {

    private static final String PROTECTED_ENDPOINT = ApiRoutes.PROFILE.ME; // random protected endpoint

    @Test
    void protectedEndpoint_Returns401Unauthorized_WhenMissingBearerToken() {
        ErrorResponse expected = new ErrorResponse(
            HttpStatus.UNAUTHORIZED,
            "Authentication token required",
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void protectedEndpoint_Returns401Unauthorized_WhenInvalidBearerToken() {
        ErrorResponse expected = new ErrorResponse(
            HttpStatus.UNAUTHORIZED,
            "Invalid authentication token",
            null);

        unauthenticatedClient.get()
            .uri(PROTECTED_ENDPOINT)
            .header(HttpHeaders.AUTHORIZATION, "Bearer 123123123")
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

}
