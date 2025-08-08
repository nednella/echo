package com.example.echo_api.unit.controller.profile;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

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
import com.example.echo_api.exception.custom.conflict.AlreadyFollowingException;
import com.example.echo_api.exception.custom.conflict.SelfActionException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
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
        String path = ApiConfig.Profile.FOLLOW_BY_ID;

        UUID id = UUID.randomUUID();
        doNothing().when(profileInteractionService).follow(id);

        mockMvc
            .perform(post(path, id))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileInteractionService, times(1)).follow(id);
    }

    @Test
    void ProfileInteractionController_Follow_Throw404ResouceNotFound() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 404 : Resource Not Found
        String path = ApiConfig.Profile.FOLLOW_BY_ID;

        UUID id = UUID.randomUUID();
        doThrow(new ResourceNotFoundException()).when(profileInteractionService).follow(id);

        String response = mockMvc
            .perform(post(path, id))
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
        verify(profileInteractionService, times(1)).follow(id);
    }

    @Test
    void ProfileInteractionController_Follow_Throw409SelfActionException() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 409 : Self Action
        String path = ApiConfig.Profile.FOLLOW_BY_ID;

        UUID id = UUID.randomUUID();
        doThrow(new SelfActionException()).when(profileInteractionService).follow(id);

        String response = mockMvc
            .perform(post(path, id))
            .andDo(print())
            .andExpect(status().isConflict())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.CONFLICT,
            ErrorMessageConfig.Conflict.SELF_ACTION,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileInteractionService, times(1)).follow(id);
    }

    @Test
    void ProfileInteractionController_Follow_Throw409AlreadyFollowingException() throws Exception {
        // api: POST /api/v1/profile/{username}/follow ==> 409 : AlreadyFollowing
        String path = ApiConfig.Profile.FOLLOW_BY_ID;

        UUID id = UUID.randomUUID();
        doThrow(new AlreadyFollowingException()).when(profileInteractionService).follow(id);

        String response = mockMvc
            .perform(post(path, id))
            .andDo(print())
            .andExpect(status().isConflict())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.CONFLICT,
            ErrorMessageConfig.Conflict.ALREADY_FOLLOWING,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileInteractionService, times(1)).follow(id);
    }

    @Test
    void ProfileInteractionController_Unfollow_Return204NoContent() throws Exception {
        // api: DELETE /api/v1/profile/{username}/follow ==> 204 : No Content
        String path = ApiConfig.Profile.FOLLOW_BY_ID;

        UUID id = UUID.randomUUID();
        doNothing().when(profileInteractionService).unfollow(id);

        mockMvc
            .perform(delete(path, id))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(profileInteractionService, times(1)).unfollow(id);
    }

    @Test
    void ProfileInteractionController_Unfollow_Throw404ResouceNotFound() throws Exception {
        // api: DELETE /api/v1/profile/{username}/follow ==> 404 : Resource Not Found
        String path = ApiConfig.Profile.FOLLOW_BY_ID;

        UUID id = UUID.randomUUID();
        doThrow(new ResourceNotFoundException()).when(profileInteractionService).unfollow(id);

        String response = mockMvc
            .perform(delete(path, id))
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
        verify(profileInteractionService, times(1)).unfollow(id);
    }

    @Test
    void ProfileInteractionController_Unfollow_Throw409SelfActionException() throws Exception {
        // api: DELETE /api/v1/profile/{username}/follow ==> 409 : Self Action
        String path = ApiConfig.Profile.FOLLOW_BY_ID;

        UUID id = UUID.randomUUID();
        doThrow(new SelfActionException()).when(profileInteractionService).unfollow(id);

        String response = mockMvc
            .perform(delete(path, id))
            .andDo(print())
            .andExpect(status().isConflict())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.CONFLICT,
            ErrorMessageConfig.Conflict.SELF_ACTION,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(profileInteractionService, times(1)).unfollow(id);
    }

}
