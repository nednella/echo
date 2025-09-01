package com.example.echo_api.modules.post.service;

import java.util.UUID;

import com.example.echo_api.exception.custom.badrequest.InvalidParentIdException;
import com.example.echo_api.exception.custom.forbidden.ResourceOwnershipException;
import com.example.echo_api.modules.post.dto.CreatePostDTO;

public interface PostManagementService {

    /**
     * Create a {@link Post} for the authenticated user.
     *
     * @param request the request object containing the relevant information to
     *                create the {@link Post}
     * @throws InvalidParentIdException if a parent id is supplied and no post by
     *                                  that id exists
     */
    public void create(CreatePostDTO request);

    /**
     * Delete a {@link Post} via {@code id} for the authenticated user.
     *
     * @param id the id of the post to delete
     * @throws ResourceOwnershipException if the authenticated user does not own the
     *                                    resource in question
     */
    public void delete(UUID id);

}
