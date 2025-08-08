package com.example.echo_api.service.clerk.webhook;

import org.springframework.http.HttpHeaders;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.exception.custom.unauthorised.WebhookVerificationException;
import com.example.echo_api.persistence.dto.request.webhook.clerk.ClerkWebhookEvent;
import com.example.echo_api.persistence.dto.request.webhook.clerk.ClerkWebhookEventType;

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
     * Deserializes a webhook JSON payload string into an appropriate
     * {@link ClerkWebhookEvent} based on its type information.
     * 
     * @param payload The JSON payload string to deserialize
     * @return The mapped {@link ClerkWebhookEvent}
     * @throws DeserializationException If there was an issue when deserializing the
     *                                  JSON payload for whatever reason
     */
    public ClerkWebhookEvent deserializePayload(String payload);

    /**
     * Check the {@link ClerkWebhookEventType} and pass the event data to the
     * appropriate handler.
     * 
     * @param event The {@link ClerkWebhookEvent} to handle
     */
    public void handleClerkWebhookEvent(ClerkWebhookEvent event);

}
