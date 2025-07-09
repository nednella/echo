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
import com.example.echo_api.persistence.repository.PostRepository;

/**
 * Integration test class for {@link PostViewController}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PostViewControllerIT extends IntegrationTest {

    @Autowired
    private PostRepository postRepository;

    private Post post;
    private Post reply;

    @BeforeAll
    void setup() {
        post = new Post(authenticatedUser.getId(), "Test post.");
        post = postRepository.save(post);

        reply = new Post(post.getId(), otherUser.getId(), "Test reply.");
        reply = postRepository.save(reply);
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

    @Test
    // @formatter:off
    void PostViewController_GetPostRepliesById_ReturnPageDtoOfPostDto() {
        // api: GET /api/v1/post/{id}/replies ==> : 200 : PageDTO<PostDTO>
        String path = ApiConfig.Post.GET_REPLIES_BY_ID;
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
    }
    // @formatter:on

    @Test
    void PostViewController_GetPostRepliesById_Throw404ResourceNotFound() {
        // api: GET /api/v1/post/{id}/replies ==> : 404 : ResourceNotFound
        String path = ApiConfig.Post.GET_REPLIES_BY_ID;
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
