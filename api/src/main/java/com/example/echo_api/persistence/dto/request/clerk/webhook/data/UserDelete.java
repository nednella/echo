package com.example.echo_api.persistence.dto.request.clerk.webhook.data;

import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEventData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDelete(
    String id,
    boolean deleted
) implements ClerkWebhookEventData {}
