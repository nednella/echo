package com.example.echo_api.modules.post.service;

import java.util.UUID;

import com.example.echo_api.exception.ApplicationException;

public interface PostInteractionService {

    /**
     * Like a post by {@code id}.
     * 
     * @param id the post id
     * @throws ApplicationException if no post with the given id exists
     * @throws ApplicationException if already liked the post
     */
    void like(UUID id);

    /**
     * Unlike a post by {@code id}.
     * 
     * <p>
     * This operation is idempotent.
     * 
     * @param id the id of the post to unlike
     */
    void unlike(UUID id);

}
