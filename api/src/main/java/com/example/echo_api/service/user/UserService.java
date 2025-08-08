package com.example.echo_api.service.user;

import com.example.echo_api.exception.custom.badrequest.ClerkIdAlreadyExistsException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.request.webhook.clerk.data.UserDeleted;
import com.example.echo_api.persistence.dto.request.webhook.clerk.data.UserUpdated;
import com.example.echo_api.persistence.model.user.User;

public interface UserService {

    /**
     * Create a new {@link User} with an associated {@link Profile} as part of the
     * Clerk user onboarding process.
     * 
     * @param clerkId  The unique identifier from Clerk
     * @param username The unique username from Clerk
     * @param imageUrl The optional profile image URL from Clerk
     * @return The newly created {@link User} entity
     * @throws ClerkIdAlreadyExistsException If a {@link User} entity with the
     *                                       supplied {@code clerkId} already exists
     *                                       in the local database
     */
    public User createUserWithProfile(String clerkId, String username, String imageUrl);

    /**
     * Handle updates to user data originating from a Clerk webhook for local
     * database synchronisation.
     * 
     * @param data The {@link UserUpdated} event payload containing the relevant
     *             user information to update
     * @throws ResourceNotFoundException If no {@link User} matching the payload
     *                                   Clerk ID exists
     */
    public void handleClerkUserUpdated(UserUpdated data);

    /**
     * Handle user deletions originating from a Clerk webhook for local database
     * synchronisation.
     * 
     * @param data The {@link UserDeleted} event payload containing the Clerk ID of
     *             the deleted user
     */
    public void handleClerkUserDeleted(UserDeleted data);

}
