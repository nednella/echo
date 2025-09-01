package com.example.echo_api.modules.post.service;

import java.util.UUID;

import com.example.echo_api.exception.custom.conflict.AlreadyLikedException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;

public interface PostInteractionService {

    /**
     * Like a post by {@code id}.
     * 
     * @param id the id of the post to like
     * @throws ResourceNotFoundException if no post by that id exists
     * @throws AlreadyLikedException     if the authenticated user already likes the
     *                                   post with that id
     */
    public void like(UUID id);

    /**
     * Unlike a post by {@code id}. This operation is idempotent.
     * 
     * @param id the id of the post to unlike
     */
    public void unlike(UUID id);

}
