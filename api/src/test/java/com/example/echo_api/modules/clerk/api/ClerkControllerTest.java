package com.example.echo_api.modules.clerk.api;

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

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.exception.custom.unauthorised.WebhookVerificationException;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.service.clerk.sync.ClerkSyncService;
import com.example.echo_api.service.clerk.webhook.ClerkWebhookService;

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
    private ClerkSyncService clerkSyncService;

    @MockitoBean
    private ClerkWebhookService clerkWebhookService;

    @Test
    void clerkOnboarding_Returns201User_WhenExternalUserInserted() {
        // api: POST /api/v1/clerk/onboarding ==> 201 Created : User
        User expected = User.forTest(UUID.randomUUID(), "user_someUniqueString");
        when(clerkSyncService.onboardAuthenticatedUser()).thenReturn(expected);

        var response = mvc.post()
            .uri(ONBOARDING_PATH)
            .exchange();

        assertThat(response)
            .hasStatus(201)
            .bodyJson().convertTo(User.class).isEqualTo(expected);

        verify(clerkSyncService).onboardAuthenticatedUser();
    }

    @Test
    void clerkOnboarding_Returns201Empty_WhenExternalUserUpdated() {
        // api: POST /api/v1/clerk/onboarding ==> 201 Created : []
        User expected = null;
        when(clerkSyncService.onboardAuthenticatedUser()).thenReturn(expected);

        var response = mvc.post()
            .uri(ONBOARDING_PATH)
            .exchange();

        assertThat(response)
            .hasStatus(201)
            .body().isEmpty();

        verify(clerkSyncService).onboardAuthenticatedUser();
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
        doThrow(new WebhookVerificationException())
            .when(clerkWebhookService).verify(any(HttpHeaders.class), eq(WEBHOOK_PAYLOAD));

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.UNAUTHORIZED,
            ErrorMessageConfig.Unauthorised.INVALID_WEBHOOK_SIGNATURE,
            null,
            null);

        var response = mvc.post()
            .uri(WEBHOOK_PATH)
            .content(WEBHOOK_PAYLOAD)
            .exchange();

        assertThat(response)
            .hasStatus(401)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(clerkWebhookService).verify(any(HttpHeaders.class), eq(WEBHOOK_PAYLOAD));
        verify(clerkWebhookService, never()).handleWebhook(WEBHOOK_PAYLOAD);
    }

    @Test
    void clerkEvent_Returns400BadRequest_WhenWebhookEventCannotBeDeserialized() {
        // api: POST /api/v1/clerk/webhook ==> 400 Bad Request
        doThrow(new DeserializationException("Unsupported event type: subscription.active"))
            .when(clerkWebhookService).handleWebhook(WEBHOOK_PAYLOAD);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            "Unsupported event type: subscription.active",
            null,
            null);

        var response = mvc.post()
            .uri(WEBHOOK_PATH)
            .content(WEBHOOK_PAYLOAD)
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(clerkWebhookService).verify(any(HttpHeaders.class), eq(WEBHOOK_PAYLOAD));
        verify(clerkWebhookService).handleWebhook(WEBHOOK_PAYLOAD);
    }

}
