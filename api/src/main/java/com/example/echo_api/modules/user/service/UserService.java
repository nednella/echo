package com.example.echo_api.modules.user.service;

import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.user.entity.User;

public interface UserService {

    /**
     * Creates or updates a local {@link User} and its associated {@link Profile}
     * basd on data from an external identity provider (IDP).
     * 
     * <p>
     * If no user exists with the given {@code externalId}, a new {@link User} is
     * created along with an associated {@link Profile}, populated with the supplied
     * information. If the user already exists locally, then the associated
     * {@link Profile} is updated with the supplied information.
     * 
     * <p>
     * This method is intended to be used for database synchronisation between the
     * IDP and the local application.
     * 
     * @param externalId the unique identifier from the IDP
     * @param username   the unique username from the IDP
     * @param imageUrl   the profile image URL from the IDP
     * @return the upserted {@link User} instance
     * @throws IllegalArgumentException if {@code externalId} or {@code username} is
     *                                  null
     */
    public User upsertFromExternalSource(String externalId, String username, String imageUrl);

    /**
     * Deletes a local {@link User} and its associated data based on the external
     * identity providers (IDP) unique identifier for that user.
     * 
     * <p>
     * This action is idempotent.
     * 
     * <p>
     * This method is intended to be used for database synchronisation between the
     * IDP and the local application.
     * 
     * @param externalId the unique identifier from the IDP
     * @return the number of affected records (0 or 1)
     */
    public int deleteFromExternalSource(String externalId);

}
