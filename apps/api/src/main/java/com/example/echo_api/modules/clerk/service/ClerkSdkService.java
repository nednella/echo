package com.example.echo_api.modules.clerk.service;

import java.util.List;

import com.clerk.backend_api.models.components.User;
import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.clerk.dto.ClerkUser;

interface ClerkSdkService {

    /**
     * Retrieves a user from Clerk based on their unique identifier.
     * 
     * @param clerkUserId the unique identifier for the Clerk user
     * @return the retrieved Clerk {@link User} mapped to {@link ClerkUser}
     * @throws IllegalArgumentException if {@code clerkUserId} is null
     * @throws ApplicationException     if the user data retrieval fails for
     *                                  whatever reason
     */
    ClerkUser getUser(String clerkUserId);

    /**
     * Retrieves all users from Clerk.
     * 
     * @return {@link List} of Clerk {@link User} mapped to {@link ClerkUser}
     * @throws ApplicationException if the user data retrieval fails for whatever
     *                              reason
     */
    List<ClerkUser> getAllUsers();

    /**
     * Marks a Clerk user as having completed the onboarding proccess by setting the
     * users {@code external_id} field and adding a boolean indicator to the users
     * {@code public_metadata}.
     * 
     * @param user       the Clerk user (represented by an internal DTO)
     * @param externalId the local application UUID for that Clerk user
     * @throws IllegalArgumentException if {@code user} or {@code externalId} is
     *                                  null
     * @throws ApplicationException     if the update operation fails for whatever
     *                                  reason
     */
    void completeOnboarding(ClerkUser user, String externalId);

    /**
     * Unmarks a user as having completed the onboarding process by removing the
     * users {@code external_id} field and removing the boolean indicator from the
     * users {@code public_metadata}.
     * 
     * @param user the Clerk user (represented by an internal DTO)
     * @throws IllegalArgumentException if {@code user} is null
     * @throws ApplicationException     if the update operation fails for whatever
     *                                  reason
     */
    void revertOnboarding(ClerkUser user);

}
