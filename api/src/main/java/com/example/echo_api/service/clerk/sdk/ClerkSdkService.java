package com.example.echo_api.service.clerk.sdk;

import com.clerk.backend_api.models.components.User;
import com.example.echo_api.exception.custom.internalserver.ClerkException;

public interface ClerkSdkService {

    /**
     * Retrieves a user from Clerk based on their Clerk user ID.
     * 
     * @param clerkUserId The unique identifier for the Clerk user.
     * @return The retrieved Clerk User object.
     * @throws ClerkException If the user data retrieval fails for whatever reason.
     */
    public User getUser(String clerkUserId) throws ClerkException;

    /**
     * Updates the external ID for a Clerk user.
     * 
     * @param clerkUserId The unique identifier for the Clerk user.
     * @param externalId  The external ID to set.
     * @throws ClerkException If the update operation fails for whatever reason.
     */
    public void setExternalId(String clerkUserId, String externalId) throws ClerkException;

    /**
     * Marks a Clerk user as having completed the onboarding proccess by updating
     * their public metadata.
     * 
     * @param clerkUserId The unique identifier for the Clerk user.
     * @throws ClerkException If the update operation fails for whatever reason.
     */
    public void completeOnboarding(String clerkUserId) throws ClerkException;

}
