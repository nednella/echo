package com.example.echo_api.integration.controller.profile;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.controller.profile.ProfileManagementController;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.persistence.dto.request.profile.UpdateInformationDTO;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;

/**
 * Integration test class for {@link ProfileManagementController}.
 */
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProfileManagementControllerIT extends IntegrationTest {

    private static final String ME_INFO_PATH = ApiConfig.Profile.ME_INFO;

    @Test
    void updateInformation_Returns204NoContent_WhenPassesValidation() {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        var body = new UpdateInformationDTO("x".repeat(50), "x".repeat(160), "x".repeat(30));

        authenticatedClient.put()
            .uri(ME_INFO_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isNoContent()
            .expectBody().isEmpty();
    }

    static Stream<Arguments> invalidNameCases() {
        return Stream.of(Arguments.of("x".repeat(51), ValidationMessageConfig.NAME_TOO_LONG));
    }

    @ParameterizedTest(name = "updateInformation_Returns400BadRequest_WhenProfileNameFieldIsInvalid: \"{0}\"")
    @MethodSource("invalidNameCases")
    void updateInformation_Returns400BadRequest_WhenProfileNameFieldIsInvalid(String name, String expectedDetails) {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        var body = new UpdateInformationDTO(name, "x", "location");

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            expectedDetails,
            null);

        authenticatedClient.put()
            .uri(ME_INFO_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    static Stream<Arguments> invalidBioCases() {
        return Stream.of(Arguments.of("x".repeat(161), ValidationMessageConfig.BIO_TOO_LONG));
    }

    @ParameterizedTest(name = "updateInformation_Returns400BadRequest_WhenProfileBioFieldIsInvalid: \"{0}\"")
    @MethodSource("invalidBioCases")
    void updateInformation_Returns400BadRequest_WhenProfileBioFieldIsInvalid(String bio, String expectedDetails) {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        var body = new UpdateInformationDTO("name", bio, "location");

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            expectedDetails,
            null);

        authenticatedClient.put()
            .uri(ME_INFO_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    static Stream<Arguments> invalidLocationCases() {
        return Stream.of(Arguments.of("x".repeat(31), ValidationMessageConfig.LOCATION_TOO_LONG));
    }

    @ParameterizedTest(name = "updateInformation_Returns400BadRequest_WhenProfileLocationFieldIsInvalid: \"{0}\"")
    @MethodSource("invalidLocationCases")
    void updateInformation_Returns400BadRequest_WhenProfileLocationFieldIsInvalid(String loc, String expectedDetails) {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        var body = new UpdateInformationDTO("name", "bio", loc);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            expectedDetails,
            null);

        authenticatedClient.put()
            .uri(ME_INFO_PATH)
            .bodyValue(body)
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

}
