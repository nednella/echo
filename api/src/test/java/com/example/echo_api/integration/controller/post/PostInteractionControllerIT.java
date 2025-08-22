package com.example.echo_api.integration.controller.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpMethod.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.controller.post.PostInteractionController;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.repository.PostRepository;

/**
 * Integration test class for {@link PostInteractionController}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PostInteractionControllerIT extends IntegrationTest {

    @Autowired
    private PostRepository postRepository;

    private Post post;

    @BeforeAll
    void setup() {
        post = new Post(authUser.getId(), "Test post.");
        post = postRepository.save(post);
    }

    @Test
    @Sql(scripts = "/sql/post-interaction-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void PostInteractionController_Like_Return204NoContent() {
        // api: POST /api/v1/post/{id}/like ==> : 204 : No Content
        String path = ApiConfig.Post.LIKE;
        UUID id = post.getId();

        ResponseEntity<Void> response = restTemplate.postForEntity(path, null, Void.class, id);

        // assert response
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void PostInteractionController_Like_Throw404ResourceNotFound() {
        // api: POST /api/v1/post/{id}/like ==> : 404 : ResourceNotFound
        String path = ApiConfig.Post.LIKE;
        UUID id = UUID.randomUUID();

        ResponseEntity<ErrorDTO> response = restTemplate.postForEntity(path, null, ErrorDTO.class, id);

        // assert response
        assertEquals(NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(NOT_FOUND.value(), error.status());
        assertEquals(ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND, error.message());
        assertEquals(null, error.details());
    }

    @Test
    @Sql(scripts = "/sql/post-interaction-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void PostInteractionController_Like_Throw409AlreadyLiked() {
        // api: POST /api/v1/post/{id}/like ==> : 409 : AlreadyLiked
        String path = ApiConfig.Post.LIKE;
        UUID id = post.getId();

        ResponseEntity<Void> response1 = restTemplate.postForEntity(path, null, Void.class, id);

        // assert 1st response
        assertEquals(NO_CONTENT, response1.getStatusCode());
        assertNull(response1.getBody());

        ResponseEntity<ErrorDTO> response2 = restTemplate.postForEntity(path, null, ErrorDTO.class, id);

        // assert 2nd response
        assertEquals(CONFLICT, response2.getStatusCode());
        assertNotNull(response2.getBody());

        // assert error
        ErrorDTO error = response2.getBody();
        assertNotNull(error);
        assertEquals(CONFLICT.value(), error.status());
        assertEquals(ErrorMessageConfig.Conflict.ALREADY_LIKED, error.message());
        assertEquals(null, error.details());
    }

    @Test
    void PostInteractionController_Unlike_Return204NoContent() {
        // api: DELETE /api/v1/post/{id}/like ==> : 204 : No Content
        String path = ApiConfig.Post.LIKE;
        UUID existingId = post.getId();
        UUID nonExistingId = UUID.randomUUID();

        ResponseEntity<Void> response1 = restTemplate.exchange(path, DELETE, null, Void.class, existingId);

        // assert existing id returns 204
        assertEquals(NO_CONTENT, response1.getStatusCode());
        assertNull(response1.getBody());

        ResponseEntity<Void> response2 = restTemplate.exchange(path, DELETE, null, Void.class, nonExistingId);

        // assert invalid id returns 204 (idempotent endpoint)
        assertEquals(NO_CONTENT, response2.getStatusCode());
        assertNull(response2.getBody());
    }

}
