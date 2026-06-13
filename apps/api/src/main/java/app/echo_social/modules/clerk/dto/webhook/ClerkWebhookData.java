package app.echo_social.modules.clerk.dto.webhook;

public sealed interface ClerkWebhookData permits UserUpsert, UserDelete {}
