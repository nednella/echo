package com.example.echo_api.persistence.dto.request.webhook.clerk.data;

import com.example.echo_api.persistence.dto.request.webhook.clerk.ClerkWebhookEventData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserCreated(
    String id,
    String username,
    @JsonProperty("image_url") String imageUrl
) implements ClerkWebhookEventData {}
