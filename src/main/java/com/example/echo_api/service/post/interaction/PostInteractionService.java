package com.example.echo_api.service.post.interaction;

import java.util.UUID;

import com.example.echo_api.exception.custom.conflict.AlreadyLikedException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;

public interface PostInteractionService {

    /**
     * Like a post by {@code id}.
     * 
     * @param id The id of the post to like.
     * @throws ResourceNotFoundException If no post by that id exists.
     * @throws AlreadyLikedException     If the authenticated user has already liked
     *                                   the post with that id.
     */
    public void like(UUID id);

    /**
     * Unlike a post by {@code id}. This operation is idempotent.
     * 
     * @param id The id of the post to unlike.
     */
    public void unlike(UUID id);

    /**
     * Share a post by {@code id}.
     * 
     * @param id The id of the post to share.
     * @throws ResourceNotFoundException If no post by that id exists.
     * @throws AlreadySharedException    If the authenticated user has already
     *                                   shared the post with that id.
     */
    public void share(UUID id);

    /**
     * Unshare a post by {@code id}. This operation is idempotent.
     * 
     * @param id The id of the post to unshare.
     */
    public void unshare(UUID id);

}
