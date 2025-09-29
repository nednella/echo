package com.example.echo_api.modules.clerk.dto.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDelete(
    String id,
    boolean deleted
) implements ClerkWebhookData {}
