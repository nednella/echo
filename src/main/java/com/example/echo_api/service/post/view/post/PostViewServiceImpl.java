package com.example.echo_api.service.post.view.post;

import org.springframework.stereotype.Service;

import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.BasePostService;
import com.example.echo_api.service.session.SessionService;

/**
 * Service implementation for managing read-only {@link Post} data presentation
 * operations associated with singlular post objects.
 */
@Service
public class PostViewServiceImpl extends BasePostService implements PostViewService {

    // @formatter:off
    protected PostViewServiceImpl(
        SessionService sessionService,
        PostRepository postRepository) {
        super(sessionService, postRepository);
    }
    // @formatter:on

}
