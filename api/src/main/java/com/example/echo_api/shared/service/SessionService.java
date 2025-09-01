package com.example.echo_api.shared.service;

import java.util.UUID;

import com.example.echo_api.security.ClerkOnboardingFilter;

public interface SessionService {

    /**
     * Retrieve the authenticated user Echo API UUID from the authenticated Clerk
     * token.
     * 
     * <p>
     * Note that the {@code echo_id} claim will always be present, otherwise any
     * authenticated request would fail at the {@link ClerkOnboardingFilter}.
     * 
     * @return The UUID of the authenticated user
     */
    public UUID getAuthenticatedUserId();

    /**
     * Retrieve the authenticated user Clerk ID from the authenticated Clerk token.
     * 
     * <p>
     * Note that the {@code sub} claim will always be present, as this is default
     * Clerk session token claim that cannot be removed.
     * 
     * @return The Clerk ID of the authenticated user
     */
    public String getAuthenticatedUserClerkId();

    /**
     * Retrieve the authenticated user onboarding status from the authenticated
     * Clerk token.
     * 
     * <p>
     * Note that the {@code onboarded} claim will always be present, otherwise any
     * authenticated request would fail at the {@link ClerkOnboardingFilter}.
     * 
     * @return A boolean representing the {@code onboarded} claim
     */
    public boolean isAuthenticatedUserOnboarded();

}
