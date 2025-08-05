package com.example.echo_api.service.webhook;

import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.persistence.dto.request.webhook.WebhookEvent;

public interface WebhookService {

    /**
     * Deserializes a JSON payload string into a {@link WebhookEvent} referencing
     * the type of event being sent from Clerk via its {@code type} field.
     * 
     * @param payload The JSON payload string to deserialize.
     * @return The mapped {@link WebhookEvent}.
     * @throws DeserializationException If there was an issue when deserializing the
     *                                  JSON payload for whatever reason.
     */
    public WebhookEvent deserializePayload(String payload);

}
