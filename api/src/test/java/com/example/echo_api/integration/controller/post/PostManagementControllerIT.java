package com.example.echo_api.integration.controller.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpMethod.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.controller.post.PostManagementController;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.integration.util.TestUtils;
import com.example.echo_api.persistence.dto.request.post.CreatePostDTO;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.repository.PostRepository;

/**
 * Integration test class for {@link PostManagementController}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PostManagementControllerIT extends IntegrationTest {

    @Autowired
    private PostRepository postRepository;

    private Post selfPost;
    private Post notSelfPost;

    @BeforeAll
    void setup() {
        selfPost = new Post(authenticatedUser.getId(), "Test post.");
        selfPost = postRepository.save(selfPost);

        notSelfPost = new Post(otherUser.getId(), "Test post.");
        notSelfPost = postRepository.save(notSelfPost);
    }

    @Test
    @Transactional
    void PostManagementController_Create_Return204NoContent() {
        // api: POST /api/v1/post ==> : 204 : No Content
        String path = ApiConfig.Post.CREATE;

        CreatePostDTO body = new CreatePostDTO(null, "Test post.");

        HttpEntity<CreatePostDTO> request = TestUtils.createJsonRequestEntity(body);
        ResponseEntity<Void> response = restTemplate.exchange(path, POST, request, Void.class);

        // assert response
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @Transactional
    void PostManagementController_Create_Throw400InvalidParentId() {
        // api: POST /api/v1/post ==> : 400 : InvalidParentId
        String path = ApiConfig.Post.CREATE;
        UUID invalidParentPostId = UUID.randomUUID();

        CreatePostDTO body = new CreatePostDTO(invalidParentPostId, "Test post.");

        HttpEntity<CreatePostDTO> request = TestUtils.createJsonRequestEntity(body);
        ResponseEntity<ErrorDTO> response = restTemplate.exchange(path, POST, request, ErrorDTO.class);

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.BadRequest.INVALID_REQUEST, error.message());
        assertEquals(ValidationMessageConfig.INVALID_PARENT_ID, error.details());
    }

    @Test
    void PostManagementController_Delete_Return204NoContent() {
        // api: DELETE /api/v1/post/{id} ==> : 204 : No Content
        String path = ApiConfig.Post.GET_BY_ID;
        UUID postId = selfPost.getId();

        ResponseEntity<Void> response = restTemplate.exchange(path, DELETE, null, Void.class, postId);

        // assert response
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void PostManagementController_Delete_Throw403ResourceOwnershipRequired() {
        // api: DELETE /api/v1/post/{id} ==> : 403 : ResourceOwnershipRequired
        String path = ApiConfig.Post.GET_BY_ID;
        UUID postId = notSelfPost.getId();

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(path, DELETE, null, ErrorDTO.class, postId);

        // assert response
        assertEquals(FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(FORBIDDEN.value(), error.status());
        assertEquals(ErrorMessageConfig.Forbidden.RESOURCE_OWNERSHIP_REQUIRED, error.message());
        assertEquals(null, error.details());
    }

}