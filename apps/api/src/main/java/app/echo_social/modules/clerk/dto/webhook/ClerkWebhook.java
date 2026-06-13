package app.echo_social.modules.clerk.dto.webhook;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = ClerkWebhookDeserializer.class)
public record ClerkWebhook(
    ClerkWebhookData data,
    ClerkWebhookType type
) {}
