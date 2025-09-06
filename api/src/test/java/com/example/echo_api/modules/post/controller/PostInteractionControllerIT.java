package com.example.echo_api.modules.post.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.exception.PostErrorCode;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.testing.support.AbstractIntegrationTest;

/**
 * Integration test class for {@link PostInteractionController}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostInteractionControllerIT extends AbstractIntegrationTest {

    private static final String LIKE_PATH = ApiRoutes.POST.LIKE;

    @Autowired
    private PostRepository postRepository;

    private Post post;

    @BeforeAll
    void setup() {
        post = postRepository.save(Post.create(null, authUser.getId(), "Test post."));
    }

    @BeforeEach
    void cleanDb() {
        cleaner.cleanPostInteractions();
    }

    @Test
    void like_Returns204NoContent_WhenPostByIdExistsAndNotAlreadyLiked() {
        // api: POST /api/v1/post/{id}/like ==> 204 No Content
        UUID postId = post.getId();

        authenticatedClient.post()
            .uri(LIKE_PATH, postId)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
    }

    @Test
    void like_Returns404NotFound_WhenPostByIdDoesNotExist() {
        // api: POST /api/v1/post/{id}/like ==> 404 Not Found : ErrorDTO
        PostErrorCode errorCode = PostErrorCode.ID_NOT_FOUND;
        UUID nonExistingPostId = UUID.randomUUID();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            errorCode.formatMessage(nonExistingPostId),
            null);

        authenticatedClient.post()
            .uri(LIKE_PATH, nonExistingPostId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void like_Returns409Conflict_WhenPostByIdAlreadyLiked() {
        // api: POST /api/v1/post/{id}/like ==> 409 Conflict : ErrorDTO
        PostErrorCode errorCode = PostErrorCode.ALREADY_LIKED;
        UUID postId = post.getId();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.CONFLICT,
            errorCode.formatMessage(postId),
            null);

        // 1st like request --> 204
        authenticatedClient.post()
            .uri(LIKE_PATH, postId)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();

        // 2nd like request --> 409
        authenticatedClient.post()
            .uri(LIKE_PATH, postId)
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void unlike_Returns204NoContent_WhenPostByIdExists() {
        // api: DELETE /api/v1/post/{id}/like ==> 204 No Content
        UUID existingPostId = post.getId();

        authenticatedClient.delete()
            .uri(LIKE_PATH, existingPostId)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
    }

    @Test
    void unlike_Returns204NoContent_WhenPostByIdDoesNotExist() {
        // api: DELETE /api/v1/post/{id}/like ==> 204 No Content
        UUID nonExistingPostId = UUID.randomUUID();

        authenticatedClient.delete()
            .uri(LIKE_PATH, nonExistingPostId)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
    }

}
