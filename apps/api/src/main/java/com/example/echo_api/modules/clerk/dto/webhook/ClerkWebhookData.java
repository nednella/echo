package com.example.echo_api.modules.clerk.dto.webhook;

public sealed interface ClerkWebhookData permits UserUpsert, UserDelete {}
