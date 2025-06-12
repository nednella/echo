package com.example.echo_api.service.post.interaction;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.conflict.AlreadyLikedException;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.model.post.like.PostLike;
import com.example.echo_api.persistence.repository.PostLikeRepository;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.BasePostService;
import com.example.echo_api.service.session.SessionService;

/**
 * Service implementation for managing CRUD operations between {@link Profile}
 * and {@link Post} entities.
 */
@Service
public class PostInteractionServiceImpl extends BasePostService implements PostInteractionService {

    private final PostLikeRepository likeRepository;

    // @formatter:off
    protected PostInteractionServiceImpl(
        SessionService sessionService,
        PostRepository postRepository,
        PostLikeRepository likeRepository) {
        super(sessionService, postRepository);
        this.likeRepository = likeRepository;
    }
    // @formatter:on

    @Override
    @Transactional
    public void like(UUID id) {
        UUID authenticatedUserId = getAuthenticatedUser().getId();
        Post post = getPostEntityById(id);

        if (likeRepository.existsByPostIdAndAuthorId(post.getId(), authenticatedUserId)) {
            throw new AlreadyLikedException();
        }

        PostLike like = new PostLike(post.getId(), authenticatedUserId);
        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void unlike(UUID id) {
        UUID authenticatedUserId = getAuthenticatedUser().getId();
        likeRepository.deleteByPostIdAndAuthorId(id, authenticatedUserId);
    }

}
