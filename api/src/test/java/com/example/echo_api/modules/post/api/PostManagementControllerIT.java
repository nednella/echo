package com.example.echo_api.modules.post.api;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.modules.post.dto.request.CreatePostDTO;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.shared.constants.ApiRoutes;
import com.example.echo_api.shared.dto.ErrorDTO;
import com.example.echo_api.testing.support.AbstractIntegrationTest;

/**
 * Integration test class for {@link PostManagementController}.
 */
class PostManagementControllerIT extends AbstractIntegrationTest {

    private static final String CREATE_PATH = ApiRoutes.POST.CREATE;
    private static final String BY_ID_PATH = ApiRoutes.POST.BY_ID;

    @Autowired
    private PostRepository postRepository;

    private Post selfPost;
    private Post notSelfPost;

    @BeforeEach
    void setup() {
        selfPost = new Post(authUser.getId(), "Test post.");
        selfPost = postRepository.save(selfPost);

        notSelfPost = new Post(mockUser.getId(), "Test post.");
        notSelfPost = postRepository.save(notSelfPost);
    }

    @Test
    void create_Returns204NoContent_WhenPassesValidation() {
        // api: POST /api/v1/post ==> 204 No Content
        UUID parentId = selfPost.getId();
        String text = "Test post.";
        var body = new CreatePostDTO(parentId, text);

        authenticatedClient.post()
            .uri(CREATE_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
    }

    static Stream<Arguments> invalidTextCases() {
        return Stream.of(
            Arguments.of(null, ValidationMessageConfig.TEXT_NULL_OR_BLANK),
            Arguments.of(" ", ValidationMessageConfig.TEXT_NULL_OR_BLANK),
            Arguments.of("x".repeat(281), ValidationMessageConfig.TEXT_TOO_LONG));
    }

    @ParameterizedTest(name = "create_Returns400BadRequest_WhenPostTextFieldIsInvalid: \"{0}\"")
    @MethodSource("invalidTextCases")
    void create_Returns400BadRequest_WhenPostTextFieldIsInvalid(String text, String expectedDetails) {
        // api: POST /api/v1/post ==> 400 Bad Request : ErrorDTO
        var body = new CreatePostDTO(null, text);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            expectedDetails,
            null);

        authenticatedClient.post()
            .uri(CREATE_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test
    void create_Returns400BadRequest_WhenPostByParentIdDoesNotExist() {
        // api: POST /api/v1/post ==> 400 Bad Request : ErrorDTO
        UUID invalidParentId = UUID.randomUUID();
        var body = new CreatePostDTO(invalidParentId, "Test post.");

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_PARENT_ID,
            null);

        authenticatedClient.post()
            .uri(CREATE_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test
    void delete_Returns204NoContent_WhenPostByIdIsOwnedByYou() {
        // api: DELETE /api/v1/post/{id} ==> 204 No Content
        UUID myPostId = selfPost.getId();

        authenticatedClient.delete()
            .uri(BY_ID_PATH, myPostId)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
    }

    @Test
    void delete_Returns204NoContent_WhenPostByIdDoesNotExist() {
        // api: DELETE /api/v1/post/{id} ==> 204 No Content
        UUID nonExistingPostId = UUID.randomUUID();

        authenticatedClient.delete()
            .uri(BY_ID_PATH, nonExistingPostId)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
    }

    @Test
    void delete_Returns403Forbidden_WhenPostByIdExistsAndIsNotOwnedByYou() {
        // api: DELETE /api/v1/post/{id} ==> 403 Forbidden : ErrorDTO
        UUID notMyPostId = notSelfPost.getId();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.FORBIDDEN,
            ErrorMessageConfig.Forbidden.RESOURCE_OWNERSHIP_REQUIRED,
            null,
            null);

        authenticatedClient.delete()
            .uri(BY_ID_PATH, notMyPostId)
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

}