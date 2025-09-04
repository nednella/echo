package com.example.echo_api.modules.profile.controller;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.profile.exception.ProfileErrorCode;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.testing.support.AbstractIntegrationTest;

/**
 * Integration test class for {@link ProfileInteractionController}.
 */
class ProfileInteractionControllerIT extends AbstractIntegrationTest {

    private static final String FOLLOW_PATH = ApiRoutes.PROFILE.FOLLOW;

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
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID nonExistingProfileId = UUID.randomUUID();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            errorCode.formatMessage(nonExistingProfileId),
            null);

        authenticatedClient.post()
            .uri(FOLLOW_PATH, nonExistingProfileId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void follow_Returns409Conflict_WhenProfileByIdIsYou() {
        // api: POST /api/v1/profile/{id}/follow ==> 409 Conflict : ErrorDTO
        ProfileErrorCode errorCode = ProfileErrorCode.SELF_ACTION;
        UUID myProfileId = authUser.getId();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.CONFLICT,
            errorCode.formatMessage(),
            null);

        authenticatedClient.post()
            .uri(FOLLOW_PATH, myProfileId)
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void follow_Returns409Conflict_WhenProfileByIdAlreadyFollowedByYou() {
        // api: POST /api/v1/profile/{id}/follow ==> 409 Conflict : ErrorDTO
        ProfileErrorCode errorCode = ProfileErrorCode.ALREADY_FOLLOWING;
        UUID profileId = mockUser.getId();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.CONFLICT,
            errorCode.formatMessage(profileId),
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
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void unfollow_Returns204NoContent_WhenProfileByIdExists() {
        // api: DELETE /api/v1/profile/{id}/follow ==> 204 No Content
        UUID profileId = mockUser.getId();

        authenticatedClient.delete()
            .uri(FOLLOW_PATH, profileId)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
    }

    @Test
    void unfollow_Returns204NoContent_WhenProfileByIdDoesNotExist() {
        // api: DELETE /api/v1/profile/{id}/follow ==> 204 No Content
        UUID nonExistingProfileId = UUID.randomUUID();

        authenticatedClient.delete()
            .uri(FOLLOW_PATH, nonExistingProfileId)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
    }

}
