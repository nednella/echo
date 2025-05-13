package com.example.echo_api.service.post.interaction;

import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.BasePostService;
import com.example.echo_api.service.session.SessionService;

public class PostInteractionServiceImpl extends BasePostService implements PostInteractionService {

    // @formatter:off
    protected PostInteractionServiceImpl(
        SessionService sessionService,
        PostRepository postRepository) {
        super(sessionService, postRepository);
    }
    // @formatter:on

}
