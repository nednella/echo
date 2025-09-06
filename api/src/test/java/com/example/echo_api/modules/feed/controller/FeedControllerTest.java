package com.example.echo_api.modules.feed.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.example.echo_api.modules.feed.service.FeedService;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.modules.post.dto.response.PostEntitiesDTO;
import com.example.echo_api.modules.post.dto.response.PostMetricsDTO;
import com.example.echo_api.modules.post.dto.response.PostRelationshipDTO;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.modules.profile.exception.ProfileErrorCode;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.OffsetLimitRequest;
import com.example.echo_api.shared.pagination.PageDTO;
import com.example.echo_api.shared.pagination.PageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link FeedController}.
 */
@WebMvcTest(FeedController.class)
@AutoConfigureMockMvc(addFilters = false)
class FeedControllerTest {

    private static final String HOMEPAGE_PATH = ApiRoutes.FEED.HOMEPAGE;
    private static final String DISCOVER_PATH = ApiRoutes.FEED.DISCOVER;
    private static final String POSTS_PATH = ApiRoutes.FEED.POSTS;
    private static final String REPLIES_PATH = ApiRoutes.FEED.REPLIES;
    private static final String LIKES_PATH = ApiRoutes.FEED.LIKES;
    private static final String MENTIONS_PATH = ApiRoutes.FEED.MENTIONS;

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private FeedService feedService;

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
    void getHomeFeed_Returns200PageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/homepage ==> 200 OK : PageDTO<PostDTO>
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, HOMEPAGE_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(feedService.getHomeFeed(any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(HOMEPAGE_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(feedService).getHomeFeed(any(Pageable.class));
    }

    @Test
    void getHomeFeed_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/homepage ==> 400 Bad Request : ErrorDTO
        int offset = -1;
        int limit = 20;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Offset index must not be negative",
            null);

        var response = mvc.get()
            .uri(HOMEPAGE_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService, never()).getHomeFeed(any(Pageable.class));
    }

