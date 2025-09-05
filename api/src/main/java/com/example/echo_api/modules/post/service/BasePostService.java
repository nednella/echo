package com.example.echo_api.modules.post.service;

import java.util.UUID;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.exception.PostErrorCode;
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
     * Fetch the {@link UUID} associated with the authenticated user.
     * 
     * @return the {@link UUID}
     */
    protected UUID getAuthenticatedUserId() {
        return sessionService.getAuthenticatedUserId();
    }

    /**
     * Fetch a post via {@code id} from {@link PostRepository}.
     * 
     * @param id the post id
     * @return the {@link Post} entity
     * @throws ApplicationException if no post with the given id exists
     */
    protected Post getPost(UUID id) {
        return postRepository.findById(id)
            .orElseThrow(() -> PostErrorCode.ID_NOT_FOUND.buildAsException(id));
    }

    /**
     * Throws if a {@link Post} does not exist with the given {@code id}.
     * 
     * @param id
     * @throws ApplicationException if no post with the given id exists
     */
    protected void validatePostExists(UUID id) {
        if (!postRepository.existsById(id)) {
            throw PostErrorCode.ID_NOT_FOUND.buildAsException(id);
        }
    }

}
