package com.example.echo_api.service.profile.interaction;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.model.follow.Follow;

public interface ProfileInteractionService {

    /**
     * Create a {@link Follow} relationship between the authenticated user and the
     * target profile.
     * 
     * @param username The username of the target profile.
     * @throws ResourceNotFoundException If no profile by that username exists.
     */
    public void follow(String username) throws ResourceNotFoundException;

    /**
     * Delete a {@link Follow} relationship between the authenticated user and the
     * target profile.
     * 
     * @param username The username of the target profile.
     * @throws ResourceNotFoundException If no profile by that username exists.
     */
    public void unfollow(String username) throws ResourceNotFoundException;

    /**
     * Create a {@link Block} relationship between the authenticated user and the
     * target profile.
     * 
     * @param username The username of the target profile.
     * @throws ResourceNotFoundException If no profile by that username exists.
     */
    public void block(String username) throws ResourceNotFoundException;

    /**
     * Delete a {@link Block} relationship between the authenticated user and the
     * target profile.
     * 
     * @param username The username of the target profile.
     * @throws ResourceNotFoundException If no profile by that username exists.
     */
    public void unblock(String username) throws ResourceNotFoundException;

}
