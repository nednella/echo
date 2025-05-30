package com.example.echo_api.service.post.management;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.echo_api.exception.custom.forbidden.ResourceOwnershipException;
import com.example.echo_api.persistence.dto.request.post.CreatePostDTO;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.BasePostService;
import com.example.echo_api.service.session.SessionService;

/**
 * Service implementation for managing mutation operations for {@link Post}
 * information related to the authenticated user.
 */
@Service
public class PostManagementServiceImpl extends BasePostService implements PostManagementService {

    // @formatter:off
    protected PostManagementServiceImpl(
        SessionService sessionService,
        PostRepository postRepository) {
        super(sessionService, postRepository);
    }
    // @formatter:on

    @Override
    public void create(CreatePostDTO request) {
        UUID parentId = request.parentId() != null ? request.parentId() : null;
        UUID authorId = getAuthenticatedUser().getId();
        String text = request.text();

        postRepository.save(new Post(parentId, authorId, text));
    }

    @Override
    public void delete(UUID id) {
        UUID authenticatedUserId = getAuthenticatedUser().getId();
        Post post = getPostEntityById(id);

        validatePostOwnership(authenticatedUserId, post.getAuthorId());
        postRepository.delete(post);
    }

    /**
     * Validate that the authenticated user is allowed to perform the requested
     * action on the given post.
     * 
     * @param authenticatedUserId The authenticated user id.
     * @param authorId            The author id of the post in question.
     * @throws ResourceOwnershipException If the requesting user and the resource
     *                                    owner do not match.
     */
    private void validatePostOwnership(UUID authenticatedUserId, UUID authorId) throws ResourceOwnershipException {
        if (!Objects.equals(authenticatedUserId, authorId)) {
            throw new ResourceOwnershipException();
        }
    }

}
