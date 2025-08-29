package com.example.echo_api.service.clerk.webhook;

import org.springframework.http.HttpHeaders;

import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.exception.custom.unauthorised.WebhookVerificationException;

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
     * @throws WebhookVerificationException If the webhook signature verification
     *                                      fails
     */
    public void verify(HttpHeaders headers, String payload);

    /**
     * Handle processing of Clerk webhooks, including payload deserialization and
     * event dispatching.
     * 
     * @param headers HTTP headers containing Svix signature metadata
     * @param payload Raw JSON payload string
     * @throws DeserializationException If there was an issue when deserializing the
     *                                  JSON payload for whatever reason
     */
    public void handleWebhook(String payload);

}
