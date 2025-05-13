package com.example.echo_api.service.post;

import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.session.SessionService;

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

    /**
     * Protected method for obtaining the {@link Account} associated to the
     * authenticated user.
     * 
     * @return The {@link Account} entity.
     */
    protected Account getAuthenticatedUser() {
        return sessionService.getAuthenticatedUser();
    }

}
