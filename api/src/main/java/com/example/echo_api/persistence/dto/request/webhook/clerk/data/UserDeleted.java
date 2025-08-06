package com.example.echo_api.persistence.dto.request.webhook.clerk.data;

import com.example.echo_api.persistence.dto.request.webhook.clerk.ClerkWebhookEventData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDeleted(
    String id,
    boolean deleted
) implements ClerkWebhookEventData {}
