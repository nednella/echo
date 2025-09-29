package com.example.echo_api.modules.clerk.dto.webhook;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Deserializes a JSON webhook payload into a {@link ClerkWebhook} object.
 * 
 * <p>
 * The webhook {@code type} field is extracted into a {@link ClerkWebhookType}
 * enum if the type is supported, and used to extract the {@code data} field
 * into the appropriate {@link ClerkWebhookData} subclass based on the type.
 */
public class ClerkWebhookDeserializer extends JsonDeserializer<ClerkWebhook> {

    private static final String DATA_FIELD = "data";
    private static final String TYPE_FIELD = "type";

    @Override
    public ClerkWebhook deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        JsonNode root = p.readValueAsTree();
        JsonNode typeNode = getChildNode(root, TYPE_FIELD);
        JsonNode dataNode = getChildNode(root, DATA_FIELD);
        ClerkWebhookType type = ClerkWebhookType.fromString(typeNode.asText());
        ClerkWebhookData data = ctx.readTreeAsValue(dataNode, resolveDataType(type));
        return new ClerkWebhook(data, type);
    }

    /**
     * Retrieve a child node from the given parent JSON node by field name.
     * 
     * @param parent    the parent (root) node to search
     * @param fieldName the name of the field to retrieve
     * @return the child {@link JsonNode} associated to that field
     * @throws IllegalArgumentException if the specified field is not found in the
     *                                  parent node
     */
    private JsonNode getChildNode(JsonNode parent, String fieldName) {
        JsonNode childNode = parent.get(fieldName);
        if (childNode == null) {
            throw new IllegalArgumentException("Missing \"" + fieldName + "\" field in JSON payload");
        }
        return childNode;
    }

    /**
     * Resolves the expected {@link ClerkWebhookData} implementation based on the
     * provided {@link ClerkWebhookType}.
     * 
     * @param type the event type to map
     * @return the {@link Class} of the {@link ClerkWebhookData} implementation
     * @throws IllegalArgumentException if the webhook event type is not handled
     */
    private Class<? extends ClerkWebhookData> resolveDataType(ClerkWebhookType type) throws IllegalArgumentException {
        return switch (type) {
            case USER_CREATED, USER_UPDATED -> UserUpsert.class;
            case USER_DELETED -> UserDelete.class;
            default -> throw new IllegalArgumentException("Unhandled webhook event type: " + type.name());
        };
    }

}
