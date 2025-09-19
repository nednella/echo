package com.example.echo_api.modules.post.service;

import java.util.UUID;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.post.dto.request.CreatePostDTO;

public interface PostManagementService {

    /**
     * Create a {@link Post} for the authenticated user.
     *
     * @param request the client request object containing the relevant information
     *                to create a {@link Post}
     * @throws ApplicationException if the parent id is present and no post with the
     *                              given id exists
     */
    void create(CreatePostDTO request);

    /**
     * Delete a {@link Post} via {@code id} for the authenticated user.
     *
     * @param id the post id
     * @throws ApplicationException if the authenticated user is not the author if
     *                              the post given by id if it exists
     */
    void delete(UUID id);

}
