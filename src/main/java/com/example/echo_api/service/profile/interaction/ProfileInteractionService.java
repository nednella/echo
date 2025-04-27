package com.example.echo_api.service.profile.interaction;

import java.util.UUID;

import com.example.echo_api.exception.custom.account.IdNotFoundException;
import com.example.echo_api.persistence.model.follow.Follow;

public interface ProfileInteractionService {

    /**
     * Create a {@link Follow} relationship between the authenticated user and the
     * target profile.
     * 
     * @param id The id of the target profile.
     * @throws IdNotFoundException If no profile by that id exists.
     */
    public void follow(UUID id) throws IdNotFoundException;

    /**
     * Delete a {@link Follow} relationship between the authenticated user and the
     * target profile.
     * 
     * @param id The id of the target profile.
     * @throws IdNotFoundException If no profile by that id exists.
     */
    public void unfollow(UUID id) throws IdNotFoundException;

    /**
     * Create a {@link Block} relationship between the authenticated user and the
     * target profile.
     * 
     * @param id The id of the target profile.
     * @throws IdNotFoundException If no profile by that id exists.
     */
    public void block(UUID id) throws IdNotFoundException;

    /**
     * Delete a {@link Block} relationship between the authenticated user and the
     * target profile.
     * 
     * @param id The id of the target profile.
     * @throws IdNotFoundException If no profile by that id exists.
     */
    public void unblock(UUID id) throws IdNotFoundException;

}
