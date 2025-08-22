package com.example.echo_api.unit.persistence.dto.clerk.webhook;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEventType;

class ClerkWebhookEventTypeTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "user.updated",
        "user.deleted"
    })
    void fromString_CorrectlyConvertsToEnum(String value) {
        assertDoesNotThrow(() -> ClerkWebhookEventType.fromString(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "user___updated", // wrong format
        "user___deleted", // wrong format
        "subscription.active", // unsupported event type
        "some_random!String_%&^" // self explanatory
    })
    void fromString_ThrowsWhenUnsupportedEventType(String value) {
        assertThrows(IllegalArgumentException.class, () -> ClerkWebhookEventType.fromString(value));
    }

}
