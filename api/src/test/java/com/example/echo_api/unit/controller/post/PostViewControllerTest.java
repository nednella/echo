package com.example.echo_api.unit.controller.post;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.test.web.servlet.MockMvc;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.controller.post.PostViewController;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.persistence.dto.response.post.PostEntitiesDTO;
import com.example.echo_api.persistence.dto.response.post.PostMetricsDTO;
import com.example.echo_api.persistence.dto.response.post.PostRelationshipDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.service.post.view.PostViewService;
import com.example.echo_api.util.pagination.OffsetLimitRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link PostViewController}.
 */
@WebMvcTest(PostViewController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    void PostViewController_GetPostById_ReturnPostDto() throws Exception {
        // api: GET /api/v1/post/{id} ==> : 200 : PostDTO
        String path = ApiConfig.Post.GET_BY_ID;
        UUID id = UUID.randomUUID();

        when(postViewService.getPostById(id)).thenReturn(post);

        String response = mockMvc
            .perform(get(path, id))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PostDTO expected = post;
        PostDTO actual = objectMapper.readValue(response, PostDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getPostById(id);
    }

    @Test
    void PostViewController_GetPostById_Throw404ResourceNotFound() throws Exception {
        // api: GET /api/v1/post/{id} ==> : 404 : ResourceNotFound
        String path = ApiConfig.Post.GET_BY_ID;
        UUID id = UUID.randomUUID();

        when(postViewService.getPostById(id)).thenThrow(new ResourceNotFoundException());

        String response = mockMvc
            .perform(get(path, id))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getPostById(id);
    }

    @Test
    void PostViewController_GetRepliesById_ReturnPageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/post/{id}/replies ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Post.GET_REPLIES_BY_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<PostDTO> replies = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(replies, path);

        when(postViewService.getRepliesById(eq(id), any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<PostDTO> actual = objectMapper.readValue(response, new TypeReference<PageDTO<PostDTO>>() {});

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getRepliesById(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetRepliesById_Throw400InvalidRequest_InvalidOffset() throws Exception {
        // api: GET /api/v1/post/{id}/replies ==> : 400 : InvalidRequest
        String path = ApiConfig.Post.GET_REPLIES_BY_ID;
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getRepliesById(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetRepliesById_Throw400InvalidRequest_InvalidLimit() throws Exception {
        // api: GET /api/v1/post/{id}/replies ==> : 400 : InvalidRequest
        String path = ApiConfig.Post.GET_REPLIES_BY_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 51;

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getRepliesById(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetRepliesById_Throw404ResourceNotFound() throws Exception {
        // api: GET /api/v1/post/{id}/replies ==> : 404 : ResourceNotFound
        String path = ApiConfig.Post.GET_REPLIES_BY_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(postViewService.getRepliesById(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getRepliesById(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetHomepagePosts_ReturnPageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/homepage ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.HOMEPAGE;
        int offset = 0;
        int limit = 20;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, path);

        when(postViewService.getHomepagePosts(any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<PostDTO> actual = objectMapper.readValue(response, new TypeReference<PageDTO<PostDTO>>() {});

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getHomepagePosts(any(Pageable.class));
    }

    @Test
    void PostViewController_GetHomepagePosts_Throw400InvalidRequest_InvalidOffset() throws Exception {
        // api: GET /api/v1/feed/homepage ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.HOMEPAGE;
        int offset = -1;
        int limit = 20;

        String response = mockMvc
            .perform(get(path)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getHomepagePosts(any(Pageable.class));
    }

    @Test
    void PostViewController_GetHomepagePosts_Throw400InvalidRequest_InvalidLimit() throws Exception {
        // api: GET /api/v1/feed/homepage ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.HOMEPAGE;
        int offset = 0;
        int limit = 51;

        String response = mockMvc
            .perform(get(path)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getHomepagePosts(any(Pageable.class));
    }

    @Test
    void PostViewController_GetDiscoverPosts_ReturnPageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/discover ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.DISCOVER;
        int offset = 0;
        int limit = 20;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, path);

        when(postViewService.getDiscoverPosts(any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<PostDTO> actual = objectMapper.readValue(response, new TypeReference<PageDTO<PostDTO>>() {});

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getDiscoverPosts(any(Pageable.class));
    }

    @Test
    void PostViewController_GetDiscoverPosts_Throw400InvalidRequest_InvalidOffset() throws Exception {
        // api: GET /api/v1/feed/discover ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.DISCOVER;
        int offset = -1;
        int limit = 20;

        String response = mockMvc
            .perform(get(path)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getDiscoverPosts(any(Pageable.class));
    }

    @Test
    void PostViewController_GetDiscoverPosts_Throw400InvalidRequest_InvalidLimit() throws Exception {
        // api: GET /api/v1/feed/discover ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.DISCOVER;
        int offset = 0;
        int limit = 51;

        String response = mockMvc
            .perform(get(path)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getDiscoverPosts(any(Pageable.class));
    }

    @Test
    void PostViewController_GetPostsByProfileId_ReturnPageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id} ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.POSTS_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, path);

        when(postViewService.getPostsByAuthorId(eq(id), any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<PostDTO> actual = objectMapper.readValue(response, new TypeReference<PageDTO<PostDTO>>() {});

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getPostsByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetPostsByProfileId_Throw400InvalidRequest_InvalidOffset() throws Exception {
        // api: GET /api/v1/feed/profile/{id} ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.POSTS_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getPostsByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetPostsByProfileId_Throw400InvalidRequest_InvalidLimit() throws Exception {
        // api: GET /api/v1/feed/profile/{id} ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.POSTS_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 51;

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getPostsByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetPostsByProfileId_Throw404ResourceNotFound() throws Exception {
        // api: GET /api/v1/feed/profile/{id} ==> : 404 : ResourceNotFound
        String path = ApiConfig.Feed.POSTS_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(postViewService.getPostsByAuthorId(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getPostsByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetRepliesByProfileId_ReturnPageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/replies ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.REPLIES_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, path);

        when(postViewService.getRepliesByAuthorId(eq(id), any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<PostDTO> actual = objectMapper.readValue(response, new TypeReference<PageDTO<PostDTO>>() {});

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getRepliesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetRepliesByProfileId_Throw400InvalidRequest_InvalidOffset() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/replies ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.REPLIES_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getRepliesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetRepliesByProfileId_Throw400InvalidRequest_InvalidLimit() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/replies ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.REPLIES_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 51;

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getRepliesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetRepliesByProfileId_Throw404ResourceNotFound() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/replies ==> : 404 : ResourceNotFound
        String path = ApiConfig.Feed.REPLIES_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(postViewService.getRepliesByAuthorId(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getRepliesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetLikesByProfileId_ReturnPageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/likes ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.LIKES_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, path);

        when(postViewService.getLikesByAuthorId(eq(id), any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<PostDTO> actual = objectMapper.readValue(response, new TypeReference<PageDTO<PostDTO>>() {});

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getLikesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetLikesByProfileId_Throw400InvalidRequest_InvalidOffset() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/likes ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.LIKES_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getLikesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetLikesByProfileId_Throw400InvalidRequest_InvalidLimit() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/likes ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.LIKES_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 61;

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getLikesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetLikesByProfileId_Throw404ResourceNotFound() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/likes ==> : 404 : ResourceNotFound
        String path = ApiConfig.Feed.LIKES_BY_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(postViewService.getLikesByAuthorId(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getLikesByAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetMentionsOfProfileId_ReturnPageDtoOfPostDto() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.MENTIONS_OF_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<PostDTO> posts = new PageImpl<>(List.of(post), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, path);

        when(postViewService.getMentionsOfAuthorId(eq(id), any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<PostDTO> actual = objectMapper.readValue(response, new TypeReference<PageDTO<PostDTO>>() {});

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getMentionsOfAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetMentionsOfProfileId_Throw400InvalidRequest_InvalidOffset() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.MENTIONS_OF_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getMentionsOfAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetMentionsOfProfileId_Throw400InvalidRequest_InvalidLimit() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> : 400 : InvalidRequest
        String path = ApiConfig.Feed.MENTIONS_OF_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 51;

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, never()).getMentionsOfAuthorId(eq(id), any(Pageable.class));
    }

    @Test
    void PostViewController_GetMentionsOfProfileId_Throw404ResourceNotFound() throws Exception {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> : 404 : ResourceNotFound
        String path = ApiConfig.Feed.MENTIONS_OF_PROFILE_ID;
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        when(postViewService.getMentionsOfAuthorId(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        String response = mockMvc
            .perform(get(path, id)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postViewService, times(1)).getMentionsOfAuthorId(eq(id), any(Pageable.class));
    }

}
