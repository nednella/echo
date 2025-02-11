package com.example.echo_api.integration.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpMethod.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.controller.auth.AuthController;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.integration.util.TestUtils;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.service.account.AccountService;

/**
 * Integration test class for {@link AuthController}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProfileControllerIT extends IntegrationTest {

    @Autowired
    private AccountService accountService;

    private Account targetUser;

    @BeforeAll
    void setup() {
        targetUser = new Account("target_user", "password1");
        accountService.register(targetUser.getUsername(), targetUser.getPassword());
    }

    @Test
    void ProfileController_GetMe_ReturnProfileDTO() {
        // api: GET /api/v1/profile/me ==> 200 : ProfileDTO
        String path = ApiConfig.Profile.ME;

        ResponseEntity<ProfileDTO> response = restTemplate.getForEntity(path, ProfileDTO.class);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        ProfileDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(existingAccount.getUsername(), body.username());
    }

    @Test
    void ProfileController_UpdateMe_Return204NoContent() {
        // api: PUT /api/v1/profile/me ==> 204 : No Content
        String path = ApiConfig.Profile.ME;

        UpdateProfileDTO body = new UpdateProfileDTO(
            "name",
            "bio",
            "location");

        HttpEntity<UpdateProfileDTO> request = TestUtils.createJsonRequestEntity(body);
        ResponseEntity<Void> response = restTemplate.exchange(path, PUT, request, Void.class);

        // assert response
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void ProfileController_GetByUsername_ReturnProfileDTO() {
        // api: GET /api/v1/profile/{username} ==> 200 : ProfileDTO
        String path = ApiConfig.Profile.GET_BY_USERNAME;

        ResponseEntity<ProfileDTO> response = restTemplate.getForEntity(
            path, ProfileDTO.class, existingAccount.getUsername());

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        ProfileDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(existingAccount.getUsername(), body.username());
    }

    @Test
    void ProfileController_GetByUsername_Throw400UsernameNotFound() {
        // api: GET /api/v1/profile/{username} ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.GET_BY_USERNAME;

        ResponseEntity<ErrorDTO> response = restTemplate.getForEntity(
            path, ErrorDTO.class, "non-existent-user");

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.USERNAME_NOT_FOUND, error.message());
    }

    @Test
    @Sql(scripts = "/sql/relationship-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_Follow_Return204NoContent() {
        // api: POST /api/v1/profile/{username}/follow ==> 204 : No Content
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        ResponseEntity<Void> response = restTemplate.postForEntity(
            path, null, Void.class, targetUser.getUsername());

        // assert response
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void ProfileController_Follow_Throw400UsernameNotFound() {
        // api: POST /api/v1/profile/{username}/follow ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        ResponseEntity<ErrorDTO> response = restTemplate.postForEntity(
            path, null, ErrorDTO.class, "non-existent-user");

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.USERNAME_NOT_FOUND, error.message());
    }

    @Test
    void ProfileController_Follow_ThrowSelfActionException() {
        // api: POST /api/v1/profile/{username}/follow ==> 400 : SelfFollow
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        ResponseEntity<ErrorDTO> response = restTemplate.postForEntity(
            path, null, ErrorDTO.class, existingAccount.getUsername());

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.SELF_FOLLOW, error.message());
    }

    @Test
    void ProfileController_Follow_ThrowAlreadyFollowingException() {
        // api: POST /api/v1/profile/{username}/follow ==> 400 : AlreadyFollowing
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        // follow the user to create a follow relationship in the db
        ResponseEntity<Void> response1 = restTemplate.postForEntity(
            path, null, Void.class, targetUser.getUsername());

        // assert response
        assertEquals(NO_CONTENT, response1.getStatusCode());
        assertNull(response1.getBody());

        // attempt to follow the same user again to force a 400 AlreadyFollowing
        ResponseEntity<ErrorDTO> response2 = restTemplate.postForEntity(
            path, null, ErrorDTO.class, targetUser.getUsername());

        // assert response
        assertEquals(BAD_REQUEST, response2.getStatusCode());
        assertNotNull(response2.getBody());

        // assert error
        ErrorDTO error = response2.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.ALREADY_FOLLOWING, error.message());
    }

    @Test
    @Sql(scripts = "/sql/relationship-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_Unfollow_Return204NoContent() {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 204 : No Content
        String followPath = ApiConfig.Profile.FOLLOW_BY_USERNAME;
        String unfollowPath = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        // follow the user to create a follow relationship in the db
        ResponseEntity<Void> followResponse = restTemplate.postForEntity(
            followPath, null, Void.class, targetUser.getUsername());

        // assert response
        assertEquals(NO_CONTENT, followResponse.getStatusCode());
        assertNull(followResponse.getBody());

        // unfollow the user
        ResponseEntity<Void> unfollowResponse = restTemplate.exchange(
            unfollowPath, DELETE, null, Void.class, targetUser.getUsername());

        // assert response
        assertEquals(NO_CONTENT, unfollowResponse.getStatusCode());
        assertNull(unfollowResponse.getBody());
    }

    @Test
    void ProfileController_Unfollow_Throw400UsernameNotFound() {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(
            path, DELETE, null, ErrorDTO.class, "non-existent-user");

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.USERNAME_NOT_FOUND, error.message());
    }

    @Test
    void ProfileController_Unfollow_ThrowSelfActionException() {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 400 : SelfUnfollow
        String path = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(
            path, DELETE, null, ErrorDTO.class, existingAccount.getUsername());

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.SELF_UNFOLLOW, error.message());
    }

    @Test
    void ProfileController_Unfollow_ThrowNotFollowingException() {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 400 : NotFollowing
        String path = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(
            path, DELETE, null, ErrorDTO.class, targetUser.getUsername());

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.NOT_FOLLOWING, error.message());
    }

    @Test
    @Sql(scripts = "/sql/relationship-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_Block_Return204NoContent() {
        // api: POST /api/v1/profile/{username}/block ==> 204 : No Content
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        ResponseEntity<Void> response = restTemplate.postForEntity(
            path, null, Void.class, targetUser.getUsername());

        // assert response
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void ProfileController_Block_Throw400UsernameNotFound() {
        // api: POST /api/v1/profile/{username}/block ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        ResponseEntity<ErrorDTO> response = restTemplate.postForEntity(
            path, null, ErrorDTO.class, "non-existent-user");

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.USERNAME_NOT_FOUND, error.message());
    }

    @Test
    void ProfileController_Block_ThrowSelfActionException() {
        // api: POST /api/v1/profile/{username}/block ==> 400 : SelfBlock
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        ResponseEntity<ErrorDTO> response = restTemplate.postForEntity(
            path, null, ErrorDTO.class, existingAccount.getUsername());

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.SELF_BLOCK, error.message());
    }

    @Test
    void ProfileController_Block_ThrowAlreadyBlockingException() {
        // api: POST /api/v1/profile/{username}/block ==> 400 : AlreadyBlocking
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        // block the user to create a follow relationship in the db
        ResponseEntity<Void> response1 = restTemplate.postForEntity(
            path, null, Void.class, targetUser.getUsername());

        // assert response
        assertEquals(NO_CONTENT, response1.getStatusCode());
        assertNull(response1.getBody());

        // attempt to block the same user again to force a 400 AlreadyBlocking
        ResponseEntity<ErrorDTO> response2 = restTemplate.postForEntity(
            path, null, ErrorDTO.class, targetUser.getUsername());

        // assert response
        assertEquals(BAD_REQUEST, response2.getStatusCode());
        assertNotNull(response2.getBody());

        // assert error
        ErrorDTO error = response2.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.ALREADY_BLOCKING, error.message());

    }

    @Test
    @Sql(scripts = "/sql/relationship-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_Unblock_Return204NoContent() {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 204 : No Content
        String followPath = ApiConfig.Profile.BLOCK_BY_USERNAME;
        String unfollowPath = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        // block the user to create a block relationship in the db
        ResponseEntity<Void> followResponse = restTemplate.postForEntity(
            followPath, null, Void.class, targetUser.getUsername());

        // assert response
        assertEquals(NO_CONTENT, followResponse.getStatusCode());
        assertNull(followResponse.getBody());

        // unblock the user
        ResponseEntity<Void> unfollowResponse = restTemplate.exchange(
            unfollowPath, DELETE, null, Void.class, targetUser.getUsername());

        // assert response
        assertEquals(NO_CONTENT, unfollowResponse.getStatusCode());
        assertNull(unfollowResponse.getBody());

    }

    @Test
    void ProfileController_Unblock_Throw400UsernameNotFound() {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(
            path, DELETE, null, ErrorDTO.class, "non-existent-user");

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.USERNAME_NOT_FOUND, error.message());
    }

    @Test
    void ProfileController_Unblock_ThrowSelfActionException() {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 400 : SelfUnblock
        String path = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(
            path, DELETE, null, ErrorDTO.class, existingAccount.getUsername());

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.SELF_UNBLOCK, error.message());
    }

    @Test
    void ProfileController_Unblock_ThrowNotBlockingException() {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 400 : NotBlocking
        String path = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(
            path, DELETE, null, ErrorDTO.class, targetUser.getUsername());

        // assert response
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert error
        ErrorDTO error = response.getBody();
        assertNotNull(error);
        assertEquals(BAD_REQUEST.value(), error.status());
        assertEquals(ErrorMessageConfig.NOT_BLOCKING, error.message());
    }

}
