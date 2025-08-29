package com.example.echo_api.modules.clerk.dto.webhook;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ClerkWebhookEventDeserializer.class)
public record ClerkWebhookEvent(
    ClerkWebhookEventData data,
    ClerkWebhookEventType type
) {}
