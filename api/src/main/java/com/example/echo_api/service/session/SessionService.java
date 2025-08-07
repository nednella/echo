package com.example.echo_api.service.session;

import java.util.UUID;

public interface SessionService {

    /**
     * Retrieves the authenticated user Echo API UUID from the authenticated Clerk
     * token.
     * 
     * <p>
     * Note that the {@code external_id} claim will always be present, otherwise any
     * authenticated request would fail at the {@link OnboardingFilter}.
     * 
     * @return The UUID of the authenticated user.
     */
    public UUID getAuthenticatedUserId();

    /**
     * Retrieves the authenticated user Clerk ID from the authenticated Clerk token.
     * 
     * <p>
     * Note that the {@code sub} claim will always be present, as this is default
     * Clerk session token claim that cannot be removed.
     * 
     * @return The Clerk ID of the authenticated user.
     */
    public String getAuthenticatedUserClerkId();

    /**
     * Checks if the authenticated Clerk token contains {@code onboardingComplete}
     * public metadata and returns its value if present.
     * 
     * <p>
     * Note that the {@code metadata} claim will always be present, otherwise any
     * authenticated request would fail at the {@link OnboardingFilter}.
     * 
     * @return A boolean representing the {@code onboardingComplete} public metadata
     */
    public boolean isAuthenticatedUserOnboardingComplete();

}
