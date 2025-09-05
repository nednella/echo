package com.example.echo_api.modules.post.service;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.post.dto.request.CreatePostDTO;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.exception.PostErrorCode;
import com.example.echo_api.modules.post.repository.PostEntityRepository;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.shared.service.SessionService;
import com.example.echo_api.util.PostEntityExtractor;

/**
 * Service implementation for managing mutation operations for {@link Post}
 * information related to the authenticated user.
 */
@Service
class PostManagementServiceImpl extends BasePostService implements PostManagementService {

    private final PostEntityRepository postEntityRepository;

    // @formatter:off
    protected PostManagementServiceImpl(
        SessionService sessionService,
        PostRepository postRepository,
        PostEntityRepository postEntityRepository) {
        super(sessionService, postRepository);
        this.postEntityRepository = postEntityRepository;
    }
    // @formatter:on

    @Override
    @Transactional
    public void create(CreatePostDTO request) {
        UUID parentId = validateAndReturnParentId(request.parentId());
        UUID authorId = getAuthenticatedUserId();
        String text = request.text();

        Post post = postRepository.save(new Post(parentId, authorId, text));
        postEntityRepository.saveAll(PostEntityExtractor.extract(post.getId(), text));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Optional<Post> optPost = postRepository.findById(id);
        if (optPost.isEmpty()) {
            return; // treat not found as idempotent
        }

        UUID authenticatedUserId = getAuthenticatedUserId();
        Post post = optPost.get();
        validatePostOwnership(authenticatedUserId, post.getAuthorId());
        postRepository.delete(post);
    }

    /**
     * Validate that a {@link Post} exists by the given {@code parentId}.
     * 
     * @param parentId
     * @throws ApplicationException if no post by that id exists
     */
    private UUID validateAndReturnParentId(UUID parentId) {
        if (parentId == null) {
            return parentId;
        }

        validatePostExists(parentId);
        return parentId;
    }

    /**
     * Throws if {@code authenticatedUserId} and {@code authorId} are equal.
     * 
     * @param authenticatedUserId
     * @param authorId
     * @throws ApplicationException if the ids match
     */
    private void validatePostOwnership(UUID authenticatedUserId, UUID authorId) {
        if (!Objects.equals(authenticatedUserId, authorId)) {
            throw PostErrorCode.POST_NOT_OWNED.buildAsException();
        }
    }

}
