package com.example.echo_api.service.user;

import com.example.echo_api.exception.custom.badrequest.ClerkIdAlreadyExistsException;
import com.example.echo_api.persistence.model.user.User;

public interface UserService {

    /**
     * Create a new {@link User} with an associated {@link Profile} as part of the
     * Clerk database synchronisation process.
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

}
