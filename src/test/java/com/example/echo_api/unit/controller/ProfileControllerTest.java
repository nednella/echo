package com.example.echo_api.unit.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.controller.profile.ProfileController;
import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.BlockedException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.exception.custom.username.UsernameNotFoundException;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.persistence.mapper.ProfileMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.service.profile.ProfileService;
import com.example.echo_api.util.pagination.OffsetLimitRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link ProfileController}.
 */
@WebMvcTest(ProfileController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileService profileService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void ProfileController_GetMe_ReturnProfileDTO() throws Exception {
        // api: GET /api/v1/profile/me ==> 200 : ProfileResponse
        String path = ApiConfig.Profile.ME;

        Account account = new Account("test", "test");
        Profile profile = new Profile(account);
        MetricsDTO metrics = new MetricsDTO(0, 0, 0, 0);
        ProfileDTO expected = ProfileMapper.toDTO(profile, metrics, null);

        when(profileService.getMe()).thenReturn(expected);

        String response = mockMvc
            .perform(get(path))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ProfileDTO actual = objectMapper.readValue(response, ProfileDTO.class);
        assertEquals(expected, actual);
        verify(profileService, times(1)).getMe();
    }

    @Test
    void ProfileController_GetMe_Throw400UsernameNotFound() throws Exception {
        // api: GET /api/v1/profile/me ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.ME;

        when(profileService.getMe()).thenThrow(new UsernameNotFoundException());

        String response = mockMvc
            .perform(get(path))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.USERNAME_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).getMe();
    }

    @Test
    void ProfileController_UpdateMe_Return204NoContent() throws Exception {
        // api: PUT /api/v1/profile/me ==> 204 : No Content
        String path = ApiConfig.Profile.ME;

        UpdateProfileDTO request = new UpdateProfileDTO(
            "name",
            "bio",
            "location");

        String body = objectMapper.writeValueAsString(request);

        doNothing().when(profileService).updateMeProfile(request);

        mockMvc
            .perform(put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileService, times(1)).updateMeProfile(request);
    }

    @Test
    void ProfileController_UpdateMe_Throw400InvalidRequest_NameExceeds50Characters() throws Exception {
        // api: PUT /api/v1/profile/me ==> 400 : Invalid Request
        String path = ApiConfig.Profile.ME;

        UpdateProfileDTO request = new UpdateProfileDTO(
            "ThisNameIsTooLongBy......................1Character",
            "bio",
            "location");

        String body = objectMapper.writeValueAsString(request);

        doNothing().when(profileService).updateMeProfile(request);

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
            ErrorMessageConfig.INVALID_REQUEST,
            ValidationMessageConfig.NAME_TOO_LONG,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, never()).updateMeProfile(request);
    }

    @Test
    void ProfileController_UpdateMe_Throw400InvalidRequest_BioExceeds160Characters() throws Exception {
        // api: PUT /api/v1/profile/me ==> 400 : Invalid Request
        String path = ApiConfig.Profile.ME;

        UpdateProfileDTO request = new UpdateProfileDTO(
            "name",
            "ThisBioIsTooLongBy.....................................................................................................................................1Character",
            "location");

        String body = objectMapper.writeValueAsString(request);

        doNothing().when(profileService).updateMeProfile(request);

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
            ErrorMessageConfig.INVALID_REQUEST,
            ValidationMessageConfig.BIO_TOO_LONG,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, never()).updateMeProfile(request);
    }

    @Test
    void ProfileController_UpdateMe_Throw400InvalidRequest_LocationExceeds30Characters() throws Exception {
        // api: PUT /api/v1/profile/me ==> 400 : Invalid Request
        String path = ApiConfig.Profile.ME;

        UpdateProfileDTO request = new UpdateProfileDTO(
            "name",
            "bio",
            "ThisLocationIsTooLongBy4Characters");

        String body = objectMapper.writeValueAsString(request);

        doNothing().when(profileService).updateMeProfile(request);

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
            ErrorMessageConfig.INVALID_REQUEST,
            ValidationMessageConfig.LOCATION_TOO_LONG,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, never()).updateMeProfile(request);
    }

    @Test
    void ProfileController_GetByUsername_ReturnProfileDTO() throws Exception {
        // api: GET /api/v1/profile/{username} ==> 200 : ProfileDTO
        String path = ApiConfig.Profile.GET_BY_USERNAME;

        Account account = new Account("test", "test");
        Profile profile = new Profile(account);
        MetricsDTO metrics = new MetricsDTO(0, 0, 0, 0);
        RelationshipDTO relationship = new RelationshipDTO(false, false, false, false);
        ProfileDTO expected = ProfileMapper.toDTO(profile, metrics, relationship);

        when(profileService.getByUsername(expected.username())).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, expected.username()))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ProfileDTO actual = objectMapper.readValue(response, ProfileDTO.class);
        assertEquals(expected, actual);
        verify(profileService, times(1)).getByUsername(expected.username());
    }

    @Test
    void ProfileController_GetByUsername_Throw400UsernameNotFound() throws Exception {
        // api: GET /api/v1/profile/{username} ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.GET_BY_USERNAME;

        when(profileService.getByUsername("non-existent-user")).thenThrow(new UsernameNotFoundException());

        String response = mockMvc
            .perform(get(path, "non-existent-user"))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.USERNAME_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).getByUsername("non-existent-user");
    }

    @Test
    void ProfileController_GetFollowers_ReturnPageOfProfileDTO() throws Exception {
        // api: GET /api/v1/profile/{username}/followers ==> 200 : PageDTO<ProfileDTO>
        String path = ApiConfig.Profile.GET_FOLLOWERS_BY_USERNAME;
        String username = "existing-user";
        int offset = 0;
        int limit = 1;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Account account = new Account(username, "password");
        Profile profile = new Profile(account);
        MetricsDTO metrics = new MetricsDTO(0, 0, 0, 0);
        RelationshipDTO relationship = new RelationshipDTO(false, false, false, false);
        ProfileDTO profileDto = ProfileMapper.toDTO(profile, metrics, relationship);
        Page<ProfileDTO> pageProfileDto = new PageImpl<>(List.of(profileDto), page, 1);
        PageDTO<ProfileDTO> expected = PageMapper.toDTO(pageProfileDto, path);

        when(profileService.getFollowers(eq(username), any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, username)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<ProfileDTO> actual = objectMapper.readValue(response, new TypeReference<PageDTO<ProfileDTO>>() {
        });

        verify(profileService, times(1)).getFollowers(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
        assertEquals(1, actual.total());
        assertEquals(1, actual.items().size());
    }

    @Test
    void ProfileController_GetFollowers_ReturnPageOfEmpty() throws Exception {
        // api: GET /api/v1/profile/{username}/followers ==> 200 : PageDTO<ProfileDTO>
        String path = ApiConfig.Profile.GET_FOLLOWERS_BY_USERNAME;
        String username = "existing-user";
        int offset = 0;
        int limit = 1;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<ProfileDTO> pageProfileDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<ProfileDTO> expected = PageMapper.toDTO(pageProfileDto, path);

        when(profileService.getFollowers(eq(username), any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, username)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<ProfileDTO> actual = objectMapper.readValue(response, new TypeReference<PageDTO<ProfileDTO>>() {
        });

        verify(profileService, times(1)).getFollowers(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
        assertEquals(0, actual.total());
        assertEquals(0, actual.items().size());
    }

    @Test
    void ProfileController_GetFollowers_Throw400UsernameNotFound() throws Exception {
        // api: GET /api/v1/profile/{username}/followers ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.GET_FOLLOWERS_BY_USERNAME;
        String username = "non-existent-user";
        int offset = 0;
        int limit = 1;

        when(profileService.getFollowers(eq(username), any(Pageable.class))).thenThrow(new UsernameNotFoundException());

        String response = mockMvc
            .perform(get(path, username)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.USERNAME_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        verify(profileService, times(1)).getFollowers(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
    }

    @Test
    void ProfileController_GetFollowing_ReturnPageOfProfileDTO() throws Exception {
        // api: GET /api/v1/profile/{username}/following ==> 200 : PageDTO<ProfileDTO>
        String path = ApiConfig.Profile.GET_FOLLOWING_BY_USERNAME;
        String username = "existing-user";
        int offset = 0;
        int limit = 1;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Account account = new Account(username, "password");
        Profile profile = new Profile(account);
        MetricsDTO metrics = new MetricsDTO(0, 0, 0, 0);
        RelationshipDTO relationship = new RelationshipDTO(false, false, false, false);
        ProfileDTO profileDto = ProfileMapper.toDTO(profile, metrics, relationship);
        Page<ProfileDTO> pageProfileDto = new PageImpl<>(List.of(profileDto), page, 1);
        PageDTO<ProfileDTO> expected = PageMapper.toDTO(pageProfileDto, path);

        when(profileService.getFollowing(eq(username), any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, username)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<ProfileDTO> actual = objectMapper.readValue(response, new TypeReference<PageDTO<ProfileDTO>>() {
        });

        verify(profileService, times(1)).getFollowing(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
        assertEquals(1, actual.total());
        assertEquals(1, actual.items().size());
    }

    @Test
    void ProfileController_GetFollowing_ReturnPageOfEmpty() throws Exception {
        // api: GET /api/v1/profile/{username}/following ==> 200 : PageDTO<ProfileDTO>
        String path = ApiConfig.Profile.GET_FOLLOWING_BY_USERNAME;
        String username = "existing-user";
        int offset = 0;
        int limit = 1;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<ProfileDTO> pageProfileDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<ProfileDTO> expected = PageMapper.toDTO(pageProfileDto, path);

        when(profileService.getFollowing(eq(username), any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, username)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<ProfileDTO> actual = objectMapper.readValue(response, new TypeReference<PageDTO<ProfileDTO>>() {
        });

        verify(profileService, times(1)).getFollowing(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
        assertEquals(0, actual.total());
        assertEquals(0, actual.items().size());
    }

    @Test
    void ProfileController_GetFollowing_Throw400UsernameNotFound() throws Exception {
        // api: GET /api/v1/profile/{username}/following ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.GET_FOLLOWING_BY_USERNAME;
        String username = "non-existent-user";
        int offset = 0;
        int limit = 1;

        when(profileService.getFollowing(eq(username), any(Pageable.class))).thenThrow(new UsernameNotFoundException());

        String response = mockMvc
            .perform(get(path, username)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.USERNAME_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        verify(profileService, times(1)).getFollowing(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
    }

    @Test
    void ProfileController_Follow_Return204NoContent() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 204 : No Content
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        String username = "username";
        doNothing().when(profileService).follow(username);

        mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileService, times(1)).follow(username);
    }

    @Test
    void ProfileController_Follow_Throw400UsernameNotFound() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new UsernameNotFoundException()).when(profileService).follow("username");

        String response = mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.USERNAME_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).follow(username);
    }

    @Test
    void ProfileController_Follow_ThrowSelfActionException() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 400 : SelfFollow
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_FOLLOW)).when(profileService).follow("username");

        String response = mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.SELF_FOLLOW,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).follow(username);
    }

    @Test
    void ProfileController_Follow_ThrowAlreadyFollowingException() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 400 : AlreadyFollowing
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new AlreadyFollowingException()).when(profileService).follow("username");

        String response = mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.ALREADY_FOLLOWING,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).follow(username);
    }

    @Test
    void ProfileController_Follow_ThrowBlockedException() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 401 : Blocked
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new BlockedException()).when(profileService).follow("username");

        String response = mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isUnauthorized())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.UNAUTHORIZED,
            ErrorMessageConfig.UNAUTHORISED,
            ErrorMessageConfig.BLOCKED,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).follow(username);
    }

    @Test
    void ProfileController_Unfollow_Return204NoContent() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 204 : No Content
        String path = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        String username = "username";
        doNothing().when(profileService).unfollow("username");

        mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileService, times(1)).unfollow(username);
    }

    @Test
    void ProfileController_Unfollow_Throw400UsernameNotFound() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new UsernameNotFoundException()).when(profileService).unfollow("username");

        String response = mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.USERNAME_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).unfollow(username);
    }

    @Test
    void ProfileController_Unfollow_ThrowSelfActionException() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 400 : SelfUnfollow
        String path = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_UNFOLLOW)).when(profileService).unfollow("username");

        String response = mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.SELF_UNFOLLOW,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).unfollow(username);
    }

    @Test
    void ProfileController_Unfollow_ThrowNotFollowingException() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 400 : NotFollowing
        String path = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new NotFollowingException()).when(profileService).unfollow("username");

        String response = mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.NOT_FOLLOWING,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).unfollow(username);
    }

    @Test
    void ProfileController_Block_Return204NoContent() throws Exception {
        // api: POST /api/v1/profile/{username}/block ==> 204 : No Content
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        String username = "username";
        doNothing().when(profileService).block("username");

        mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileService, times(1)).block(username);
    }

    @Test
    void ProfileController_Block_Throw400UsernameNotFound() throws Exception {
        // api: POST /api/v1/profile/{username}/block ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new UsernameNotFoundException()).when(profileService).block("username");

        String response = mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.USERNAME_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).block(username);
    }

    @Test
    void ProfileController_Block_ThrowSelfActionException() throws Exception {
        // api: POST /api/v1/profile/{username}/block ==> 400 : SelfBlock
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_BLOCK)).when(profileService).block("username");

        String response = mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.SELF_BLOCK,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).block(username);
    }

    @Test
    void ProfileController_Block_ThrowAlreadyBlockingException() throws Exception {
        // api: POST /api/v1/profile/{username}/block ==> 400 : AlreadyBlocking
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new AlreadyBlockingException()).when(profileService).block("username");

        String response = mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.ALREADY_BLOCKING,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).block(username);
    }

    @Test
    void ProfileController_Unblock_Return204NoContent() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 204 : No Content
        String path = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        String username = "username";
        doNothing().when(profileService).unblock("username");

        mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileService, times(1)).unblock(username);
    }

    @Test
    void ProfileController_Unblock_Throw400UsernameNotFound() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new UsernameNotFoundException()).when(profileService).unblock("username");

        String response = mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.USERNAME_NOT_FOUND,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).unblock(username);
    }

    @Test
    void ProfileController_Unblock_ThrowSelfActionException() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 400 : SelfUnblock
        String path = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_UNBLOCK)).when(profileService).unblock("username");

        String response = mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.SELF_UNBLOCK,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).unblock(username);
    }

    @Test
    void ProfileController_Unblock_ThrowNotBlockingException() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 400 : NotBlocking
        String path = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new NotBlockingException()).when(profileService).unblock("username");

        String response = mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.NOT_BLOCKING,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileService, times(1)).unblock(username);
    }

}
