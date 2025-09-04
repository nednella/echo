package com.example.echo_api.modules.clerk.dto.webhook;

public enum ClerkWebhookType {

    USER_CREATED, USER_UPDATED, USER_DELETED;

    /**
     * Convert a string to a {@link ClerkWebhookType} enum value.
     * 
     * @param str the string to convert
     * @return the matching {@link ClerkWebhookType} enum value
     * @throws IllegalArgumentException if the input is null or does not match any
     *                                  supported event type
     */
    public static ClerkWebhookType fromString(String str) throws IllegalArgumentException {
        try {
            return ClerkWebhookType.valueOf(normaliseString(str));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported webhook event type: " + str);
        }
    }

    /**
     * Normalise a string from lower.dot.notation to UPPER_SNAKE_CASE to match enum
     * naming conventions, e.g., {@code user.created} -> {@code USER_CREATED}
     * 
     * @param str the string to normalise
     * @return the normalised string
     */
    private static String normaliseString(String str) {
        return (str == null) ? null : str.replace(".", "_").toUpperCase();
    }

}
