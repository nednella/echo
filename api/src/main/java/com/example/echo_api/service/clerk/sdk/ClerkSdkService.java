package com.example.echo_api.service.clerk.sdk;

import com.clerk.backend_api.models.components.User;
import com.example.echo_api.exception.custom.internalserver.ClerkException;

public interface ClerkSdkService {

    /**
     * Retrieves a user from Clerk based on their unique identifier
     * 
     * @param clerkUserId The unique identifier for the Clerk user
     * @return The retrieved Clerk {@link User}
     * @throws ClerkException If the user data retrieval fails for whatever reason
     */
    public User getUser(String clerkUserId) throws ClerkException;

    /**
     * Marks a Clerk user as having completed the onboarding proccess by setting the
     * users {@code external_id} field and adding a boolean indicator to the users
     * {@code public_metadata}.
     * 
     * @param clerkUserId The unique identifier for the Clerk user
     * @param externalId  the local application UUID for that Clerk user
     * @throws ClerkException If the update operation fails for whatever reason
     */
    public void completeOnboarding(String clerkUserId, String externalId) throws ClerkException;

}
