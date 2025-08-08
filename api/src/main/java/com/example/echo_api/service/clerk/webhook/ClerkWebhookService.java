package com.example.echo_api.service.clerk.webhook;

import org.springframework.http.HttpHeaders;

import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.exception.custom.unauthorised.WebhookVerificationException;

public interface ClerkWebhookService {

    /**
     * Handle e2e processing of Clerk webhooks including verification, payload
     * deserialization and event routing.
     * 
     * @param headers HTTP headers containing Svix signature metadata
     * @param payload Raw JSON payload string
     * @throws WebhookVerificationException If the webhook signature verification
     *                                      fails
     * @throws DeserializationException     If there was an issue when deserializing
     *                                      the JSON payload for whatever reason
     */
    public void handleWebhook(HttpHeaders headers, String payload);

}
