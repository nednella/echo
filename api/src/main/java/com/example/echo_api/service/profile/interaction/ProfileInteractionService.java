package com.example.echo_api.service.profile.interaction;

import java.util.UUID;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;

public interface ProfileInteractionService {

    /**
     * Follows a profile by {@code id}.
     * 
     * @param id the id of the profile
     * @throws ResourceNotFoundException if no profile by that id exists
     * @throws SelfActionException       if attempting to follow self
     * @throws AlreadyFollowingException if already following the target profile
     */
    public void follow(UUID id);

    /**
     * Unfollows a profile by {@code id}. This operation is idempotent.
     * 
     * @param id the id of the profile
     */
    public void unfollow(UUID id);

}
