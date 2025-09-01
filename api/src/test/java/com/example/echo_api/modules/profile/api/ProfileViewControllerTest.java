package com.example.echo_api.modules.profile.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileMetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileRelationshipDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.service.profile.view.ProfileViewService;
import com.example.echo_api.util.OffsetLimitRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link ProfileViewController}.
 */
@WebMvcTest(ProfileViewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileViewControllerTest {

    private static final String ME_PATH = ApiRoutes.PROFILE.ME;
    private static final String BY_USERNAME_PATH = ApiRoutes.PROFILE.BY_USERNAME;
    private static final String FOLLOWERS_BY_ID_PATH = ApiRoutes.PROFILE.FOLLOWERS;
    private static final String FOLLOWING_BY_ID_PATH = ApiRoutes.PROFILE.FOLLOWING;

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private ProfileViewService profileViewService;

    @Autowired
    private ObjectMapper objectMapper;

    private static ProfileDTO profile;
    private static SimplifiedProfileDTO simplifiedProfile;

    @BeforeAll
    static void setup() {
        profile = new ProfileDTO(
            UUID.randomUUID().toString(),
            "username",
            "name",
            "bio",
            "location",
            "imageUrl",
            Instant.now().toString(),
            new ProfileMetricsDTO(0, 0, 0),
            new ProfileRelationshipDTO(false, false));

        simplifiedProfile = new SimplifiedProfileDTO(
            UUID.randomUUID().toString(),
            "username",
            "name",
            "imageUrl",
            new ProfileRelationshipDTO(false, false));
    }

    @Test
    void getMe_Returns200ProfileDto_WhenIExist() {
        // api: GET /api/v1/profile/me ==> 200 OK : ProfileDTO
        when(profileViewService.getMe()).thenReturn(profile);

        var response = mvc.get()
            .uri(ME_PATH)
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().convertTo(ProfileDTO.class).isEqualTo(profile);

        verify(profileViewService).getMe();
    }

    @Test
    void getMe_Throw404ResourceNotFound_WhenIDoNotExist() {
        // api: GET /api/v1/profile/me ==> 404 Not Found : ErrorDTO
        when(profileViewService.getMe()).thenThrow(new ResourceNotFoundException());

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        var response = mvc.get()
            .uri(ME_PATH)
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileViewService).getMe();
    }

    @Test
    void getByUsername_Returns200ProfileDto_WhenProfileByUsernameExists() {
        // api: GET /api/v1/profile/{username} ==> 200 : ProfileDTO
        String username = "test";
        when(profileViewService.getByUsername(username)).thenReturn(profile);

        var response = mvc.get()
            .uri(BY_USERNAME_PATH, username)
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().convertTo(ProfileDTO.class).isEqualTo(profile);

        verify(profileViewService).getByUsername(username);
    }

    @Test
    void getByUsername_Returns404NotFound_WhenProfileByUsernameDoesNotExist() {
        // api: GET /api/v1/profile/{username} ==> 404 Not Found : ErrorDTO
        String username = "non-existent-user";
        when(profileViewService.getByUsername(username)).thenThrow(new ResourceNotFoundException());

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        var response = mvc.get()
            .uri(BY_USERNAME_PATH, username)
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileViewService).getByUsername(username);
    }

    @Test
    void getFollowers_Returns200PageDtoOfProfileDto_WhenProfileByIdExists() throws Exception {
        // api: GET /api/v1/profile/{id}/followers ==> 200 OK : PageDTO<ProfileDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 20;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<SimplifiedProfileDTO> pageProfileDto = new PageImpl<>(List.of(simplifiedProfile), page, 1);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(pageProfileDto, FOLLOWERS_BY_ID_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(profileViewService.getFollowers(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(FOLLOWERS_BY_ID_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(profileViewService).getFollowers(eq(id), any(Pageable.class));
    }

    @Test
    void getFollowers_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/profile/{id}/followers ==> 404 Not Found : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 1;

        when(profileViewService.getFollowers(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        var response = mvc.get()
            .uri(FOLLOWERS_BY_ID_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileViewService).getFollowers(eq(id), any(Pageable.class));
    }

    @Test
    void getFollowers_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/profile/{id}/followers ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 20;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            null);

        var response = mvc.get()
            .uri(FOLLOWERS_BY_ID_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileViewService, never()).getFollowers(eq(id), any(Pageable.class));
    }

    @Test
    void getFollowers_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/profile/{id}/followers ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 0;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            null);

        var response = mvc.get()
            .uri(FOLLOWERS_BY_ID_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileViewService, never()).getFollowers(eq(id), any(Pageable.class));
    }

    @Test
    void getFollowing_Returns200PageDtoOfProfileDto_WhenProfileByIdExists() throws Exception {
        // api: GET /api/v1/profile/{id}/following ==> 200 OK : PageDTO<ProfileDTO>
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 1;

        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<SimplifiedProfileDTO> pageProfileDto = new PageImpl<>(List.of(simplifiedProfile), page, 1);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(pageProfileDto, FOLLOWING_BY_ID_PATH);
        String expectedJson = objectMapper.writeValueAsString(expected);

        when(profileViewService.getFollowing(eq(id), any(Pageable.class))).thenReturn(expected);

        var response = mvc.get()
            .uri(FOLLOWING_BY_ID_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(200)
            .bodyJson().isEqualTo(expectedJson);

        verify(profileViewService).getFollowing(eq(id), any(Pageable.class));
    }

    @Test
    void getFollowing_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/profile/{id}/following ==> 404 Not Found : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 1;

        when(profileViewService.getFollowing(eq(id), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        var response = mvc.get()
            .uri(FOLLOWING_BY_ID_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileViewService).getFollowing(eq(id), any(Pageable.class));
    }

    @Test
    void getFollowing_Returns400BadRequest_WhenInvalidOffsetSupplied() {
        // api: GET /api/v1/profile/{id}/following ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = -1;
        int limit = 1;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_OFFSET,
            null);

        var response = mvc.get()
            .uri(FOLLOWING_BY_ID_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileViewService, never()).getFollowing(eq(id), any(Pageable.class));
    }

    @Test
    void getFollowing_Returns400BadRequest_WhenInvalidLimitSupplied() {
        // api: GET /api/v1/profile/{id}/following ==> 400 Bad Request : ErrorDTO
        UUID id = UUID.randomUUID();
        int offset = 0;
        int limit = 0;

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_LIMIT,
            null);

        var response = mvc.get()
            .uri(FOLLOWING_BY_ID_PATH, id)
            .queryParam("offset", String.valueOf(offset))
            .queryParam("limit", String.valueOf(limit))
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileViewService, never()).getFollowing(eq(id), any(Pageable.class));
    }

}