    @Test
    void getHomeFeed_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/homepage ==> 400 Bad Request : ErrorDTO
        int offset = 0;
        int limit = 51;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Limit must be in the range 1 to 50",
            null);

        var response = mvc.get()
            .uri(HOMEPAGE_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService, never()).getHomeFeed(any(Pageable.class));
    }

    @Test
    void getDiscoverFeed_Returns200PageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/discover ==> 200 OK : PageDTO<PostDTO>
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, DISCOVER_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(feedService.getDiscoverFeed(any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(DISCOVER_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(feedService).getDiscoverFeed(any(Pageable.class));
    }

    @Test
    void getDiscoverFeed_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/discover ==> 400 Bad Request : ErrorDTO
        int offset = -1;
        int limit = 20;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Offset index must not be negative",
            null);

        var response = mvc.get()
            .uri(DISCOVER_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService, never()).getDiscoverFeed(any(Pageable.class));
    }

    @Test
    void getDiscoverFeed_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/discover ==> 400 Bad Request : ErrorDTO
        int offset = 0;
        int limit = 51;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Limit must be in the range 1 to 50",
            null);

        var response = mvc.get()
            .uri(DISCOVER_PATH)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService, never()).getDiscoverFeed(any(Pageable.class));
    }

    @Test
    void getProfilePosts_Returns200PageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/posts ==> 200 OK : PageDTO<PostDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, POSTS_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(feedService.getProfilePosts(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(POSTS_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(feedService).getProfilePosts(eq(id), any(Pageable.class));
    }

    @Test
    void getProfilePosts_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/profile/{id}/posts ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Offset index must not be negative",
            null);

        var response = mvc.get()
            .uri(POSTS_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService, never()).getProfilePosts(eq(id), any(Pageable.class));
    }

    @Test
    void getProfilePosts_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/profile/{id}/posts ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 51;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Limit must be in the range 1 to 50",
            null);

        var response = mvc.get()
            .uri(POSTS_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService, never()).getProfilePosts(eq(id), any(Pageable.class));
    }

    @Test
    void getProfilePosts_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/posts ==> 404 Not Found : ErrorDTO
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;

        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(feedService.getProfilePosts(eq(id), any(Pageable.class)))
            .thenThrow(errorCode.buildAsException(id));

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            errorCode.formatMessage(id),
            null);

        var response = mvc.get()
            .uri(POSTS_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService).getProfilePosts(eq(id), any(Pageable.class));
    }

    @Test
    void getProfileReplies_Returns200PageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/replies ==> 200 OK : PageDTO<PostDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, REPLIES_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(feedService.getProfileReplies(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(REPLIES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(feedService).getProfileReplies(eq(id), any(Pageable.class));
    }

    @Test
    void getProfileReplies_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/profile/{id}/replies ==> 400 Bad Request : ErrorDTO
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

        verify(feedService, never()).getProfileReplies(eq(id), any(Pageable.class));
    }

    @Test
    void getProfileReplies_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/profile/{id}/replies ==> 400 Bad Request : ErrorDTO
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

        verify(feedService, never()).getProfileReplies(eq(id), any(Pageable.class));
    }

    @Test
    void getProfileReplies_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/replies ==> 404 Not Found : ErrorDTO
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;

        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(feedService.getProfileReplies(eq(id), any(Pageable.class)))
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

        verify(feedService).getProfileReplies(eq(id), any(Pageable.class));
    }

    @Test
    void getProfileLikes_ReturnPageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/likes ==> 200 OK : PageDTO<PostDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, LIKES_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(feedService.getProfileLikes(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(LIKES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(feedService).getProfileLikes(eq(id), any(Pageable.class));
    }

    @Test
    void getProfileLikes_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/profile/{id}/likes ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Offset index must not be negative",
            null);

        var response = mvc.get()
            .uri(LIKES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService, never()).getProfileLikes(eq(id),
            any(Pageable.class));
    }

    @Test
    void getProfileLikes_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/profile/{id}/likes ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 61;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Limit must be in the range 1 to 50",
            null);

        var response = mvc.get()
            .uri(LIKES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService, never()).getProfileLikes(eq(id),
            any(Pageable.class));
    }

    @Test
    void getProfileLikes_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/likes ==> 404 Not Found : ErrorDTO
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;

        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(feedService.getProfileLikes(eq(id), any(Pageable.class)))
            .thenThrow(errorCode.buildAsException(id));

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            errorCode.formatMessage(id),
            null);

        var response = mvc.get()
            .uri(LIKES_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService).getProfileLikes(eq(id), any(Pageable.class));
    }

    @Test
    void getProfileMentions_ReturnPageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> 200 OK : PageDTO<PostDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, MENTIONS_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(feedService.getProfileMentions(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(MENTIONS_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(feedService).getProfileMentions(eq(id), any(Pageable.class));
    }

    @Test
    void getProfileMentions_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Offset index must not be negative",
            null);

        var response = mvc.get()
            .uri(MENTIONS_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService, never()).getProfileMentions(eq(id),
            any(Pageable.class));
    }

    @Test
    void getProfileMentions_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 51;

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Limit must be in the range 1 to 50",
            null);

        var response = mvc.get()
            .uri(MENTIONS_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService, never()).getProfileMentions(eq(id),
            any(Pageable.class));
    }

    @Test
    void getProfileMentions_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> 404 Not Found : ErrorDTO
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;

        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(feedService.getProfileMentions(eq(id), any(Pageable.class)))
            .thenThrow(errorCode.buildAsException(id));

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            errorCode.formatMessage(id),
            null);

        var response = mvc.get()
            .uri(MENTIONS_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorResponse.class).isEqualTo(expected);

        verify(feedService).getProfileMentions(eq(id), any(Pageable.class));
    }

}
