package com.example.echo_api.unit.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.controller.profile.ProfileManagementController;
import com.example.echo_api.exception.custom.internalserver.CloudinaryException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
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

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileManagementService profileManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockMultipartFile validImage;
    private static MockMultipartFile invalidImageFileSize;
    private static MockMultipartFile invalidImageFormat;

    @BeforeAll
    static void setup() throws Exception {
        validImage = readFileFromResources("data/valid_image.jpg", "image/jpeg");
        invalidImageFileSize = readFileFromResources("data/invalid_image_file_size.jpg", "image/jpeg");
        invalidImageFormat = readFileFromResources("data/invalid_image_format.pdf", "application/pdf");
    }

    private static MockMultipartFile readFileFromResources(String fileName, String fileType) throws IOException {
        InputStream inputStream = ProfileManagementControllerTest.class.getClassLoader().getResourceAsStream(fileName);

        return new MockMultipartFile(
            "file",
            fileName,
            fileType,
            inputStream);
    }

    @Test
    void ProfileManagementController_UpdateInformation_Return204NoContent() throws Exception {
        // api: PUT /api/v1/profile/me/info ==> 204 : No Content
        String path = ApiConfig.Profile.ME_INFO;

        UpdateInformationDTO request = new UpdateInformationDTO(
            "name",
            "bio",
            "location");

        String body = objectMapper.writeValueAsString(request);

        doNothing().when(profileManagementService).updateInformation(request);

        mockMvc
            .perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileManagementService, times(1)).updateInformation(request);
    }

    @Test
    void ProfileManagementController_UpdateInformation_Throw400InvalidRequest_NameExceeds50Characters()
        throws Exception {
        // api: PUT /api/v1/profile/me/info ==> 400 : Invalid Request
        String path = ApiConfig.Profile.ME_INFO;

        UpdateInformationDTO request = new UpdateInformationDTO(
            "ThisNameIsTooLongBy......................1Character",
            "bio",
            "location");

        String body = objectMapper.writeValueAsString(request);

        doNothing().when(profileManagementService).updateInformation(request);

        String response = mockMvc
            .perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.NAME_TOO_LONG,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, never()).updateInformation(request);
    }

    @Test
    void ProfileManagementController_UpdateInformation_Throw400InvalidRequest_BioExceeds160Characters()
        throws Exception {
        // api: PUT /api/v1/profile/me/info ==> 400 : Invalid Request
        String path = ApiConfig.Profile.ME_INFO;

        UpdateInformationDTO request = new UpdateInformationDTO(
            "name",
            "ThisBioIsTooLongBy.....................................................................................................................................1Character",
            "location");

        String body = objectMapper.writeValueAsString(request);

        doNothing().when(profileManagementService).updateInformation(request);

        String response = mockMvc
            .perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.BIO_TOO_LONG,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, never()).updateInformation(request);
    }

    @Test
    void ProfileManagementController_UpdateInformation_Throw400InvalidRequest_LocationExceeds30Characters()
        throws Exception {
        // api: PUT /api/v1/profile/me/info ==> 400 : Invalid Request
        String path = ApiConfig.Profile.ME_INFO;

        UpdateInformationDTO request = new UpdateInformationDTO(
            "name",
            "bio",
            "ThisLocationIsTooLongBy4Characters");

        String body = objectMapper.writeValueAsString(request);

        doNothing().when(profileManagementService).updateInformation(request);

        String response = mockMvc
            .perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.LOCATION_TOO_LONG,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, never()).updateInformation(request);
    }

    @Test
    void ProfileManagementController_UpdateAvatar_Return204NoContent() throws Exception {
        // api: POST /api/v1/profile/me/avatar ==> 204 : No Content
        String path = ApiConfig.Profile.ME_AVATAR;

        MockMultipartFile file = validImage;

        doNothing().when(profileManagementService).updateAvatar(file);

        mockMvc
            .perform(multipart(path)
                .file(file))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileManagementService, times(1)).updateAvatar(file);
    }

    @Test
    void ProfileManagementController_UpdateAvatar_Throw400ImageFormat() throws Exception {
        // api: POST /api/v1/profile/me/avatar ==> 400 : ImageFormat
        String path = ApiConfig.Profile.ME_AVATAR;

        MockMultipartFile file = invalidImageFormat;

        String response = mockMvc
            .perform(multipart(path)
                .file(file))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.IMAGE_FORMAT_UNSUPPORTED,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, never()).updateAvatar(file);
    }

    @Test
    void ProfileManagementController_UpdateAvatar_Throw400ImageSize() throws Exception {
        // api: POST /api/v1/profile/me/avatar ==> 400 : ImageSize
        String path = ApiConfig.Profile.ME_AVATAR;

        MockMultipartFile file = invalidImageFileSize;

        String response = mockMvc
            .perform(multipart(path)
                .file(file))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.IMAGE_SIZE_TOO_LARGE,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, never()).updateAvatar(file);
    }

    @Test
    void ProfileManagementController_UpdateAvatar_Throw500CloudinaryException() throws Exception {
        // api: POST /api/v1/profile/me/avatar ==> 500 : CloudinaryException
        String path = ApiConfig.Profile.ME_AVATAR;

        MockMultipartFile file = validImage;

        doThrow(new CloudinaryException("Placeholder"))
            .when(profileManagementService).updateAvatar(file);

        String response = mockMvc
            .perform(multipart(path)
                .file(file))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessageConfig.InternalServerError.CLOUDINARY_SDK_ERROR,
            "Placeholder",
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, times(1)).updateAvatar(file);
    }

    @Test
    void ProfileManagementController_DeleteAvatar_Return204NoContent() throws Exception {
        // api: DELETE /api/v1/profile/me/avatar ==> 204 : No Content
        String path = ApiConfig.Profile.ME_AVATAR;

        doNothing().when(profileManagementService).deleteAvatar();

        mockMvc
            .perform(delete(path))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileManagementService, times(1)).deleteAvatar();
    }

    @Test
    void ProfileManagementController_DeleteAvatar_Throw500CloudinaryException() throws Exception {
        // api: DELETE /api/v1/profile/me/avatar ==> 500 : CloudinaryException
        String path = ApiConfig.Profile.ME_AVATAR;

        doThrow(new CloudinaryException("Placeholder"))
            .when(profileManagementService).deleteAvatar();

        String response = mockMvc
            .perform(delete(path))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessageConfig.InternalServerError.CLOUDINARY_SDK_ERROR,
            "Placeholder",
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, times(1)).deleteAvatar();
    }

    @Test
    void ProfileManagementController_DeleteAvatar_Throw404ResourceNotFound() throws Exception {
        // api: DELETE /api/v1/profile/me/avatar ==> 404 : ResourceNotFound
        String path = ApiConfig.Profile.ME_AVATAR;

        doThrow(new ResourceNotFoundException()).when(profileManagementService).deleteAvatar();

        String response = mockMvc
            .perform(delete(path))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, times(1)).deleteAvatar();
    }

    @Test
    void ProfileManagementController_UpdateBanner_Return204NoContent() throws Exception {
        // api: POST /api/v1/profile/me/banner ==> 204 : No Content
        String path = ApiConfig.Profile.ME_BANNER;

        MockMultipartFile file = validImage;

        doNothing().when(profileManagementService).updateBanner(file);

        mockMvc
            .perform(multipart(path)
                .file(file))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileManagementService, times(1)).updateBanner(file);
    }

    @Test
    void ProfileManagementController_UpdateBanner_Throw400ImageFormat() throws Exception {
        // api: POST /api/v1/profile/me/banner ==> 400 : ImageFormat
        String path = ApiConfig.Profile.ME_BANNER;

        MockMultipartFile file = invalidImageFormat;

        doNothing().when(profileManagementService).updateBanner(file);

        String response = mockMvc
            .perform(multipart(path)
                .file(file))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.IMAGE_FORMAT_UNSUPPORTED,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, never()).updateBanner(file);
    }

    @Test
    void ProfileManagementController_UpdateBanner_Throw400ImageSize() throws Exception {
        // api: POST /api/v1/profile/me/banner ==> 400 : ImageSize
        String path = ApiConfig.Profile.ME_BANNER;

        MockMultipartFile file = invalidImageFileSize;

        doNothing().when(profileManagementService).updateBanner(file);

        String response = mockMvc
            .perform(multipart(path)
                .file(file))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.IMAGE_SIZE_TOO_LARGE,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, never()).updateBanner(file);
    }

    @Test
    void ProfileManagementController_UpdateBanner_Throw500CloudinaryException() throws Exception {
        // api: POST /api/v1/profile/me/banner ==> 500 : CloudinaryException
        String path = ApiConfig.Profile.ME_BANNER;

        MockMultipartFile file = validImage;

        doThrow(new CloudinaryException("Placeholder"))
            .when(profileManagementService).updateBanner(file);

        String response = mockMvc
            .perform(multipart(path)
                .file(file))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessageConfig.InternalServerError.CLOUDINARY_SDK_ERROR,
            "Placeholder",
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, times(1)).updateBanner(file);
    }

    @Test
    void ProfileManagementController_DeleteBanner_Return204NoContent() throws Exception {
        // api: DELETE /api/v1/profile/me/banner ==> 204 : No Content
        String path = ApiConfig.Profile.ME_BANNER;

        doNothing().when(profileManagementService).deleteBanner();

        mockMvc
            .perform(delete(path))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileManagementService, times(1)).deleteBanner();
    }

    @Test
    void ProfileManagementController_DeleteBanner_Throw404ResourceNotFound() throws Exception {
        // api: DELETE /api/v1/profile/me/banner ==> 404 : ResourceNotFound
        String path = ApiConfig.Profile.ME_BANNER;

        doThrow(new ResourceNotFoundException()).when(profileManagementService).deleteBanner();

        String response = mockMvc
            .perform(delete(path))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, times(1)).deleteBanner();
    }

    @Test
    void ProfileManagementController_DeleteBanner_Throw500CloudinaryException() throws Exception {
        // api: DELETE /api/v1/profile/me/banner ==> 500 : CloudinaryException
        String path = ApiConfig.Profile.ME_BANNER;

        doThrow(new CloudinaryException("Placeholder"))
            .when(profileManagementService).deleteBanner();

        String response = mockMvc
            .perform(delete(path))
            .andDo(print())
            .andExpect(status().isInternalServerError())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorMessageConfig.InternalServerError.CLOUDINARY_SDK_ERROR,
            "Placeholder",
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileManagementService, times(1)).deleteBanner();
    }

}
