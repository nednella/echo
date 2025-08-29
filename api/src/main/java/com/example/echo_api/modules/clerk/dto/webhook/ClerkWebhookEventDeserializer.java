package com.example.echo_api.modules.clerk.dto.webhook;

import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Deserializes a JSON webhook payload into a {@link ClerkWebhookEvent} object.
 * 
 * <p>
 * The webhook {@code type} field is extracted into a
 * {@link ClerkWebhookEventType} enum if the type is supported, and used to
 * extract the {@code data} field into the appropriate
 * {@link ClerkWebhookEventData} subclass based on that enum.
 */
public class ClerkWebhookEventDeserializer extends JsonDeserializer<ClerkWebhookEvent> {

    private static final String DATA_FIELD = "data";
    private static final String TYPE_FIELD = "type";

    @Override
    public ClerkWebhookEvent deserialize(JsonParser p, DeserializationContext ctx) throws DeserializationException {
        try {
            JsonNode root = p.readValueAsTree();
            JsonNode typeNode = getChildNode(root, TYPE_FIELD);
            JsonNode dataNode = getChildNode(root, DATA_FIELD);
            ClerkWebhookEventType type = ClerkWebhookEventType.fromString(typeNode.asText());
            ClerkWebhookEventData data = ctx.readTreeAsValue(dataNode, resolveWebhookEventDataType(type));
            return new ClerkWebhookEvent(data, type);
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
     * Resolves the expected {@link ClerkWebhookEventData} subclass based on the
     * provided {@link ClerkWebhookEventType}.
     * 
     * @param type The {@link ClerkWebhookEventType} to map to a data class.
     * @return The {@link Class} of the corresponding {@link ClerkWebhookEventData}
     *         subclass.
     * @throws IllegalArgumentException If the event type is not supported.
     */
    private Class<? extends ClerkWebhookEventData> resolveWebhookEventDataType(ClerkWebhookEventType type)
        throws IllegalArgumentException {
        return switch (type) {
            case USER_CREATED, USER_UPDATED -> UserUpsert.class;
            case USER_DELETED -> UserDelete.class;
            default -> throw new IllegalArgumentException("Unsupported data type for event type: " + type);
        };
    }

}
