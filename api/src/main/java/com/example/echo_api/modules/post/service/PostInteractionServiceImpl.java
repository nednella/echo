package com.example.echo_api.modules.post.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.entity.PostLike;
import com.example.echo_api.modules.post.exception.PostErrorCode;
import com.example.echo_api.modules.post.repository.PostLikeRepository;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.shared.service.SessionService;

/**
 * Service implementation for managing CRUD operations between {@link Profile}
 * and {@link Post} entities.
 */
@Service
class PostInteractionServiceImpl extends BasePostService implements PostInteractionService {

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
        UUID authenticatedUserId = getAuthenticatedUserId();
        Post post = getPostEntityById(id); // validate existence of id

        if (likeRepository.existsByPostIdAndAuthorId(post.getId(), authenticatedUserId)) {
            throw PostErrorCode.ALREADY_LIKED.buildAsException(post.getId());
        }

        PostLike like = new PostLike(post.getId(), authenticatedUserId);
        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void unlike(UUID id) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        likeRepository.deleteByPostIdAndAuthorId(id, authenticatedUserId);
    }

}
