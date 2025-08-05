package com.example.echo_api.persistence.dto.request.webhook.data;

import com.example.echo_api.persistence.dto.request.webhook.WebhookEventData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDeleted(
    String id,
    boolean deleted
) implements WebhookEventData {}
