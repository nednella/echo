package com.example.echo_api.modules.profile.api;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.modules.profile.dto.request.UpdateProfileDTO;
import com.example.echo_api.shared.constants.ApiRoutes;
import com.example.echo_api.shared.dto.ErrorDTO;
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

    static Stream<Arguments> invalidNameCases() {
        return Stream.of(Arguments.of("x".repeat(51), ValidationMessageConfig.NAME_TOO_LONG));
    }

    @ParameterizedTest(name = "updateProfile_Returns400BadRequest_WhenProfileNameFieldIsInvalid: \"{0}\"")
    @MethodSource("invalidNameCases")
    void updateProfile_Returns400BadRequest_WhenProfileNameFieldIsInvalid(String name, String expectedDetails) {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        var body = new UpdateProfileDTO(name, "x", "location");

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            expectedDetails,
            null);

        authenticatedClient.put()
            .uri(ME_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    static Stream<Arguments> invalidBioCases() {
        return Stream.of(Arguments.of("x".repeat(161), ValidationMessageConfig.BIO_TOO_LONG));
    }

    @ParameterizedTest(name = "updateProfile_Returns400BadRequest_WhenProfileBioFieldIsInvalid: \"{0}\"")
    @MethodSource("invalidBioCases")
    void updateProfile_Returns400BadRequest_WhenProfileBioFieldIsInvalid(String bio, String expectedDetails) {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        var body = new UpdateProfileDTO("name", bio, "location");

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            expectedDetails,
            null);

        authenticatedClient.put()
            .uri(ME_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    static Stream<Arguments> invalidLocationCases() {
        return Stream.of(Arguments.of("x".repeat(31), ValidationMessageConfig.LOCATION_TOO_LONG));
    }

    @ParameterizedTest(name = "updateProfile_Returns400BadRequest_WhenProfileLocationFieldIsInvalid: \"{0}\"")
    @MethodSource("invalidLocationCases")
    void updateProfile_Returns400BadRequest_WhenProfileLocationFieldIsInvalid(String loc, String expectedDetails) {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        var body = new UpdateProfileDTO("name", "bio", loc);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            expectedDetails,
            null);

        authenticatedClient.put()
            .uri(ME_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

}
