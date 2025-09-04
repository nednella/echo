package com.example.echo_api.modules.clerk.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.clerk.exception.ClerkErrorCode;
import com.example.echo_api.modules.clerk.service.ClerkOnboardingService;
import com.example.echo_api.modules.clerk.service.ClerkWebhookService;
import com.example.echo_api.modules.user.entity.User;
import com.example.echo_api.shared.constant.ApiRoutes;

/**
 * Unit test class for {@link ClerkController}.
 */
@WebMvcTest(ClerkController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClerkControllerTest {

    private static final String ONBOARDING_PATH = ApiRoutes.CLERK.ONBOARDING;
    private static final String WEBHOOK_PATH = ApiRoutes.CLERK.WEBHOOK;
    private static final String WEBHOOK_PAYLOAD = "some_placeholder_payload";

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private ClerkOnboardingService clerkOnboardingService;

    @MockitoBean
    private ClerkWebhookService clerkWebhookService;

    @Test
    void clerkOnboarding_Returns201User_WhenExternalUserInserted() {
        // api: POST /api/v1/clerk/onboarding ==> 201 Created : User
        User expected = User.forTest(UUID.randomUUID(), "user_someUniqueString");
        when(clerkOnboardingService.onboardAuthenticatedUser()).thenReturn(expected);

        var response = mvc.post()
            .uri(ONBOARDING_PATH)
            .exchange();

        assertThat(response)
            .hasStatus(201)
            .bodyJson().convertTo(User.class).isEqualTo(expected);

        verify(clerkOnboardingService).onboardAuthenticatedUser();
    }

    @Test
    void clerkOnboarding_Returns201Empty_WhenExternalUserUpdated() {
        // api: POST /api/v1/clerk/onboarding ==> 201 Created : []
        User expected = null;
        when(clerkOnboardingService.onboardAuthenticatedUser()).thenReturn(expected);

        var response = mvc.post()
            .uri(ONBOARDING_PATH)
            .exchange();

        assertThat(response)
            .hasStatus(201)
            .body().isEmpty();

        verify(clerkOnboardingService).onboardAuthenticatedUser();
    }

    @Test
    void clerkEvent_Returns204NoContent_WhenWebhookSucessfullyHandled() {
        // api: POST /api/v1/clerk/webhook ==> 204 No Content
        var response = mvc.post()
            .uri(WEBHOOK_PATH)
            .content(WEBHOOK_PAYLOAD)
            .exchange();

        assertThat(response)
            .hasStatus(204)
            .body().isEmpty();

        verify(clerkWebhookService).verify(any(HttpHeaders.class), eq(WEBHOOK_PAYLOAD));
        verify(clerkWebhookService).handleWebhook(WEBHOOK_PAYLOAD);
    }

    @Test
    void clerkEvent_Returns401Unauthorised_WhenWebhookCannotBeVerified() {
        // api: POST /api/v1/clerk/webhook ==> 401 Unauthorised : ErrorDTO
        ClerkErrorCode errorCode = ClerkErrorCode.WEBHOOK_SIGNATURE_INVALID;

        doThrow(errorCode.buildAsException())
            .when(clerkWebhookService).verify(any(HttpHeaders.class), eq(WEBHOOK_PAYLOAD));

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.UNAUTHORIZED,
            errorCode.formatMessage(),
            null);

        var response = mvc.post()
            .uri(WEBHOOK_PATH)
            .content(WEBHOOK_PAYLOAD)
            .exchange();

        assertThat(response)
            .hasStatus(401)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(clerkWebhookService).verify(any(HttpHeaders.class), eq(WEBHOOK_PAYLOAD));
        verify(clerkWebhookService, never()).handleWebhook(WEBHOOK_PAYLOAD);
    }

    @Test
    void clerkEvent_Returns400BadRequest_WhenWebhookEventCannotBeDeserialized() {
        // api: POST /api/v1/clerk/webhook ==> 400 Bad Request
        ClerkErrorCode errorCode = ClerkErrorCode.WEBHOOK_PAYLOAD_INVALID;

        doThrow(errorCode.buildAsException("Some exception message"))
            .when(clerkWebhookService).handleWebhook(WEBHOOK_PAYLOAD);

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Some exception message",
            null);

        var response = mvc.post()
            .uri(WEBHOOK_PATH)
            .content(WEBHOOK_PAYLOAD)
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(clerkWebhookService).verify(any(HttpHeaders.class), eq(WEBHOOK_PAYLOAD));
        verify(clerkWebhookService).handleWebhook(WEBHOOK_PAYLOAD);
    }

}
