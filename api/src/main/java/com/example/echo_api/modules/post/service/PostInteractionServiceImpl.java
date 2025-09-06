package com.example.echo_api.modules.post.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.ApplicationException;
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

    private final PostLikeRepository postLikeRepository;

    // @formatter:off
    protected PostInteractionServiceImpl(
        SessionService sessionService,
        PostRepository postRepository,
        PostLikeRepository postLikeRepository) {
        super(sessionService, postRepository);
        this.postLikeRepository = postLikeRepository;
    }
    // @formatter:on

    @Override
    @Transactional
    public void like(UUID id) {
        validatePostExists(id);

        UUID authUserId = getAuthenticatedUserId();
        validateLikeDoesNotExist(id, authUserId);

        postLikeRepository.save(new PostLike(id, authUserId));
    }

    @Override
    @Transactional
    public void unlike(UUID id) {
        UUID authUserId = getAuthenticatedUserId();
        postLikeRepository.deleteByPostIdAndAuthorId(id, authUserId);
    }

    /**
     * Throws if a {@link PostLike} exists with the given {@code postId} and
     * {@code authorId}.
     * 
     * @param postId
     * @param authorId
     * @throws ApplicationException if a like already exists
     */
    private void validateLikeDoesNotExist(UUID postId, UUID authorId) {
        if (postLikeRepository.existsByPostIdAndAuthorId(postId, authorId)) {
            throw PostErrorCode.ALREADY_LIKED.buildAsException(postId);
        }
    }

}
