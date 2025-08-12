package com.example.echo_api.service.clerk.webhook;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.clerk.backend_api.Clerk;
import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.exception.custom.unauthorised.WebhookVerificationException;
import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEvent;
import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEventType;
import com.example.echo_api.persistence.dto.request.clerk.webhook.data.UserDeleted;
import com.example.echo_api.persistence.dto.request.clerk.webhook.data.UserUpdated;
import com.example.echo_api.service.user.UserService;
import com.example.echo_api.util.Utils;
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
    public void verify(HttpHeaders headers, String payload) {
        try {
            svixWebhook.verify(payload, Utils.convertHeaders(headers));
        } catch (Exception ex) {
            throw new WebhookVerificationException();
        }
    }

    @Override
    public void handleWebhook(String payload) {
        var event = deserializePayload(payload);
        dispatchEvent(event);
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
    ClerkWebhookEvent deserializePayload(String payload) {
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
    void dispatchEvent(ClerkWebhookEvent event) {
        switch (event.type()) {
            case USER_UPDATED -> userService.handleClerkUserUpdated((UserUpdated) event.data());
            case USER_DELETED -> userService.handleClerkUserDeleted((UserDeleted) event.data());
        }
    }

}
