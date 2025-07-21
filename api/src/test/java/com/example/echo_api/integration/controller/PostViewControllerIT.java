package com.example.echo_api.integration.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpMethod.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.controller.post.PostViewController;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.model.post.like.PostLike;
import com.example.echo_api.persistence.repository.PostLikeRepository;
import com.example.echo_api.persistence.repository.PostRepository;

/**
 * Integration test class for {@link PostViewController}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PostViewControllerIT extends IntegrationTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    private Post post;
    private Post reply;
    private Post homepagePost;
    private Post discoverPost;

    @BeforeAll
    void setup() {
        // get by id, homepage feed, discover feed
        post = new Post(authenticatedUser.getId(), "Test post.");
        post = postRepository.save(post);

        // post replies, replies feed
        reply = new Post(post.getId(), otherUser.getId(), "Test reply.");
        reply = postRepository.save(reply);

        // homepage feed, discover feed
        homepagePost = new Post(authenticatedUser.getId(), "This post will appear on the homepage feed.");
        homepagePost = postRepository.save(homepagePost);

        // discover feed
        discoverPost = new Post(otherUser.getId(), "The post will appear on the discover feed.");
        discoverPost = postRepository.save(discoverPost);

        // likes feed
        PostLike like = new PostLike(reply.getId(), authenticatedUser.getId());
        postLikeRepository.save(like);
    }

    @Test
    void PostViewController_GetPostById_ReturnPostDto() {
        // api: GET /api/v1/post/{id} ==> : 200 : PostDTO
        String path = ApiConfig.Post.GET_BY_ID;
        UUID id = post.getId();

        ResponseEntity<PostDTO> response = restTemplate.getForEntity(path, PostDTO.class, id);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        PostDTO postDto = response.getBody();
        assertNotNull(postDto);
        assertEquals(post.getId().toString(), postDto.id());
    }

    @Test
    void PostViewController_GetPostById_Throw404ResourceNotFound() {
        // api: GET /api/v1/post/{id} ==> : 404 : ResourceNotFound
        String path = ApiConfig.Post.GET_BY_ID;
        UUID id = UUID.randomUUID();

        ResponseEntity<ErrorDTO> response = restTemplate.getForEntity(path, ErrorDTO.class, id);

        // assert response
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(NOT_FOUND.value(), error.status());
        assertEquals(ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND, error.message());
    }

    @Test // @formatter:off
    void PostViewController_GetRepliesById_ReturnPageDtoOfPostDto() {
        // api: GET /api/v1/post/{id}/replies ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Post.GET_REPLIES_BY_ID + "?offset=0&limit=20";
        UUID id = post.getId();

        ParameterizedTypeReference<PageDTO<PostDTO>> typeRef = new ParameterizedTypeReference<PageDTO<PostDTO>>() {};
        ResponseEntity<PageDTO<PostDTO>> response = restTemplate.exchange(path, GET, null, typeRef, id);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        PageDTO<PostDTO> pageOfPostDto = response.getBody();
        assertNotNull(pageOfPostDto);
        assertEquals(1, pageOfPostDto.total());
        assertEquals(reply.getId().toString(), pageOfPostDto.items().getFirst().id());
    } // @formatter:on

    @Test
    void PostViewController_GetRepliesById_Throw404ResourceNotFound() {
        // api: GET /api/v1/post/{id}/replies ==> : 404 : ResourceNotFound
        String path = ApiConfig.Post.GET_REPLIES_BY_ID + "?offset=0&limit=20";
        UUID id = UUID.randomUUID();

        ResponseEntity<ErrorDTO> response = restTemplate.getForEntity(path, ErrorDTO.class, id);

        // assert response
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(NOT_FOUND.value(), error.status());
        assertEquals(ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND, error.message());
    }

    @Test // @formatter:off
    void PostViewController_GetHomepagePosts_ReturnPageDtoOfPostDto() {
        // api: GET /api/v1/feed/homepage ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.HOMEPAGE + "?offset=0&limit=20";

        ParameterizedTypeReference<PageDTO<PostDTO>> typeRef = new ParameterizedTypeReference<PageDTO<PostDTO>>() {};
        ResponseEntity<PageDTO<PostDTO>> response = restTemplate.exchange(path, GET, null, typeRef);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        PageDTO<PostDTO> posts = response.getBody();
        assertNotNull(posts);
        assertEquals(2, posts.total());
        assertTrue(posts.items().stream().anyMatch(p -> p.id().equals(homepagePost.getId().toString())));
        assertTrue(posts.items().stream().anyMatch(p -> p.id().equals(post.getId().toString())));
    } // @formatter:on

    @Test // @formatter:off
    void PostViewController_GetDiscoverPosts_ReturnPageDtoOfPostDto() {
        // api: GET /api/v1/feed/discover ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.DISCOVER + "?offset=0&limit=20";

        ParameterizedTypeReference<PageDTO<PostDTO>> typeRef = new ParameterizedTypeReference<PageDTO<PostDTO>>() {};
        ResponseEntity<PageDTO<PostDTO>> response = restTemplate.exchange(path, GET, null, typeRef);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        PageDTO<PostDTO> posts = response.getBody();
        assertNotNull(posts);
        assertEquals(3, posts.total());
        assertTrue(posts.items().stream().anyMatch(p -> p.id().equals(post.getId().toString())));
        assertTrue(posts.items().stream().anyMatch(p -> p.id().equals(homepagePost.getId().toString())));
        assertTrue(posts.items().stream().anyMatch(p -> p.id().equals(discoverPost.getId().toString())));
    }

    @Test // @formatter:off
    void PostViewController_GetPostsByProfileId_ReturnPageDtoOfPostDto() {
        // api: GET /api/v1/feed/profile/{id} ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.POSTS_BY_PROFILE_ID + "?offset=0&limit=20";
        UUID id = authenticatedUser.getId();

        ParameterizedTypeReference<PageDTO<PostDTO>> typeRef = new ParameterizedTypeReference<PageDTO<PostDTO>>() {};
        ResponseEntity<PageDTO<PostDTO>> response = restTemplate.exchange(path, GET, null, typeRef, id);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        PageDTO<PostDTO> posts = response.getBody();
        assertNotNull(posts);
        assertEquals(2, posts.total());
        assertTrue(posts.items().stream().anyMatch(p -> p.id().equals(post.getId().toString())));
        assertTrue(posts.items().stream().anyMatch(p -> p.id().equals(homepagePost.getId().toString())));
    } // @formatter:on

    @Test
    void PostViewController_GetPostsByProfileId_Throw404ResourceNotFound() {
        // api: GET /api/v1/feed/profile/{id} ==> : 404 : ResourceNotFound
        String path = ApiConfig.Feed.POSTS_BY_PROFILE_ID + "?offset=0&limit=20";
        UUID id = UUID.randomUUID();

        ResponseEntity<ErrorDTO> response = restTemplate.getForEntity(path, ErrorDTO.class, id);

        // assert response
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(NOT_FOUND.value(), error.status());
        assertEquals(ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND, error.message());
    }

    @Test // @formatter:off
    void PostViewController_GetRepliesByProfileId_ReturnPageDtoOfPostDto() {
        // api: GET /api/v1/feed/profile/{id}/replies ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.REPLIES_BY_PROFILE_ID + "?offset=0&limit=20";
        UUID id = otherUser.getId();

        ParameterizedTypeReference<PageDTO<PostDTO>> typeRef = new ParameterizedTypeReference<PageDTO<PostDTO>>() {};
        ResponseEntity<PageDTO<PostDTO>> response = restTemplate.exchange(path, GET, null, typeRef, id);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        PageDTO<PostDTO> posts = response.getBody();
        assertNotNull(posts);
        assertEquals(1, posts.total());
        assertTrue(posts.items().stream().anyMatch(p -> p.id().equals(reply.getId().toString())));
    } // @formatter:on

    @Test
    void PostViewController_GetRepliesByProfileId_Throw404ResourceNotFound() {
        // api: GET /api/v1/feed/profile/{id}/replies ==> : 404 : ResourceNotFound
        String path = ApiConfig.Feed.REPLIES_BY_PROFILE_ID + "?offset=0&limit=20";
        UUID id = UUID.randomUUID();

        ResponseEntity<ErrorDTO> response = restTemplate.getForEntity(path, ErrorDTO.class, id);

        // assert response
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(NOT_FOUND.value(), error.status());
        assertEquals(ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND, error.message());
    }

    @Test // @formatter:off
    void PostViewController_GetLikesByProfileId_ReturnPageDtoOfPostDto() {
        // api: GET /api/v1/feed/profile/{id}/likes ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.LIKES_BY_PROFILE_ID + "?offset=0&limit=20";
        UUID id = authenticatedUser.getId();

        ParameterizedTypeReference<PageDTO<PostDTO>> typeRef = new ParameterizedTypeReference<PageDTO<PostDTO>>() {};
        ResponseEntity<PageDTO<PostDTO>> response = restTemplate.exchange(path, GET, null, typeRef, id);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        PageDTO<PostDTO> posts = response.getBody();
        assertNotNull(posts);
        assertEquals(1, posts.total());
        assertTrue(posts.items().stream().anyMatch(p -> p.id().equals(reply.getId().toString())));
    } // @formatter:on

    @Test
    void PostViewController_GetLikesByProfileId_Throw404ResourceNotFound() {
        // api: GET /api/v1/feed/profile/{id}/likes ==> : 404 : ResourceNotFound
        String path = ApiConfig.Feed.LIKES_BY_PROFILE_ID + "?offset=0&limit=20";
        UUID id = UUID.randomUUID();

        ResponseEntity<ErrorDTO> response = restTemplate.getForEntity(path, ErrorDTO.class, id);

        // assert response
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(NOT_FOUND.value(), error.status());
        assertEquals(ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND, error.message());
    }

    @Test // @formatter:off
    void PostViewController_GetMentionsOfProfileId_ReturnPageDtoOfPostDto() {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Feed.MENTIONS_OF_PROFILE_ID + "?offset=0&limit=20";
        UUID id = authenticatedUser.getId();

        ParameterizedTypeReference<PageDTO<PostDTO>> typeRef = new ParameterizedTypeReference<PageDTO<PostDTO>>() {};
        ResponseEntity<PageDTO<PostDTO>> response = restTemplate.exchange(path, GET, null, typeRef, id);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        PageDTO<PostDTO> posts = response.getBody();
        assertNotNull(posts);
        assertEquals(0, posts.total());
        assertTrue(posts.items().isEmpty());
        
    } // @formatter:on

    @Test
    void PostViewController_GetMentionsOfProfileId_Throw404ResourceNotFound() {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> : 404 : ResourceNotFound
        String path = ApiConfig.Feed.MENTIONS_OF_PROFILE_ID + "?offset=0&limit=20";
        UUID id = UUID.randomUUID();

        ResponseEntity<ErrorDTO> response = restTemplate.getForEntity(path, ErrorDTO.class, id);

        // assert response
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(NOT_FOUND.value(), error.status());
        assertEquals(ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND, error.message());
    }

}
