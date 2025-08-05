package com.example.echo_api.persistence.dto.request.webhook;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = WebhookEventDeserializer.class)
public record WebhookEvent(
    WebhookEventData data,
    WebhookEventType type
) {}
