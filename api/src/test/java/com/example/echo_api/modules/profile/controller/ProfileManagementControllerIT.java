package com.example.echo_api.modules.profile.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.profile.dto.request.UpdateProfileDTO;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.testing.support.AbstractIntegrationTest;

/**
 * Integration test class for {@link ProfileManagementController}.
 */
class ProfileManagementControllerIT extends AbstractIntegrationTest {

    private static final String ME_PATH = ApiRoutes.PROFILE.ME;

    @Test
    void updateProfile_Returns204NoContent_WhenPassesValidation() {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        var body = new UpdateProfileDTO("x".repeat(50), "x".repeat(160), "x".repeat(30));

        authenticatedClient.put()
            .uri(ME_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
    }

    void updateProfile_Returns400BadRequest_WhenProfileNameFieldIsInvalid() {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        String invalidName = "x".repeat(51);
        var body = new UpdateProfileDTO(invalidName, "x", "location");

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Name must not exceed 50 characters",
            null);

        authenticatedClient.put()
            .uri(ME_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    void updateProfile_Returns400BadRequest_WhenProfileBioFieldIsInvalid() {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        String invalidBio = "x".repeat(161);
        var body = new UpdateProfileDTO("name", invalidBio, "location");

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Bio must not exceed 160 characters",
            null);

        authenticatedClient.put()
            .uri(ME_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    void updateProfile_Returns400BadRequest_WhenProfileLocationFieldIsInvalid() {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        String invalidLocation = "x".repeat(31);
        var body = new UpdateProfileDTO("name", "bio", invalidLocation);

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            "Location must not exceed 30 characters",
            null);

        authenticatedClient.put()
            .uri(ME_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

}
