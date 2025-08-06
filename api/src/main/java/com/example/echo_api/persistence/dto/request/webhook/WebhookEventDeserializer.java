package com.example.echo_api.persistence.dto.request.webhook;

import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.persistence.dto.request.webhook.data.UserCreated;
import com.example.echo_api.persistence.dto.request.webhook.data.UserDeleted;
import com.example.echo_api.persistence.dto.request.webhook.data.UserUpdated;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Deserializes a JSON webhook payload into a {@link WebhookEvent} object.
 * 
 * <p>
 * The webhook {@code type} field is extracted into a {@link WebhookEventType}
 * enum if the type is supported, and used to extract the {@code data} field
 * into the appropriate {@link WebhookEventData} subclass based on that enum.
 */
public class WebhookEventDeserializer extends JsonDeserializer<WebhookEvent> {

    private static final String DATA_FIELD = "data";
    private static final String TYPE_FIELD = "type";

    @Override
    public WebhookEvent deserialize(JsonParser p, DeserializationContext ctx) throws DeserializationException {
        try {
            JsonNode root = p.readValueAsTree();
            JsonNode typeNode = getChildNode(root, TYPE_FIELD);
            JsonNode dataNode = getChildNode(root, DATA_FIELD);
            WebhookEventType type = WebhookEventType.fromString(typeNode.asText());
            WebhookEventData data = ctx.readTreeAsValue(dataNode, resolveWebhookEventDataType(type));
            return new WebhookEvent(data, type);
        } catch (Exception ex) {
            throw new DeserializationException(ex.getMessage());
        }
    }

    /**
     * Retrieve a child node from the given parent JSON node by field name.
     * 
     * @param parent    The parent (root) node to search.
     * @param fieldName The name of the field to retrieve.
     * @return The child {@link JsonNode} associated to that field.
     * @throws IllegalArgumentException If the specified field is not found in the
     *                                  parent node.
     */
    private JsonNode getChildNode(JsonNode parent, String fieldName) {
        JsonNode childNode = parent.get(fieldName);
        if (childNode == null) {
            throw new IllegalArgumentException("Missing \"" + fieldName + "\" field in JSON payload");
        }
        return childNode;
    }

    /**
     * Resolves the expected {@link WebhookEventData} subclass based on the provided
     * {@link WebhookEventType}.
     * 
     * @param type The {@link WebhookEventType} to map to a data class.
     * @return The {@link Class} of the corresponding {@link WebhookEventData}
     *         subclass.
     * @throws IllegalArgumentException If the event type is not supported.
     */
    private Class<? extends WebhookEventData> resolveWebhookEventDataType(WebhookEventType type)
        throws IllegalArgumentException {
        return switch (type) {
            case USER_CREATED -> UserCreated.class;
            case USER_UPDATED -> UserUpdated.class;
            case USER_DELETED -> UserDeleted.class;
            default -> throw new IllegalArgumentException("Unsupported data type for event type: " + type);
        };
    }

}
