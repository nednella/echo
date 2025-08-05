package com.example.echo_api.persistence.dto.request.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum WebhookEventType {
    @JsonProperty("user.created")
    USER_CREATED,
    @JsonProperty("user.updated")
    USER_UPDATED,
    @JsonProperty("user.deleted")
    USER_DELETED;
}
