package com.example.echo_api.service.post.interaction;

import java.util.UUID;

public interface PostInteractionService {

    /**
     * Likes a post by {@code id}.
     * 
     * @param id The target post id.
     * @throws PostNotFoundException If no post by that id exists.
     */
    public void like(UUID id);

    /**
     * Unlikes a post by {@code id}.
     * 
     * @param id The target post id.
     */
    public void unlike(UUID id);

}
