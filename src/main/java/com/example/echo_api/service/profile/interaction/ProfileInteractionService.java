package com.example.echo_api.service.profile.interaction;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;

public interface ProfileInteractionService {

    /**
     * Follows a profile by {@code username}.
     * 
     * @param username The target profile username.
     * @throws ResourceNotFoundException If no profile by that username exists.
     * @throws SelfActionException       If attempting to follow self.
     * @throws BlockedException          if a block exists between the authenticated
     *                                   user and the target profile.
     * @throws AlreadyFollowingException If already following the target profile.
     */
    public void follow(String username);

    /**
     * Unfollows a profile by {@code username}.
     * 
     * @param username The target profile username.
     * @throws ResourceNotFoundException If no profile by that username exists.
     * @throws SelfActionException       If attempting to unfollow self.
     */
    public void unfollow(String username);

    /**
     * Blocks a profile by {@code username}.
     * 
     * @param username The target profile username.
     * @throws ResourceNotFoundException If no profile by that username exists.
     * @throws SelfActionException       If attempting to block self.
     * @throws AlreadyBlockingException  If already following the target profile.
     */
    public void block(String username);

    /**
     * Unblocks a profile by {@code username}.
     * 
     * @param username The target profile username.
     * @throws ResourceNotFoundException If no profile by that username exists.
     * @throws SelfActionException       If attempting to unblock self.
     */
    public void unblock(String username);

}
