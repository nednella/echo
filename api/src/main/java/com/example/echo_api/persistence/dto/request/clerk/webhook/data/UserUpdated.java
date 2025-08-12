package com.example.echo_api.persistence.dto.request.clerk.webhook.data;

import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEventData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserUpdated(
    String id,
    String username,
    @JsonProperty("image_url") String imageUrl
) implements ClerkWebhookEventData {}
