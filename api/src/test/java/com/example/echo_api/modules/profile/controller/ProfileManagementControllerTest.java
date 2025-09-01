package com.example.echo_api.modules.profile.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.modules.profile.dto.request.UpdateProfileDTO;
import com.example.echo_api.modules.profile.service.ProfileManagementService;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.dto.ErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link ProfileManagementController}.
 */
@WebMvcTest(ProfileManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileManagementControllerTest {

    private static final String ME_PATH = ApiRoutes.PROFILE.ME;

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private ProfileManagementService profileManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updateProfile_Returns204NoContent_WhenPassesValidation() throws Exception {
        // api: PUT /api/v1/profile/me ==> 204 No Content
        var request = new UpdateProfileDTO("name", "bio", "location");
        String body = objectMapper.writeValueAsString(request);

        var response = mvc.put()
            .uri(ME_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(response)
            .hasStatus(204)
            .body().isEmpty();

        verify(profileManagementService).updateProfile(request);
    }

    @Test
    void updateProfile_Returns400BadRequest_WhenNameExceeds50Characters() throws Exception {
        // api: PUT /api/v1/profile/me ==> 400 Bad Request : ErrorDTO
        String invalidName = "x".repeat(51);
        var request = new UpdateProfileDTO(invalidName, "bio", "location");
        String body = objectMapper.writeValueAsString(request);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.NAME_TOO_LONG,
            null);

        var response = mvc.put()
            .uri(ME_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileManagementService, never()).updateProfile(request);
    }

    @Test
    void updateProfile_Returns400BadRequest_WhenBioExceeds160Characters() throws Exception {
        // api: PUT /api/v1/profile/me ==> 400 Bad Request : ErrorDTO
        String invalidBio = "x".repeat(161);
        var request = new UpdateProfileDTO("name", invalidBio, "location");
        String body = objectMapper.writeValueAsString(request);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.BIO_TOO_LONG,
            null);

        var response = mvc.put()
            .uri(ME_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileManagementService, never()).updateProfile(request);
    }

    @Test
    void updateProfile_Returns400BadRequest_WhenLocationExceeds30Characters() throws Exception {
        // api: PUT /api/v1/profile/me ==> 400 Bad Request : ErrorDTO
        String invalidLocation = "x".repeat(31);
        var request = new UpdateProfileDTO("name", "bio", invalidLocation);
        String body = objectMapper.writeValueAsString(request);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.LOCATION_TOO_LONG,
            null);

        var response = mvc.put()
            .uri(ME_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileManagementService, never()).updateProfile(request);
    }

}
