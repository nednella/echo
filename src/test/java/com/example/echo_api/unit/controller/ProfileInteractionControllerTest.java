package com.example.echo_api.unit.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.controller.profile.ProfileInteractionController;
import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.BlockedException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.exception.custom.username.UsernameNotFoundException;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.service.profile.interaction.ProfileInteractionService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link ProfileInteractionController}.
 */
@WebMvcTest(ProfileInteractionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileInteractionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileInteractionService profileInteractionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void ProfileInteractionController_Follow_Return204NoContent() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 204 : No Content
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        String username = "username";
        doNothing().when(profileInteractionService).follow(username);

        mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileInteractionService, times(1)).follow(username);
    }

    @Test
    void ProfileInteractionController_Follow_Throw400UsernameNotFound() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new UsernameNotFoundException()).when(profileInteractionService).follow("username");

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
        verify(profileInteractionService, times(1)).follow(username);
    }

    @Test
    void ProfileInteractionController_Follow_ThrowSelfActionException() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 400 : SelfFollow
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new SelfActionException()).when(profileInteractionService)
            .follow("username");

        String response = mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.SELF_ACTION,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileInteractionService, times(1)).follow(username);
    }

    @Test
    void ProfileInteractionController_Follow_ThrowAlreadyFollowingException() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 400 : AlreadyFollowing
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new AlreadyFollowingException()).when(profileInteractionService).follow("username");

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
        verify(profileInteractionService, times(1)).follow(username);
    }

    @Test
    void ProfileInteractionController_Follow_ThrowBlockedException() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 401 : Blocked
        String path = ApiConfig.Profile.FOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new BlockedException()).when(profileInteractionService).follow("username");

        String response = mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.FORBIDDEN,
            ErrorMessageConfig.FORBIDDEN,
            ErrorMessageConfig.BLOCKED,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileInteractionService, times(1)).follow(username);
    }

    @Test
    void ProfileInteractionController_Unfollow_Return204NoContent() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 204 : No Content
        String path = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        String username = "username";
        doNothing().when(profileInteractionService).unfollow("username");

        mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileInteractionService, times(1)).unfollow(username);
    }

    @Test
    void ProfileInteractionController_Unfollow_Throw400UsernameNotFound() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new UsernameNotFoundException()).when(profileInteractionService).unfollow("username");

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
        verify(profileInteractionService, times(1)).unfollow(username);
    }

    @Test
    void ProfileInteractionController_Unfollow_ThrowSelfActionException() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 400 : SelfUnfollow
        String path = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new SelfActionException()).when(profileInteractionService)
            .unfollow("username");

        String response = mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.SELF_ACTION,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileInteractionService, times(1)).unfollow(username);
    }

    @Test
    void ProfileInteractionController_Unfollow_ThrowNotFollowingException() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unfollow ==> 400 : NotFollowing
        String path = ApiConfig.Profile.UNFOLLOW_BY_USERNAME;

        String username = "username";
        doThrow(new NotFollowingException()).when(profileInteractionService).unfollow("username");

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
        verify(profileInteractionService, times(1)).unfollow(username);
    }

    @Test
    void ProfileInteractionController_Block_Return204NoContent() throws Exception {
        // api: POST /api/v1/profile/{username}/block ==> 204 : No Content
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        String username = "username";
        doNothing().when(profileInteractionService).block("username");

        mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileInteractionService, times(1)).block(username);
    }

    @Test
    void ProfileInteractionController_Block_Throw400UsernameNotFound() throws Exception {
        // api: POST /api/v1/profile/{username}/block ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new UsernameNotFoundException()).when(profileInteractionService).block("username");

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
        verify(profileInteractionService, times(1)).block(username);
    }

    @Test
    void ProfileInteractionController_Block_ThrowSelfActionException() throws Exception {
        // api: POST /api/v1/profile/{username}/block ==> 400 : SelfBlock
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new SelfActionException()).when(profileInteractionService)
            .block("username");

        String response = mockMvc
            .perform(post(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.SELF_ACTION,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileInteractionService, times(1)).block(username);
    }

    @Test
    void ProfileInteractionController_Block_ThrowAlreadyBlockingException() throws Exception {
        // api: POST /api/v1/profile/{username}/block ==> 400 : AlreadyBlocking
        String path = ApiConfig.Profile.BLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new AlreadyBlockingException()).when(profileInteractionService).block("username");

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
        verify(profileInteractionService, times(1)).block(username);
    }

    @Test
    void ProfileInteractionController_Unblock_Return204NoContent() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 204 : No Content
        String path = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        String username = "username";
        doNothing().when(profileInteractionService).unblock("username");

        mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileInteractionService, times(1)).unblock(username);
    }

    @Test
    void ProfileInteractionController_Unblock_Throw400UsernameNotFound() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 400 : UsernameNotFound
        String path = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new UsernameNotFoundException()).when(profileInteractionService).unblock("username");

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
        verify(profileInteractionService, times(1)).unblock(username);
    }

    @Test
    void ProfileInteractionController_Unblock_ThrowSelfActionException() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 400 : SelfUnblock
        String path = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new SelfActionException()).when(profileInteractionService)
            .unblock("username");

        String response = mockMvc
            .perform(delete(path, username))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.SELF_ACTION,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileInteractionService, times(1)).unblock(username);
    }

    @Test
    void ProfileInteractionController_Unblock_ThrowNotBlockingException() throws Exception {
        // api: DELETE /api/v1/profile/{username}/unblock ==> 400 : NotBlocking
        String path = ApiConfig.Profile.UNBLOCK_BY_USERNAME;

        String username = "username";
        doThrow(new NotBlockingException()).when(profileInteractionService).unblock("username");

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
        verify(profileInteractionService, times(1)).unblock(username);
    }

}
