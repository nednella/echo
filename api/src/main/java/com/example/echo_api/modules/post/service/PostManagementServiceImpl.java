package com.example.echo_api.modules.post.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.badrequest.InvalidParentIdException;
import com.example.echo_api.exception.custom.forbidden.ResourceOwnershipException;
import com.example.echo_api.modules.post.dto.request.CreatePostDTO;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.entity.PostEntity;
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
        UUID parentId = request.parentId() != null ? request.parentId() : null;
        UUID authorId = getAuthenticatedUserId();
        String text = request.text();

        validatePostExistsByParentId(parentId);
        Post post = postRepository.save(new Post(parentId, authorId, text));

        List<PostEntity> entities = PostEntityExtractor.extract(post.getId(), text);
        postEntityRepository.saveAll(entities);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        Optional<Post> optPost = postRepository.findById(id);

        if (optPost.isEmpty())
            return; // treat not found as idempotent

        Post post = optPost.get();
        validatePostOwnership(authenticatedUserId, post.getAuthorId());
        postRepository.delete(post);
    }

    /**
     * Validate that a {@link Post} exists by the supplied {@code id}.
     * 
     * @param parentId the id of the post the validate
     * @throws InvalidParentIdException if no post by that id exists
     */
    private void validatePostExistsByParentId(UUID parentId) throws InvalidParentIdException {
        if (parentId == null)
            return;

        if (!postRepository.existsById(parentId)) {
            throw new InvalidParentIdException();
        }
    }

    /**
     * Validate that the authenticated user is allowed to perform the requested
     * action on the given post.
     * 
     * @param authenticatedUserId the authenticated user id
     * @param authorId            the author id of the post in question
     * @throws ResourceOwnershipException if the requesting user and the resource
     *                                    owner do not match
     */
    private void validatePostOwnership(UUID authenticatedUserId, UUID authorId) throws ResourceOwnershipException {
        if (!Objects.equals(authenticatedUserId, authorId)) {
            throw new ResourceOwnershipException();
        }
    }

}
