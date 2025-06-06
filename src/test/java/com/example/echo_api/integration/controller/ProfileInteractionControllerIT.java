package com.example.echo_api.integration.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpMethod.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.controller.profile.ProfileInteractionController;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;

/**
 * Integration test class for {@link ProfileInteractionController}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProfileInteractionControllerIT extends IntegrationTest {

    @Test
    @Sql(scripts = "/sql/profile-interaction-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_Follow_Return204NoContent() {
        // api: POST /api/v1/profile/{username}/follow ==> 204 : No Content
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;
        String username = otherUser.getUsername();

        ResponseEntity<Void> response = restTemplate.postForEntity(path, null, Void.class, username);

        // assert response
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void ProfileController_Follow_Throw404ResourceNotFound() {
        // api: POST /api/v1/profile/{username}/follow ==> 404 : ResourceNotFound
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;
        String username = "non-existent-user";

        ResponseEntity<ErrorDTO> response = restTemplate.postForEntity(path, null, ErrorDTO.class, username);

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
    void ProfileController_Follow_Throw409SelfActionException() {
        // api: POST /api/v1/profile/{username}/follow ==> 409 : SelfAction
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;
        String username = authenticatedUser.getUsername();

        ResponseEntity<ErrorDTO> response = restTemplate.postForEntity(path, null, ErrorDTO.class, username);

        // assert response
        assertEquals(CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(CONFLICT.value(), error.status());
        assertEquals(ErrorMessageConfig.Conflict.SELF_ACTION, error.message());
    }

    @Test
    @Sql(scripts = "/sql/profile-interaction-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_Follow_Throw409AlreadyFollowingException() {
        // api: POST /api/v1/profile/{username}/follow ==> 409 : AlreadyFollowing
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;
        String username = otherUser.getUsername();

        // follow the user to create a follow relationship in the db
        ResponseEntity<Void> response1 = restTemplate.postForEntity(path, null, Void.class, username);

        // assert response
        assertEquals(NO_CONTENT, response1.getStatusCode());
        assertNull(response1.getBody());

        // attempt to follow the same user again to force a 409 AlreadyFollowing
        ResponseEntity<ErrorDTO> response2 = restTemplate.postForEntity(path, null, ErrorDTO.class, username);

        // assert response
        assertEquals(CONFLICT, response2.getStatusCode());
        assertNotNull(response2.getBody());

        // assert error
        ErrorDTO error = response2.getBody();
        assertNotNull(error);
        assertEquals(CONFLICT.value(), error.status());
        assertEquals(ErrorMessageConfig.Conflict.ALREADY_FOLLOWING, error.message());
    }

    @Test
    @Sql(scripts = "/sql/profile-interaction-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_Unfollow_Return204NoContent() {
        // api: DELETE /api/v1/profile/{username}/follow ==> 204 : No Content
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;
        String username = otherUser.getUsername();

        // follow the user to create a follow relationship in the db
        ResponseEntity<Void> followResponse = restTemplate.postForEntity(path, null, Void.class, username);

        // assert response
        assertEquals(NO_CONTENT, followResponse.getStatusCode());
        assertNull(followResponse.getBody());

        // unfollow the user
        ResponseEntity<Void> unfollowResponse = restTemplate.exchange(path, DELETE, null, Void.class, username);

        // assert response
        assertEquals(NO_CONTENT, unfollowResponse.getStatusCode());
        assertNull(unfollowResponse.getBody());
    }

    @Test
    void ProfileController_Unfollow_Throw404ResourceNotFound() {
        // api: DELETE /api/v1/profile/{username}/follow ==> 404 : ResourceNotFound
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;
        String username = "non-existent-user";

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(path, DELETE, null, ErrorDTO.class, username);

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
    void ProfileController_Unfollow_Throw409SelfActionException() {
        // api: DELETE /api/v1/profile/{username}/follow ==> 409 : SelfAction
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;
        String username = authenticatedUser.getUsername();

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(path, DELETE, null, ErrorDTO.class, username);

        // assert response
        assertEquals(CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(CONFLICT.value(), error.status());
        assertEquals(ErrorMessageConfig.Conflict.SELF_ACTION, error.message());
    }

    @Test
    @Sql(scripts = "/sql/profile-interaction-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_Block_Return204NoContent() {
        // api: POST /api/v1/profile/{username}/block ==> 204 : No Content
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;
        String username = otherUser.getUsername();

        ResponseEntity<Void> response = restTemplate.postForEntity(path, null, Void.class, username);

        // assert response
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void ProfileController_Block_Throw404ResourceNotFound() {
        // api: POST /api/v1/profile/{username}/block ==> 404 : ResourceNotFound
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;
        String username = "non-existent-user";

        ResponseEntity<ErrorDTO> response = restTemplate.postForEntity(path, null, ErrorDTO.class, username);

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
    void ProfileController_Block_Throw409SelfActionException() {
        // api: POST /api/v1/profile/{username}/block ==> 400 : SelfAction
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;
        String username = authenticatedUser.getUsername();

        ResponseEntity<ErrorDTO> response = restTemplate.postForEntity(path, null, ErrorDTO.class, username);

        // assert response
        assertEquals(CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(CONFLICT.value(), error.status());
        assertEquals(ErrorMessageConfig.Conflict.SELF_ACTION, error.message());
    }

    @Test
    @Sql(scripts = "/sql/profile-interaction-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_Block_Throw409AlreadyBlockingException() {
        // api: POST /api/v1/profile/{username}/block ==> 409 : AlreadyBlocking
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;
        String username = otherUser.getUsername();

        // block the user to create a block relationship in the db
        ResponseEntity<Void> response1 = restTemplate.postForEntity(path, null, Void.class, username);

        // assert response
        assertEquals(NO_CONTENT, response1.getStatusCode());
        assertNull(response1.getBody());

        // attempt to block the same user again to force a 409 AlreadyBlocking
        ResponseEntity<ErrorDTO> response2 = restTemplate.postForEntity(path, null, ErrorDTO.class, username);

        // assert response
        assertEquals(CONFLICT, response2.getStatusCode());
        assertNotNull(response2.getBody());

        // assert error
        ErrorDTO error = response2.getBody();
        assertNotNull(error);
        assertEquals(CONFLICT.value(), error.status());
        assertEquals(ErrorMessageConfig.Conflict.ALREADY_BLOCKING, error.message());

    }

    @Test
    @Sql(scripts = "/sql/profile-interaction-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_Unblock_Return204NoContent() {
        // api: DELETE /api/v1/profile/{username}/block ==> 204 : No Content
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;
        String username = otherUser.getUsername();

        // block the user to create a block relationship in the db
        ResponseEntity<Void> followResponse = restTemplate.postForEntity(path, null, Void.class, username);

        // assert response
        assertEquals(NO_CONTENT, followResponse.getStatusCode());
        assertNull(followResponse.getBody());

        // unblock the user
        ResponseEntity<Void> unfollowResponse = restTemplate.exchange(path, DELETE, null, Void.class, username);

        // assert response
        assertEquals(NO_CONTENT, unfollowResponse.getStatusCode());
        assertNull(unfollowResponse.getBody());

    }

    @Test
    void ProfileController_Unblock_Throw404ResourceNotFound() {
        // api: DELETE /api/v1/profile/{username}/block ==> 404 : ResourceNotFound
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;
        String username = "non-existent-user";

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(path, DELETE, null, ErrorDTO.class, username);

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
    void ProfileController_Unblock_Throw409SelfActionException() {
        // api: DELETE /api/v1/profile/{username}/block ==> 409 : SelfAction
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;
        String username = authenticatedUser.getUsername();

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(path, DELETE, null, ErrorDTO.class, username);

        // assert response
        assertEquals(CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(CONFLICT.value(), error.status());
        assertEquals(ErrorMessageConfig.Conflict.SELF_ACTION, error.message());
    }

}
