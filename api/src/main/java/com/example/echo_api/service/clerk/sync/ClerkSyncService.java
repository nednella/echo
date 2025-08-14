package com.example.echo_api.service.clerk.sync;

import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEvent;
import com.example.echo_api.persistence.model.user.User;

public interface ClerkSyncService {

    /**
     * Onboard the authenticated user by persisting a local {@link User} that
     * matches the user information included in Clerk-issued JWT, where Clerk acts
     * as the external identity provider (IDP).
     * 
     * @return the persisted {@link User}, or {@code null} if the authenticated
     *         token contains the relevant claims indicating the user was already
     *         onboarded
     */
    public User onboardAuthenticatedUser();

    /**
     * Process a {@link ClerkWebhookEvent} according to its type, ensuring local
     * references to Clerk users remain synchronised beyond initial onboarding.
     * 
     * @param event the {@link ClerkWebhookEvent} to process
     */
    public void handleWebhookEvent(ClerkWebhookEvent event);

}
