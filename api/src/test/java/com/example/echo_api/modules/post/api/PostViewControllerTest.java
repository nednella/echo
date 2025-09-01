package com.example.echo_api.modules.post.api;

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

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.modules.profile.dto.SimplifiedProfileDTO;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.persistence.dto.response.post.PostEntitiesDTO;
import com.example.echo_api.persistence.dto.response.post.PostMetricsDTO;
import com.example.echo_api.persistence.dto.response.post.PostRelationshipDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.service.post.view.PostViewService;
import com.example.echo_api.util.OffsetLimitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link PostViewController}.
 */
@WebMvcTest(PostViewController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostViewControllerTest {

    private static final String BY_ID_PATH = ApiRoutes.POST.BY_ID;
    private static final String REPLIES_PATH = ApiRoutes.POST.REPLIES;
    private static final String HOMEPAGE_FEED_PATH = ApiRoutes.FEED.HOMEPAGE;
    private static final String DISCOVER_FEED_PATH = ApiRoutes.FEED.DISCOVER;
    private static final String POSTS_FEED_PATH = ApiRoutes.FEED.POSTS;
    private static final String REPLIES_FEED_PATH = ApiRoutes.FEED.REPLIES;
    private static final String LIKES_FEED_PATH = ApiRoutes.FEED.LIKES;
    private static final String MENTIONS_FEED_PATH = ApiRoutes.FEED.MENTIONS;

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
        UUID id = UUID.randomUUID();

        when(postViewService.getPostById(id)).thenThrow(new ResourceNotFoundException());

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        var response = mvc.get()
            .uri(BY_ID_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService).getPostById(id);
    }

    @Test
    void getRepliesById_Returns200PageDtoOfPostDto_WhenPostByIdExists() throws Exception {
        // api: GET /api/v1/post/{id}/replies ==> 200 OK : PageDTO<PostDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> replies = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(replies, REPLIES_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(postViewService.getRepliesById(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(REPLIES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(postViewService).getRepliesById(eq(id), any(Pageable.class));
    }

    @Test
    void getRepliesById_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/post/{id}/replies ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            null);

        var response = mvc.get()
            .uri(REPLIES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getRepliesById(eq(id), any(Pageable.class));
    }

    @Test
    void getRepliesById_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/post/{id}/replies ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 51;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            null);

        var response = mvc.get()
            .uri(REPLIES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getRepliesById(eq(id), any(Pageable.class));
    }

    @Test
    void getRepliesById_Returns404NotFound_WhenPostByIdDoesNotExist() {
        // api: GET /api/v1/post/{id}/replies ==> 404 Not Found : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(postViewService.getRepliesById(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        var response = mvc.get()
            .uri(REPLIES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService).getRepliesById(eq(id), any(Pageable.class));
    }

    @Test
    void getHomepagePosts_Returns200PageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/homepage ==> 200 OK : PageDTO<PostDTO>
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, HOMEPAGE_FEED_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(postViewService.getHomepagePosts(any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(HOMEPAGE_FEED_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(postViewService).getHomepagePosts(any(Pageable.class));
    }

    @Test
    void getHomepagePosts_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/homepage ==> 400 Bad Request : ErrorDTO
        int offset = -1;
        int limit = 20;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            null);

        var response = mvc.get()
            .uri(HOMEPAGE_FEED_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getHomepagePosts(any(Pageable.class));
    }

    @Test
    void getHomepagePosts_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/homepage ==> 400 Bad Request : ErrorDTO
        int offset = 0;
        int limit = 51;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            null);

        var response = mvc.get()
            .uri(HOMEPAGE_FEED_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getHomepagePosts(any(Pageable.class));
    }

    @Test
    void getDiscoverPosts_Returns200PageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/discover ==> 200 OK : PageDTO<PostDTO>
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, DISCOVER_FEED_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(postViewService.getDiscoverPosts(any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(DISCOVER_FEED_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(postViewService).getDiscoverPosts(any(Pageable.class));
    }

    @Test
    void getDiscoverPosts_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/discover ==> 400 Bad Request : ErrorDTO
        int offset = -1;
        int limit = 20;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            null);

        var response = mvc.get()
            .uri(DISCOVER_FEED_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getDiscoverPosts(any(Pageable.class));
    }

    @Test
    void getDiscoverPosts_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/discover ==> 400 Bad Request : ErrorDTO
        int offset = 0;
        int limit = 51;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            null);

        var response = mvc.get()
            .uri(DISCOVER_FEED_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getDiscoverPosts(any(Pageable.class));
    }

    @Test
    void getPostsByProfileId_Returns200PageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/posts ==> 200 OK : PageDTO<PostDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, POSTS_FEED_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(postViewService.getPostsByAuthorId(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(POSTS_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(postViewService).getPostsByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void getPostsByProfileId_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/profile/{id}/posts ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            null);

        var response = mvc.get()
            .uri(POSTS_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getPostsByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void getPostsByProfileId_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/profile/{id}/posts ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 51;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            null);

        var response = mvc.get()
            .uri(POSTS_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getPostsByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void getPostsByProfileId_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/posts ==> 404 Not Found : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(postViewService.getPostsByAuthorId(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        var response = mvc.get()
            .uri(POSTS_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService).getPostsByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void getRepliesByProfileId_Returns200PageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/replies ==> 200 OK : PageDTO<PostDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, REPLIES_FEED_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(postViewService.getRepliesByAuthorId(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(REPLIES_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(postViewService).getRepliesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void getRepliesByProfileId_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/profile/{id}/replies ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            null);

        var response = mvc.get()
            .uri(REPLIES_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getRepliesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void getRepliesByProfileId_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/profile/{id}/replies ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 51;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            null);

        var response = mvc.get()
            .uri(REPLIES_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getRepliesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void getRepliesByProfileId_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/replies ==> 404 Not Found : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(postViewService.getRepliesByAuthorId(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        var response = mvc.get()
            .uri(REPLIES_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService).getRepliesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void getLikesByProfileId_ReturnPageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/likes ==> 200 OK : PageDTO<PostDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, LIKES_FEED_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(postViewService.getLikesByAuthorId(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(LIKES_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(postViewService).getLikesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void getLikesByProfileId_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/profile/{id}/likes ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            null);

        var response = mvc.get()
            .uri(LIKES_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getLikesByAuthorId(eq(id),
            any(Pageable.class));
    }

    @Test
    void getLikesByProfileId_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/profile/{id}/likes ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 61;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            null);

        var response = mvc.get()
            .uri(LIKES_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getLikesByAuthorId(eq(id),
            any(Pageable.class));
    }

    @Test
    void getLikesByProfileId_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/likes ==> 404 Not Found : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(postViewService.getLikesByAuthorId(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        var response = mvc.get()
            .uri(LIKES_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService).getLikesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void getMentionsOfProfileId_ReturnPageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> 200 OK : PageDTO<PostDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, MENTIONS_FEED_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(postViewService.getMentionsOfAuthorId(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(MENTIONS_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(postViewService).getMentionsOfAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void getMentionsOfProfileId_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            null);

        var response = mvc.get()
            .uri(MENTIONS_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getMentionsOfAuthorId(eq(id),
            any(Pageable.class));
    }

    @Test
    void getMentionsOfProfileId_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 51;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            null);

        var response = mvc.get()
            .uri(MENTIONS_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService, never()).getMentionsOfAuthorId(eq(id),
            any(Pageable.class));
    }

    @Test
    void getMentionsOfProfileId_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> 404 Not Found : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(postViewService.getMentionsOfAuthorId(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        var response = mvc.get()
            .uri(MENTIONS_FEED_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postViewService).getMentionsOfAuthorId(eq(id), any(Pageable.class));
    }

}
