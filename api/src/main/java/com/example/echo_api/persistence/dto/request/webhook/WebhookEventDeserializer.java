package com.example.echo_api.persistence.dto.request.webhook;

import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.persistence.dto.request.webhook.data.UserCreated;
import com.example.echo_api.persistence.dto.request.webhook.data.UserDeleted;
import com.example.echo_api.persistence.dto.request.webhook.data.UserUpdated;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class WebhookEventDeserializer extends JsonDeserializer<WebhookEvent> {

    private static final String DATA_FIELD = "data";
    private static final String TYPE_FIELD = "type";

    @Override
    public WebhookEvent deserialize(JsonParser p, DeserializationContext ctx) throws DeserializationException {
        try {
            JsonNode node = p.readValueAsTree();
            JsonNode dataNode = node.get(DATA_FIELD);
            JsonNode typeNode = node.get(TYPE_FIELD);
            if (dataNode == null) {
                throw new IllegalArgumentException("Missing \"" + DATA_FIELD + "\" field in JSON payload");
            }
            if (typeNode == null) {
                throw new IllegalArgumentException("Missing \"" + TYPE_FIELD + "\" field in JSON payload");
            }

            WebhookEventType type = ctx.readTreeAsValue(typeNode, WebhookEventType.class);
            WebhookEventData data = ctx.readTreeAsValue(dataNode, resolveWebhookEventDataType(type));
            return new WebhookEvent(data, type);
        } catch (Exception ex) {
            throw new DeserializationException(ex.getMessage());
        }
    }

    private Class<? extends WebhookEventData> resolveWebhookEventDataType(WebhookEventType type)
        throws IllegalArgumentException {
        return switch (type) {
            case USER_CREATED -> UserCreated.class;
            case USER_UPDATED -> UserUpdated.class;
            case USER_DELETED -> UserDeleted.class;
            default -> throw new IllegalArgumentException("Unsupported WebhookEventType: " + type);
        };
    }

}
