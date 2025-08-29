package com.example.echo_api.service.post;

import java.util.UUID;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.shared.service.SessionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Base service implementation for managing CRUD operations surrounding
 * {@link Post} entities.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class BasePostService {

    protected final SessionService sessionService;
    protected final PostRepository postRepository;

    protected UUID getAuthenticatedUserId() {
        return sessionService.getAuthenticatedUserId();
    }

    /**
     * Protected method for obtaining a {@link Post} via it's {@code id} from
     * {@link PostRepository}.
     * 
     * @param id The id of the post.
     * @return The {@link Post} entity.
     * @throws ResourceNotFoundException If no post by that id exists.
     */
    protected Post getPostEntityById(UUID id) {
        return postRepository.findById(id)
            .orElseThrow(ResourceNotFoundException::new);
    }

}
