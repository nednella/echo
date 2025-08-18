package com.example.echo_api.service.clerk.sync;

import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEvent;
import com.example.echo_api.persistence.model.user.User;

public interface ClerkSyncService {

    /**
     * Synchronises a user from Clerk by persisting a local {@link User} that
     * matches the provided unique identifier. Also updates that Clerk user
     * {@code external_id} and {@code public_metadata} for a 2-way sync.
     * 
     * @param clerkId the unique identifier from Clerk
     * @return the persisted {@link User}, or {@code null} if a {@link User} with
     *         the given {@code clerkId} already exists
     */
    public User syncUser(String clerkId);

    /**
     * Process a {@link ClerkWebhookEvent} according to its type, ensuring local
     * references to Clerk users remain synchronised beyond initial onboarding.
     * 
     * @param event the {@link ClerkWebhookEvent} to process
     */
    public void handleWebhookEvent(ClerkWebhookEvent event);

}
