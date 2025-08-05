package com.example.echo_api.persistence.dto.request.webhook.data;

import com.example.echo_api.persistence.dto.request.webhook.WebhookEventData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserCreated(
    String id,
    String username,
    @JsonProperty("image_url") String imageUrl
) implements WebhookEventData {}
