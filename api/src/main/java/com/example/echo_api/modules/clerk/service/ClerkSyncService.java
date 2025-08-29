package com.example.echo_api.modules.clerk.service;

import com.example.echo_api.exception.custom.internalserver.ClerkException;
import com.example.echo_api.persistence.dto.request.clerk.webhook.ClerkWebhookEvent;
import com.example.echo_api.persistence.model.user.User;

public interface ClerkSyncService {

    /**
     * Completes the onboarding process for the currently authenticated user.
     * 
     * <p>
     * This method performs a 2-way sync between the local database and Clerk:
     * 
     * <ol>
     * <li>Ensures a corresponding local {@link User} exists by performing a db
     * upsert.
     * <li>Marks the Clerk user as onboarded by appending the local {@link User}
     * UUID to the {@code external_id} field and adding an
     * {@code onboardingComplete} key to the {@code public_metadata} map.
     * </ol>
     * 
     * <p>
     * If the authenticated user is already onboarded (as determined by the token
     * claims), then this operation will return early with {@code null}.
     * 
     * @return the persisted {@link User}, or {@code null} if the authenticated user
     *         is already indicated as having been onboarded
     * @throws ClerkException if there is an issue when communicating with the Clerk
     *                        SDK
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
