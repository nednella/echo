package com.example.echo_api.modules.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.post.exception.PostErrorCode;
import com.example.echo_api.modules.post.service.PostInteractionService;
import com.example.echo_api.shared.constant.ApiRoutes;

/**
 * Unit test class for {@link PostInteractionController}.
 */
@WebMvcTest(PostInteractionController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostInteractionControllerTest {

    private static final String LIKE_PATH = ApiRoutes.POST.LIKE;

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private PostInteractionService postInteractionService;

    @Test
    void like_Returns204NoContent_WhenPostByIdExistsAndNotAlreadyLiked() {
        // api: POST /api/v1/post/{id}/like ==> 204 No Content
        UUID id = UUID.randomUUID();

        var response = mvc.post()
            .uri(LIKE_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(204)
            .body().isEmpty();

        verify(postInteractionService).like(id);
    }

    @Test
    void like_Returns404NotFound_WhenPostByIdDoesNotExist() {
        // api: POST /api/v1/post/{id}/like ==> 404 Not Found : ErrorDTO
        PostErrorCode errorCode = PostErrorCode.ID_NOT_FOUND;

        UUID id = UUID.randomUUID();

        doThrow(errorCode.buildAsException(id)).when(postInteractionService).like(id);

        ErrorResponse expected = new ErrorResponse(
            errorCode.getStatus(),
            errorCode.formatMessage(id),
            null);

        var response = mvc.post()
            .uri(LIKE_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(postInteractionService).like(id);
    }

    @Test
    void like_Returns409Conflict_WhenPostByIdAlreadyLiked() {
        // api: POST /api/v1/post/{id}/like ==> 409 Conflict : ErrorDTO
        PostErrorCode errorCode = PostErrorCode.ALREADY_LIKED;

        UUID id = UUID.randomUUID();

        doThrow(errorCode.buildAsException(id)).when(postInteractionService).like(id);

        ErrorResponse expected = new ErrorResponse(
            errorCode.getStatus(),
            errorCode.formatMessage(id),
            null);

        var response = mvc.post()
            .uri(LIKE_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(409)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(postInteractionService).like(id);
    }

    @Test
    void unlike_Returns204NoContent_IsIdempotent() {
        // api: DELETE /api/v1/post/{id}/like ==> 204 No Content
        UUID id = UUID.randomUUID();

        var response = mvc.delete()
            .uri(LIKE_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(204)
            .body().isEmpty();

        verify(postInteractionService).unlike(id);
    }

}
