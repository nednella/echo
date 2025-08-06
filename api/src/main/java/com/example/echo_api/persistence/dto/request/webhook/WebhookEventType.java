package com.example.echo_api.persistence.dto.request.webhook;

public enum WebhookEventType {

    USER_CREATED,
    USER_UPDATED,
    USER_DELETED;

    /**
     * Convert a string to a {@link WebhookEventType} enum value.
     * 
     * @param str The string to convert.
     * @return The matching {@link WebhookEventType} enum value.
     * @throws IllegalArgumentException If the input is null or does not match any
     *                                  supported event type.
     */
    public static WebhookEventType fromString(String str) throws IllegalArgumentException {
        try {
            return WebhookEventType.valueOf(normaliseString(str));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported event type: " + str);
        }
    }

    /**
     * Normalise a string from lower.dot.notation to UPPER_SNAKE_CASE to match enum
     * naming conventions, e.g., {@code user.created} -> {@code USER_CREATED}
     * 
     * @param str The string to normalise.
     * @return The normalised string.
     */
    private static String normaliseString(String str) {
        if (str == null) {
            return null;
        }
        return str.replace(".", "_").toUpperCase();
    }

}
