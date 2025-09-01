package com.example.echo_api.modules.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.conflict.AlreadyLikedException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.modules.post.service.PostInteractionService;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.dto.ErrorDTO;

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
        UUID id = UUID.randomUUID();

        doThrow(new ResourceNotFoundException()).when(postInteractionService).like(id);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null);

        var response = mvc.post()
            .uri(LIKE_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postInteractionService).like(id);
    }

    @Test
    void like_Returns409Conflict_WhenPostByIdAlreadyLiked() {
        // api: POST /api/v1/post/{id}/like ==> 409 Conflict : ErrorDTO
        UUID id = UUID.randomUUID();

        doThrow(new AlreadyLikedException()).when(postInteractionService).like(id);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.CONFLICT,
            ErrorMessageConfig.Conflict.ALREADY_LIKED,
            null);

        var response = mvc.post()
            .uri(LIKE_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(409)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

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
