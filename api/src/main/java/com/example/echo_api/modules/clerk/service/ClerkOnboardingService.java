package com.example.echo_api.modules.clerk.service;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.user.entity.User;

public interface ClerkOnboardingService {

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
     * @throws ApplicationException if there is an issue when communicating with the
     *                              Clerk SDK
     */
    User onboardAuthenticatedUser();

}
