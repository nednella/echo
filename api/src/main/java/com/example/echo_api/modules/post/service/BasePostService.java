package com.example.echo_api.modules.post.service;

import java.util.UUID;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.shared.service.SessionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Base service implementation for managing CRUD operations surrounding
 * {@link Post} entities.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
class BasePostService {

    protected final SessionService sessionService;
    protected final PostRepository postRepository;

    /**
     * Protected method for obtaining the {@link UUID} associated to the
     * authenticated user.
     * 
     * @return the {@link UUID}
     */
    protected UUID getAuthenticatedUserId() {
        return sessionService.getAuthenticatedUserId();
    }

    /**
     * Protected method for obtaining a {@link Post} via {@code id} from
     * {@link PostRepository}.
     * 
     * @param id the id of the post
     * @return the {@link Post} entity
     * @throws ResourceNotFoundException if no post by that id exists
     */
    protected Post getPostEntityById(UUID id) {
        return postRepository.findById(id)
            .orElseThrow(ResourceNotFoundException::new);
    }

}
