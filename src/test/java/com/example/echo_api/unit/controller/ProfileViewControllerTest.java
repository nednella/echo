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
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.controller.profile.ProfileViewController;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.service.profile.view.ProfileViewService;
import com.example.echo_api.util.pagination.OffsetLimitRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link ProfileViewController}.
 */
@WebMvcTest(ProfileViewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileViewService profileViewService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProfileDTO createProfileDto(UUID id, String username) {
        return new ProfileDTO(
            id.toString(),
            username,
            null,
            null,
            null,
            null,
            null,
            null,
            new MetricsDTO(0, 0, 0, 0),
            null);
    }

    private SimplifiedProfileDTO createSimplifiedProfileDto(UUID id, String username) {
        return new SimplifiedProfileDTO(
            id.toString(),
            username,
            null,
            null,
            null);
    }

    @Test
    void ProfileViewController_GetMe_Return200ProfileDTO() throws Exception {
        // api: GET /api/v1/profile/me ==> 200 : ProfileResponse
        String path = ApiConfig.Profile.ME;

        ProfileDTO expected = createProfileDto(UUID.randomUUID(), "test");

        when(profileViewService.getSelf()).thenReturn(expected);

        String response = mockMvc
            .perform(get(path))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ProfileDTO actual = objectMapper.readValue(response, ProfileDTO.class);
        assertEquals(expected, actual);
        verify(profileViewService, times(1)).getSelf();
    }

    @Test
    void ProfileViewController_GetMe_Throw404ResourceNotFound() throws Exception {
        // api: GET /api/v1/profile/me ==> 404 : Resource Not Found
        String path = ApiConfig.Profile.ME;

        when(profileViewService.getSelf()).thenThrow(new ResourceNotFoundException());

        String response = mockMvc
            .perform(get(path))
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
        verify(profileViewService, times(1)).getSelf();
    }

    @Test
    void ProfileViewController_GetByUsername_Return200ProfileDTO() throws Exception {
        // api: GET /api/v1/profile/{username} ==> 200 : ProfileDTO
        String path = ApiConfig.Profile.GET_BY_USERNAME;
        String username = "test";

        ProfileDTO expected = createProfileDto(UUID.randomUUID(), username);

        when(profileViewService.getByUsername(username)).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, username))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ProfileDTO actual = objectMapper.readValue(response, ProfileDTO.class);
        assertEquals(expected, actual);
        verify(profileViewService, times(1)).getByUsername(username);
    }

    @Test
    void ProfileViewController_GetByUsername_Throw404ResourceNotFound() throws Exception {
        // api: GET /api/v1/profile/{username} ==> 404 : Resource Not Found
        String path = ApiConfig.Profile.GET_BY_USERNAME;
        String username = "non-existent-user";

        when(profileViewService.getByUsername(username)).thenThrow(new ResourceNotFoundException());

        String response = mockMvc
            .perform(get(path, username))
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
        verify(profileViewService, times(1)).getByUsername(username);
    }

    @Test
    void ProfileViewController_GetFollowers_Return200PageOfProfileDTO() throws Exception {
        // api: GET /api/v1/profile/{username}/followers ==> 200 : PageDTO<ProfileDTO>
        String path = ApiConfig.Profile.GET_FOLLOWERS_BY_USERNAME;
        String username = "existing-user";
        int offset = 0;
        int limit = 1;

        Pageable page = new OffsetLimitRequest(offset, limit);

        SimplifiedProfileDTO profileDto = createSimplifiedProfileDto(UUID.randomUUID(), username);
        Page<SimplifiedProfileDTO> pageProfileDto = new PageImpl<>(List.of(profileDto), page, 1);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(pageProfileDto, path);

        when(profileViewService.getFollowers(eq(username), any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, username)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<SimplifiedProfileDTO> actual = objectMapper.readValue(response,
            new TypeReference<PageDTO<SimplifiedProfileDTO>>() {
            });

        verify(profileViewService, times(1)).getFollowers(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
        assertEquals(1, actual.total());
        assertEquals(1, actual.items().size());
    }

    @Test
    void ProfileViewController_GetFollowers_Return200PageOfEmpty() throws Exception {
        // api: GET /api/v1/profile/{username}/followers ==> 200 : PageDTO<ProfileDTO>
        String path = ApiConfig.Profile.GET_FOLLOWERS_BY_USERNAME;
        String username = "existing-user";
        int offset = 0;
        int limit = 1;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<SimplifiedProfileDTO> pageProfileDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(pageProfileDto, path);

        when(profileViewService.getFollowers(eq(username), any(Pageable.class))).thenReturn(expected);

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

        verify(profileViewService, times(1)).getFollowers(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
        assertEquals(0, actual.total());
        assertEquals(0, actual.items().size());
    }

    @Test
    void ProfileViewController_GetFollowers_Throw404ResourceNotFound() throws Exception {
        // api: GET /api/v1/profile/{username}/followers ==> 404 : Resource Not Found
        String path = ApiConfig.Profile.GET_FOLLOWERS_BY_USERNAME;
        String username = "non-existent-user";
        int offset = 0;
        int limit = 1;

        when(profileViewService.getFollowers(eq(username), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        String response = mockMvc
            .perform(get(path, username)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
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

        verify(profileViewService, times(1)).getFollowers(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
    }

    @Test
    void ProfileViewController_GetFollowing_Return200PageOfProfileDTO() throws Exception {
        // api: GET /api/v1/profile/{username}/following ==> 200 : PageDTO<ProfileDTO>
        String path = ApiConfig.Profile.GET_FOLLOWING_BY_USERNAME;
        String username = "existing-user";
        int offset = 0;
        int limit = 1;

        Pageable page = new OffsetLimitRequest(offset, limit);

        SimplifiedProfileDTO profileDto = createSimplifiedProfileDto(UUID.randomUUID(), username);
        Page<SimplifiedProfileDTO> pageProfileDto = new PageImpl<>(List.of(profileDto), page, 1);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(pageProfileDto, path);

        when(profileViewService.getFollowing(eq(username), any(Pageable.class))).thenReturn(expected);

        String response = mockMvc
            .perform(get(path, username)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        PageDTO<SimplifiedProfileDTO> actual = objectMapper.readValue(response,
            new TypeReference<PageDTO<SimplifiedProfileDTO>>() {
            });

        verify(profileViewService, times(1)).getFollowing(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
        assertEquals(1, actual.total());
        assertEquals(1, actual.items().size());
    }

    @Test
    void ProfileViewController_GetFollowing_Return200PageOfEmpty() throws Exception {
        // api: GET /api/v1/profile/{username}/following ==> 200 : PageDTO<ProfileDTO>
        String path = ApiConfig.Profile.GET_FOLLOWING_BY_USERNAME;
        String username = "existing-user";
        int offset = 0;
        int limit = 1;

        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<SimplifiedProfileDTO> pageProfileDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(pageProfileDto, path);

        when(profileViewService.getFollowing(eq(username), any(Pageable.class))).thenReturn(expected);

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

        verify(profileViewService, times(1)).getFollowing(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
        assertEquals(0, actual.total());
        assertEquals(0, actual.items().size());
    }

    @Test
    void ProfileViewController_GetFollowing_Throw404ResourceNotFound() throws Exception {
        // api: GET /api/v1/profile/{username}/following ==> 404 : Resource Not Found
        String path = ApiConfig.Profile.GET_FOLLOWING_BY_USERNAME;
        String username = "non-existent-user";
        int offset = 0;
        int limit = 1;

        when(profileViewService.getFollowing(eq(username), any(Pageable.class)))
            .thenThrow(new ResourceNotFoundException());

        String response = mockMvc
            .perform(get(path, username)
                .param("offset", String.valueOf(offset))
                .param("limit", String.valueOf(limit)))
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

        verify(profileViewService, times(1)).getFollowing(eq(username), any(Pageable.class));
        assertEquals(expected, actual);
    }

}
