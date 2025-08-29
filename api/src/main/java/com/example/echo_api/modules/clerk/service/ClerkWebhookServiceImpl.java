package com.example.echo_api.modules.clerk.service;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.clerk.backend_api.Clerk;
import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.exception.custom.unauthorised.WebhookVerificationException;
import com.example.echo_api.modules.clerk.dto.webhook.ClerkWebhook;
import com.example.echo_api.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svix.Webhook;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for verifying and deserializing received asynchronous
 * {@link Clerk} webhook notifications.
 */
@Service
@RequiredArgsConstructor
class ClerkWebhookServiceImpl implements ClerkWebhookService {

    private final ClerkSyncService clerkSyncService;

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
        clerkSyncService.handleWebhookEvent(event);
    }

    /**
     * Deserializes a webhook JSON payload string into an appropriate
     * {@link ClerkWebhook} based on its type information.
     * 
     * @param payload The JSON payload string to deserialize
     * @return The mapped {@link ClerkWebhook}
     * @throws DeserializationException If there was an issue when deserializing the
     *                                  JSON payload for whatever reason
     */
    ClerkWebhook deserializePayload(String payload) {
        try {
            return mapper.readValue(payload, ClerkWebhook.class);
        } catch (Exception ex) {
            throw new DeserializationException(ex.getMessage());
        }
    }

}
