package com.example.echo_api.modules.post.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.modules.post.dto.response.PostEntitiesDTO;
import com.example.echo_api.modules.post.dto.response.PostMetricsDTO;
import com.example.echo_api.modules.post.dto.response.PostRelationshipDTO;
import com.example.echo_api.modules.post.exception.PostErrorCode;
import com.example.echo_api.modules.post.service.PostViewService;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.OffsetLimitRequest;
import com.example.echo_api.shared.pagination.PageDTO;
import com.example.echo_api.shared.pagination.PageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link PostViewController}.
 */
@WebMvcTest(PostViewController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostViewControllerTest {

    private static final String BY_ID_PATH = ApiRoutes.POST.BY_ID;
    private static final String REPLIES_PATH = ApiRoutes.POST.REPLIES;

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private PostViewService postViewService;

    @Autowired
    private ObjectMapper objectMapper;

    private static PostDTO post;

    @BeforeAll
    static void setup() {
        post = new PostDTO(
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            new SimplifiedProfileDTO(UUID.randomUUID().toString(), "username", "name", null, null),
            "Example post.",
            Instant.now().toString(),
            new PostMetricsDTO(0, 0),
            new PostRelationshipDTO(false),
            new PostEntitiesDTO(List.of(), List.of(), List.of()));
    }

    @Test
    void getPostById_Returns200PostDto_WhenPostByIdExists() {
        // api: GET /api/v1/post/{id} ==> 200 OK : PostDTO
        UUID id = UUID.randomUUID();

        when(postViewService.getPostById(id)).thenReturn(post);

        var response = mvc.get()
            .uri(BY_ID_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().convertTo(PostDTO.class).isEqualTo(post);

        verify(postViewService).getPostById(id);
    }

    @Test
    void getPostById_Returns404NotFound_WhenPostByIdDoesNotExist() {
        // api: GET /api/v1/post/{id} ==> 404 Not Found : ErrorDTO
        PostErrorCode errorCode = PostErrorCode.ID_NOT_FOUND;

        UUID id = UUID.randomUUID();
        when(postViewService.getPostById(id)).thenThrow(errorCode.buildAsException(id));

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            errorCode.formatMessage(id),
            null);

        var response = mvc.get()
            .uri(BY_ID_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(postViewService).getPostById(id);
    }

    @Test
    void getRepliesByPostId_Returns200PageDtoOfPostDto_WhenPostByIdExists() throws Exception {
        // api: GET /api/v1/post/{id}/replies ==> 200 OK : PageDTO<PostDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> replies = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(replies, REPLIES_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(postViewService.getRepliesByPostId(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(REPLIES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(postViewService).getRepliesByPostId(eq(id), any(Pageable.class));
    }

    @Test
    void getRepliesByPostId_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/post/{id}/replies ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Offset index must not be negative",
            null);

        var response = mvc.get()
            .uri(REPLIES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(postViewService, never()).getRepliesByPostId(eq(id), any(Pageable.class));
    }

    @Test
    void getRepliesByPostId_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/post/{id}/replies ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 51;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Limit must be in the range 1 to 50",
            null);

        var response = mvc.get()
            .uri(REPLIES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(postViewService, never()).getRepliesByPostId(eq(id), any(Pageable.class));
    }

    @Test
    void getRepliesByPostId_Returns404NotFound_WhenPostByIdDoesNotExist() {
        // api: GET /api/v1/post/{id}/replies ==> 404 Not Found : ErrorDTO
        PostErrorCode errorCode = PostErrorCode.ID_NOT_FOUND;

        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(postViewService.getRepliesByPostId(eq(id), any(Pageable.class)))
            .thenThrow(errorCode.buildAsException(id));

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            errorCode.formatMessage(id),
            null);

        var response = mvc.get()
            .uri(REPLIES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(postViewService).getRepliesByPostId(eq(id), any(Pageable.class));
    }

}
