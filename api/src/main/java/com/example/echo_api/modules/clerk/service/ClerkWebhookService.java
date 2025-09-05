package com.example.echo_api.modules.clerk.service;

import org.springframework.http.HttpHeaders;

import com.example.echo_api.exception.ApplicationException;

public interface ClerkWebhookService {

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
     * @throws ApplicationException If the webhook signature verification fails
     */
    void verify(HttpHeaders headers, String payload);

    /**
     * Handle processing of Clerk webhooks, including payload deserialization and
     * event dispatching.
     * 
     * @param headers HTTP headers containing Svix signature metadata
     * @param payload Raw JSON payload string
     * @throws ApplicationException If there was an issue when deserializing the
     *                              JSON payload for whatever reason
     */
    void handleWebhook(String payload);

}
