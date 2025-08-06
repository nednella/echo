package com.example.echo_api.service.webhook;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.clerk.backend_api.Clerk;
import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.exception.custom.unauthorised.WebhookVerificationException;
import com.example.echo_api.persistence.dto.request.webhook.clerk.ClerkWebhookEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svix.Webhook;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for acting upon received asynchronous {@link Clerk}
 * webhook notifications to ensure database synchronisation.
 */
@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

    private final Webhook svixWebhook;
    private final ObjectMapper mapper;

    @Override
    public void verify(HttpHeaders headers, String payload) {
        try {
            svixWebhook.verify(payload, convertHeaders(headers));
        } catch (Exception ex) {
            throw new WebhookVerificationException();
        }
    }

    @Override
    public ClerkWebhookEvent deserializePayload(String payload) {
        try {
            return mapper.readValue(payload, ClerkWebhookEvent.class);
        } catch (Exception ex) {
            throw new DeserializationException(ex.getMessage());
        }
    }

    private java.net.http.HttpHeaders convertHeaders(HttpHeaders headers) {
        return java.net.http.HttpHeaders.of(headers, (t, v) -> true);
    }

}
