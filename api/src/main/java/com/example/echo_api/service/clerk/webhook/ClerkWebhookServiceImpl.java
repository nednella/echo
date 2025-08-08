package com.example.echo_api.service.clerk.webhook;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.clerk.backend_api.Clerk;
import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.exception.custom.unauthorised.WebhookVerificationException;
import com.example.echo_api.persistence.dto.request.webhook.clerk.ClerkWebhookEvent;
import com.example.echo_api.persistence.dto.request.webhook.clerk.ClerkWebhookEventType;
import com.example.echo_api.persistence.dto.request.webhook.clerk.data.UserDeleted;
import com.example.echo_api.persistence.dto.request.webhook.clerk.data.UserUpdated;
import com.example.echo_api.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svix.Webhook;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for acting upon received asynchronous {@link Clerk}
 * webhook notifications to maintain local database synchronisation.
 */
@Service
@RequiredArgsConstructor
public class ClerkWebhookServiceImpl implements ClerkWebhookService {

    private final UserService userService;

    private final Webhook svixWebhook;
    private final ObjectMapper mapper;

    @Override
    public void handleWebhook(HttpHeaders headers, String payload) {
        verify(headers, payload);
        var event = deserializePayload(payload);
        dispatchEvent(event);
    }

    /**
     * Verify the authenticity of a Clerk webhook received at a
     * {@link ApiConfig.Webhook} endpoint.
     * 
     * <p>
     * The webhook authenticity is verified against the Clerk signing secret using
     * the Svix library.
     * 
     * @param headers The HTTP headers containing the Svix webhook metadata
     * @param payload The raw webhook JSON payload string
     * @throws WebhookVerificationException If the webhook signature verification
     *                                      fails
     */
    private void verify(HttpHeaders headers, String payload) {
        try {
            svixWebhook.verify(payload, convertHeaders(headers));
        } catch (Exception ex) {
            throw new WebhookVerificationException();
        }
    }

    /**
     * Deserializes a webhook JSON payload string into an appropriate
     * {@link ClerkWebhookEvent} based on its type information.
     * 
     * @param payload The JSON payload string to deserialize
     * @return The mapped {@link ClerkWebhookEvent}
     * @throws DeserializationException If there was an issue when deserializing the
     *                                  JSON payload for whatever reason
     */
    private ClerkWebhookEvent deserializePayload(String payload) {
        try {
            return mapper.readValue(payload, ClerkWebhookEvent.class);
        } catch (Exception ex) {
            throw new DeserializationException(ex.getMessage());
        }
    }

    /**
     * Check the {@link ClerkWebhookEventType} and pass the event data to the
     * appropriate handler.
     * 
     * @param event The {@link ClerkWebhookEvent} to handle
     */
    private void dispatchEvent(ClerkWebhookEvent event) {
        switch (event.type()) {
            case USER_UPDATED -> userService.handleClerkUserUpdated((UserUpdated) event.data());
            case USER_DELETED -> userService.handleClerkUserDeleted((UserDeleted) event.data());
        }
    }

    /**
     * Helper method to convert Spring HttpHeaders into Java HttpHeaders to satisfy
     * the webhook verification method.
     * 
     * @param headers {@link org.springframework.http.HttpHeaders}
     * @return {@link java.net.http.HttpHeaders}
     */
    private java.net.http.HttpHeaders convertHeaders(HttpHeaders headers) {
        return java.net.http.HttpHeaders.of(headers, (t, v) -> true);
    }

}
