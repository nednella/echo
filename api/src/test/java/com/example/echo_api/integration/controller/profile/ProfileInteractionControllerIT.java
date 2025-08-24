package com.example.echo_api.integration.controller.profile;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.controller.profile.ProfileInteractionController;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;

/**
 * Integration test class for {@link ProfileInteractionController}.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProfileInteractionControllerIT extends IntegrationTest {

    private static final String FOLLOW_PATH = ApiConfig.Profile.FOLLOW_BY_ID;

    @BeforeEach
    void cleanDb() {
        cleaner.cleanProfileInteractions();
    }

    @Test
    void follow_Returns204NoContent_WhenProfileByIdExistsAndNotAlreadyFollowed() {
        // api: POST /api/v1/profile/{id}/follow ==> 204 No Content
        UUID profileId = mockUser.getId();

        authenticatedClient.post()
            .uri(FOLLOW_PATH, profileId)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
    }

    @Test
    void follow_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: POST /api/v1/profile/{id}/follow ==> 404 Not Found : ErrorDTO
        UUID nonExistingProfileId = UUID.randomUUID();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        authenticatedClient.post()
            .uri(FOLLOW_PATH, nonExistingProfileId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test
    void follow_Returns409Conflict_WhenProfileByIdIsYou() {
        // api: POST /api/v1/profile/{id}/follow ==> 409 Conflict : ErrorDTO
        UUID myProfileId = authUser.getId();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.CONFLICT,
            ErrorMessageConfig.Conflict.SELF_ACTION,
            null,
            null);

        authenticatedClient.post()
            .uri(FOLLOW_PATH, myProfileId)
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test
    void follow_Returns409Conflict_WhenProfileByIdAlreadyFollowedByYou() {
        // api: POST /api/v1/profile/{id}/follow ==> 409 Conflict : ErrorDTO
        UUID profileId = mockUser.getId();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.CONFLICT,
            ErrorMessageConfig.Conflict.ALREADY_FOLLOWING,
            null,
            null);

        // 1st follow request --> 204
        authenticatedClient.post()
            .uri(FOLLOW_PATH, profileId)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();

        // 2nd follow request --> 409
        authenticatedClient.post()
            .uri(FOLLOW_PATH, profileId)
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test // TODO: implement when unfollow is refactored to idempotent operation
    void unfollow_Returns204NoContent_WhenProfileByIdExists() {
        // api: DELETE /api/v1/profile/{id}/follow ==> 204 No Content

    }

    @Test // TODO: implement when unfollow is refactored to idempotent operation
    void unfollow_Returns204NoContent_WhenProfileByIdIsYou() {
        // api: DELETE /api/v1/profile/{id}/follow ==> 204 No Content

    }

    @Test // TODO: implement when unfollow is refactored to idempotent operation
    void unfollow_Returns204NoContent_WhenProfileByIdDoesNotExist() {
        // api: DELETE /api/v1/profile/{id}/follow ==> 204 No Content

    }

    @Test // TODO: remove when unfollow is refactored to idempotent operation
    void unfollow_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: DELETE /api/v1/profile/{id}/follow ==> 404 Not Found : ErrorDTO

    }

    @Test // TODO: remove when unfollow is refactored to idempotent operation
    void unfollow_Returns409Conflict_WhenProfileByIdIsYou() {
        // api: DELETE /api/v1/profile/{id}/follow ==> 409 Conflict : ErrorDTO

    }

}
