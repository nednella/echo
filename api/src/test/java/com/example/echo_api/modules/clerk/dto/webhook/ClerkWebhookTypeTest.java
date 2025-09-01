package com.example.echo_api.modules.clerk.dto.webhook;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit test class for {@link ClerkWebhookType}.
 */
class ClerkWebhookTypeTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "user.updated",
        "user.deleted"
    })
    void fromString_CorrectlyConvertsToEnum(String value) {
        assertDoesNotThrow(() -> ClerkWebhookType.fromString(value));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "user___updated", // wrong format
        "user___deleted", // wrong format
        "subscription.active", // unsupported event type
        "some_random!String_%&^" // self explanatory
    })
    void fromString_ThrowsWhenUnsupportedEventType(String value) {
        assertThrows(IllegalArgumentException.class, () -> ClerkWebhookType.fromString(value));
    }

}
