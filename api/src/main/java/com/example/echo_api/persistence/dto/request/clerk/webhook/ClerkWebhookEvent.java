package com.example.echo_api.persistence.dto.request.clerk.webhook;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ClerkWebhookEventDeserializer.class)
public record ClerkWebhookEvent(
    ClerkWebhookEventData data,
    ClerkWebhookEventType type
) {}
