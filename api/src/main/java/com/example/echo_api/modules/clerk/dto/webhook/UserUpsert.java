package com.example.echo_api.modules.clerk.dto.webhook;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserUpsert(
    String id,
    String username,
    @JsonProperty("external_id") String externalId,
    @JsonProperty("image_url") String imageUrl,
    @JsonProperty("public_metadata") Map<String, Object> publicMetadata
) implements ClerkWebhookData {}
