package com.example.echo_api.integration.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import java.util.UUID;

import static org.springframework.http.HttpMethod.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;

/**
 * Integration test class for {@link ProfileViewController}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProfileViewControllerIT extends IntegrationTest {

    @Test
    void ProfileController_GetMe_Return200ProfileDTO() {
        // api: GET /api/v1/profile/me ==> 200 : ProfileDTO
        String path = ApiConfig.Profile.ME;

        ResponseEntity<ProfileDTO> response = restTemplate.getForEntity(path, ProfileDTO.class);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        ProfileDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(authenticatedUser.getUsername(), body.username());
    }

    @Test
    void ProfileController_GetByUsername_Return200ProfileDTO() {
        // api: GET /api/v1/profile/{username} ==> 200 : ProfileDTO
        String path = ApiConfig.Profile.GET_BY_USERNAME;
        String username = authenticatedUser.getUsername();

        ResponseEntity<ProfileDTO> response = restTemplate.getForEntity(path, ProfileDTO.class, username);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // assert body
        ProfileDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(username, body.username());
    }

    @Test
    void ProfileController_GetByUsername_Throw404ResourceNotFound() {
        // api: GET /api/v1/profile/{username} ==> 404 : Resource Not Found
        String path = ApiConfig.Profile.GET_BY_USERNAME;
        String username = "non-existent-user";

        ResponseEntity<ErrorDTO> response = restTemplate.getForEntity(path, ErrorDTO.class, username);

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
    @Sql(scripts = "/sql/profile-interaction-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_GetFollowers_Return200PageOfProfileDTO() {
        // api: GET /api/v1/profile/{id}/followers ==> 200 : PageDTO<ProfileDTO>
        String followPath = ApiConfig.Profile.FOLLOW_BY_ID;
        String getFollowersPath = ApiConfig.Profile.GET_FOLLOWERS_BY_ID + "?offset=0&limit=1";
        UUID id = otherUser.getId();

        // follow target user to create a follow relationship in the db
        ResponseEntity<Void> response1 = restTemplate.postForEntity(followPath, null, Void.class, id);

        // assert response
        assertEquals(NO_CONTENT, response1.getStatusCode());
        assertNull(response1.getBody());

        // get followers of target user
        ParameterizedTypeReference<PageDTO<ProfileDTO>> typeRef = new ParameterizedTypeReference<PageDTO<ProfileDTO>>() {
        };
        ResponseEntity<PageDTO<ProfileDTO>> response2 = restTemplate.exchange(getFollowersPath, GET, null, typeRef, id);

        // assert response
        assertEquals(OK, response2.getStatusCode());
        assertNotNull(response2.getBody());

        PageDTO<ProfileDTO> data = response2.getBody();
        assertNotNull(data);
        assertNull(data.previous());
        assertNull(data.next());
        assertEquals(1, data.total());
        assertEquals(1, data.items().size());
        assertEquals(authenticatedUser.getUsername(), data.items().get(0).username());
    }

    @Test
    void ProfileController_GetFollowers_Return200PageOfEmpty() {
        // api: GET /api/v1/profile/{id}/followers ==> 200 : PageDTO<ProfileDTO>
        String path = ApiConfig.Profile.GET_FOLLOWERS_BY_ID;
        UUID id = otherUser.getId();

        ParameterizedTypeReference<PageDTO<ProfileDTO>> typeRef = new ParameterizedTypeReference<PageDTO<ProfileDTO>>() {
        };
        ResponseEntity<PageDTO<ProfileDTO>> response = restTemplate.exchange(path, GET, null, typeRef, id);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        PageDTO<ProfileDTO> data = response.getBody();
        assertNotNull(data);
        assertNull(data.previous());
        assertNull(data.next());
        assertEquals(0, data.total());
        assertEquals(0, data.items().size());
    }

    @Test
    void ProfileController_GetFollowers_Throw404ResourceNotFound() {
        // api: GET /api/v1/profile/{id}/followers ==> 404 : Resource Not Found
        String path = ApiConfig.Profile.GET_FOLLOWERS_BY_ID;
        UUID id = UUID.randomUUID();

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(path, GET, null, ErrorDTO.class, id);

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
    @Sql(scripts = "/sql/profile-interaction-cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void ProfileController_GetFollowing_Return200PageOfProfileDTO() {
        // api: GET /api/v1/profile/{id}/following ==> 400 : PageDTO<ProfileDTO>
        String followPath = ApiConfig.Profile.FOLLOW_BY_ID;
        String getFollowingPath = ApiConfig.Profile.GET_FOLLOWING_BY_ID + "?offset=0&limit=1";
        UUID followId = otherUser.getId();
        UUID getFollowingId = authenticatedUser.getId();

        // follow target user to create a follow relationship in the db
        ResponseEntity<ErrorDTO> response1 = restTemplate.postForEntity(followPath, null, ErrorDTO.class, followId);

        System.out.println("-----------------------");
        System.out.println(response1.getBody());
        System.out.println("-----------------------");

        // assert response
        assertEquals(NO_CONTENT, response1.getStatusCode());
        assertNull(response1.getBody());

        // get following of existing user
        ParameterizedTypeReference<PageDTO<ProfileDTO>> typeRef = new ParameterizedTypeReference<PageDTO<ProfileDTO>>() {
        };
        ResponseEntity<PageDTO<ProfileDTO>> response2 = restTemplate.exchange(getFollowingPath, GET, null, typeRef,
            getFollowingId);

        // assert response
        assertEquals(OK, response2.getStatusCode());
        assertNotNull(response2.getBody());

        PageDTO<ProfileDTO> data = response2.getBody();
        assertNotNull(data);
        assertNull(data.previous());
        assertNull(data.next());
        assertEquals(1, data.total());
        assertEquals(1, data.items().size());
        assertEquals(followId.toString(), data.items().get(0).id());
    }

    @Test
    void ProfileController_GetFollowing_Return200PageOfEmpty() {
        // api: GET /api/v1/profile/{id}/following ==> 400 : PageDTO<ProfileDTO>
        String path = ApiConfig.Profile.GET_FOLLOWING_BY_ID;
        UUID id = otherUser.getId();

        ParameterizedTypeReference<PageDTO<ProfileDTO>> typeRef = new ParameterizedTypeReference<PageDTO<ProfileDTO>>() {
        };
        ResponseEntity<PageDTO<ProfileDTO>> response = restTemplate.exchange(path, GET, null, typeRef, id);

        // assert response
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());

        PageDTO<ProfileDTO> data = response.getBody();
        assertNotNull(data);
        assertNull(data.previous());
        assertNull(data.next());
        assertEquals(0, data.total());
        assertEquals(0, data.items().size());
    }

    @Test
    void ProfileController_GetFollowing_Throw404ResourceNotFound() {
        // api: GET /api/v1/profile/{id}/following ==> 404 : Resource Not Found
        String path = ApiConfig.Profile.GET_FOLLOWING_BY_ID;
        UUID id = UUID.randomUUID();

        ResponseEntity<ErrorDTO> response = restTemplate.exchange(path, GET, null, ErrorDTO.class, id);

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
