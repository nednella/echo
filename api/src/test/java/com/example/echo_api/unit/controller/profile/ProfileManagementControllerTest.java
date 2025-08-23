package com.example.echo_api.unit.controller.profile;

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

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.controller.profile.ProfileManagementController;
import com.example.echo_api.persistence.dto.request.profile.UpdateInformationDTO;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.service.profile.management.ProfileManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link ProfileManagementController}.
 */
@WebMvcTest(ProfileManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileManagementControllerTest {

    private static final String ME_INFO_PATH = ApiConfig.Profile.ME_INFO;

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private ProfileManagementService profileManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void updateInformation_Returns204NoContent_WhenPassesValidation() throws Exception {
        // api: PUT /api/v1/profile/me/info ==> 204 No Content
        var request = new UpdateInformationDTO("name", "bio", "location");
        String body = objectMapper.writeValueAsString(request);

        var response = mvc.put()
            .uri(ME_INFO_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(response)
            .hasStatus(204)
            .body().isEmpty();

        verify(profileManagementService).updateInformation(request);
    }

    @Test
    void updateInformation_Returns400BadRequest_WhenNameExceeds50Characters() throws Exception {
        // api: PUT /api/v1/profile/me/info ==> 400 Bad Request : ErrorDTO
        String invalidName = "ThisNameIsTooLongBy......................1Character";
        var request = new UpdateInformationDTO(invalidName, "bio", "location");
        String body = objectMapper.writeValueAsString(request);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.NAME_TOO_LONG,
            ME_INFO_PATH);

        var response = mvc.put()
            .uri(ME_INFO_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileManagementService, never()).updateInformation(request);
    }

    @Test
    void updateInformation_Returns400BadRequest_WhenBioExceeds160Characters() throws Exception {
        // api: PUT /api/v1/profile/me/info ==> 400 Bad Request : ErrorDTO
        String invalidBio = "ThisBioIsTooLongBy.....................................................................................................................................1Character";
        var request = new UpdateInformationDTO("name", invalidBio, "location");
        String body = objectMapper.writeValueAsString(request);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.BIO_TOO_LONG,
            ME_INFO_PATH);

        var response = mvc.put()
            .uri(ME_INFO_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileManagementService, never()).updateInformation(request);
    }

    @Test
    void updateInformation_Returns400BadRequest_WhenLocationExceeds30Characters() throws Exception {
        // api: PUT /api/v1/profile/me/info ==> 400 Bad Request : ErrorDTO
        String invalidLocation = "ThisLocationIsTooLongBy4Characters";
        var request = new UpdateInformationDTO("name", "bio", invalidLocation);
        String body = objectMapper.writeValueAsString(request);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.LOCATION_TOO_LONG,
            ME_INFO_PATH);

        var response = mvc.put()
            .uri(ME_INFO_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileManagementService, never()).updateInformation(request);
    }

}
