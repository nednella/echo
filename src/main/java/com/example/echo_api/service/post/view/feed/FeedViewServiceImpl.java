package com.example.echo_api.service.post.view.feed;

import org.springframework.stereotype.Service;

import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.BasePostService;
import com.example.echo_api.service.session.SessionService;

/**
 * Service implementation for managing read-only {@link Post} data presentation
 * operations associated with multiple post objects that form a feed.
 */
@Service
public class FeedViewServiceImpl extends BasePostService implements FeedViewService {

    // @formatter:off
    protected FeedViewServiceImpl(
        SessionService sessionService,
        PostRepository postRepository) {
        super(sessionService, postRepository);
    }
    // @formatter:on

}
