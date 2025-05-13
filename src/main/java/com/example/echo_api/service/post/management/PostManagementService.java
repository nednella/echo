package com.example.echo_api.service.post.management;

import java.util.UUID;

import com.example.echo_api.persistence.dto.request.post.CreatePostDTO;

public interface PostManagementService {

    /**
     * Create a {@link Post} for the authenticated user.
     *
     * @param request The request object containing the relevant information to
     *                create the {@link Post}.
     */
    public void create(CreatePostDTO request);

    /**
     * Delete a {@link Post} via {@code id} for the authenticated user.
     *
     * @param id The id of the post to delete.
     * @throws PostNotFoundException      If no post by that id exists.
     * @throws ResourceOwnershipException If the authenticated user does not own the
     *                                    resource in question.
     */
    public void delete(UUID id);

}
