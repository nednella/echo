package com.example.echo_api.modules.post.api;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.shared.dto.ErrorDTO;

/**
 * Integration test class for {@link PostInteractionController}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostInteractionControllerIT extends IntegrationTest {

    private static final String LIKE_PATH = ApiRoutes.POST.LIKE;

    @Autowired
    private PostRepository postRepository;

    private Post post;

    @BeforeAll
    void setup() {
        post = new Post(authUser.getId(), "Test post.");
        post = postRepository.save(post);
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
        UUID nonExistingPostId = UUID.randomUUID();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        authenticatedClient.post()
            .uri(LIKE_PATH, nonExistingPostId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test
    void like_Returns409Conflict_WhenPostByIdAlreadyLiked() {
        // api: POST /api/v1/post/{id}/like ==> 409 Conflict : ErrorDTO
        UUID postId = post.getId();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.CONFLICT,
            ErrorMessageConfig.Conflict.ALREADY_LIKED,
            null,
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
            .expectBody(ErrorDTO.class).isEqualTo(expected);
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
