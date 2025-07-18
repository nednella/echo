package com.example.echo_api.service.profile.interaction;

import java.util.UUID;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;

public interface ProfileInteractionService {

    /**
     * Follows a profile by {@code id}.
     * 
     * @param id The id of the profile.
     * @throws ResourceNotFoundException If no profile by that id exists.
     * @throws SelfActionException       If attempting to follow self.
     * @throws BlockedException          if a block exists between the authenticated
     *                                   user and the target profile.
     * @throws AlreadyFollowingException If already following the target profile.
     */
    public void follow(UUID id);

    /**
     * Unfollows a profile by {@code id}.
     * 
     * @param id The id of the profile.
     * @throws ResourceNotFoundException If no profile by that id exists.
     * @throws SelfActionException       If attempting to unfollow self.
     */
    public void unfollow(UUID id);

    /**
     * Blocks a profile by {@code id}.
     * 
     * @param id The id of the profile.
     * @throws ResourceNotFoundException If no profile by that id exists.
     * @throws SelfActionException       If attempting to block self.
     * @throws AlreadyBlockingException  If already following the target profile.
     */
    public void block(UUID id);

    /**
     * Unblocks a profile by {@code id}.
     * 
     * @param id The id of the profile.
     * @throws ResourceNotFoundException If no profile by that id exists.
     * @throws SelfActionException       If attempting to unblock self.
     */
    public void unblock(UUID id);

}
