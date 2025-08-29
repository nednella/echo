package com.example.echo_api.modules.clerk.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserUpsert(
    String id,
    String username,
    @JsonProperty("image_url") String imageUrl
) implements ClerkWebhookEventData {}
