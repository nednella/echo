package com.example.echo_api.unit.persistence.dto.clerk.webhook;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.echo_api.exception.custom.badrequest.DeserializationException;
import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEvent;
import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEventType;
import com.example.echo_api.persistence.dto.request.clerk.webhook.data.UserDelete;
import com.example.echo_api.persistence.dto.request.clerk.webhook.data.UserUpsert;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link ClerkWebhookDeserializer}.
 */
class ClerkWebhookEventDeserializerTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    void correctlyDeserializesUserCreatedEventDataIntoUserUpsert() throws Exception {
        String payload = """
                {
                  "data": {
                    "id": "user_29w83sxmDNGwOuEthce5gg56FcC",
                    "image_url": "https://img.clerk.com/xxxxxx",
                    "username": "test_user"
                  },
                  "type": "user.created"
                }
            """;

        ClerkWebhookEvent e = mapper.readValue(payload, ClerkWebhookEvent.class);
        assertEquals(ClerkWebhookEventType.USER_CREATED, e.type());

        UserUpsert data = (UserUpsert) e.data();
        assertEquals("user_29w83sxmDNGwOuEthce5gg56FcC", data.id());
        assertEquals("https://img.clerk.com/xxxxxx", data.imageUrl());
        assertEquals("test_user", data.username());
    }

    @Test
    void correctlyDeserializesUserUpdatedEventDataIntoUserUpsert() throws Exception {
        String payload = """
                {
                  "data": {
                    "id": "user_29w83sxmDNGwOuEthce5gg56FcC",
                    "image_url": "https://img.clerk.com/xxxxxx",
                    "username": "test_user"
                  },
                  "type": "user.updated"
                }
            """;

        ClerkWebhookEvent e = mapper.readValue(payload, ClerkWebhookEvent.class);
        assertEquals(ClerkWebhookEventType.USER_UPDATED, e.type());

        UserUpsert data = (UserUpsert) e.data();
        assertEquals("user_29w83sxmDNGwOuEthce5gg56FcC", data.id());
        assertEquals("https://img.clerk.com/xxxxxx", data.imageUrl());
        assertEquals("test_user", data.username());
    }

    @Test
    void correctlyDeserializesUserDeletedEventDataIntoUserDelete() throws Exception {
        String payload = """
                {
                  "data": {
                    "deleted": true,
                    "id": "user_29w83sxmDNGwOuEthce5gg56FcC"
                  },
                  "type": "user.deleted"
                }
            """;

        ClerkWebhookEvent e = mapper.readValue(payload, ClerkWebhookEvent.class);
        assertEquals(ClerkWebhookEventType.USER_DELETED, e.type());

        UserDelete data = (UserDelete) e.data();
        assertEquals(true, data.deleted());
        assertEquals("user_29w83sxmDNGwOuEthce5gg56FcC", data.id());
    }

    @Test
    void throwsWhenUnsupportedEventType() {
        String payload = """
                {
                  "data": {
                    "id": "user_29w83sxmDNGwOuEthce5gg56FcC"
                  },
                  "type": "subscription.active"
                }
            """;

        Exception ex = assertThrows(DeserializationException.class,
            () -> mapper.readValue(payload, ClerkWebhookEvent.class));

        assertEquals("Unsupported event type: subscription.active", ex.getMessage());
    }

    @Test
    void throwsWhenMissingTypeField() {
        String payload = """
                {
                  "data": {
                    "id": "user_29w83sxmDNGwOuEthce5gg56FcC"
                  }
                }
            """;

        Exception ex = assertThrows(DeserializationException.class,
            () -> mapper.readValue(payload, ClerkWebhookEvent.class));

        assertEquals("Missing \"type\" field in JSON payload", ex.getMessage());
    }

    @Test
    void throwsWhenMissingDataField() {
        String payload = """
                {
                  "type": "user.updated"
                }
            """;

        Exception ex = assertThrows(DeserializationException.class,
            () -> mapper.readValue(payload, ClerkWebhookEvent.class));

        assertEquals("Missing \"data\" field in JSON payload", ex.getMessage());
    }

}
