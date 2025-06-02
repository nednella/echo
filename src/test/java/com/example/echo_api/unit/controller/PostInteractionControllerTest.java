package com.example.echo_api.unit.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.controller.post.PostInteractionController;
import com.example.echo_api.exception.custom.conflict.AlreadyLikedException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.service.post.interaction.PostInteractionService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link PostInteractionController}.
 */
@WebMvcTest(PostInteractionController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostInteractionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostInteractionService postInteractionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void PostInteractionController_Like_Return204NoContent() throws Exception {
        // api: POST /api/v1/post/{id}/like ==> : 204 : No Content
        String path = ApiConfig.Post.LIKE;
        UUID id = UUID.randomUUID();

        doNothing().when(postInteractionService).like(id);

        mockMvc
            .perform(post(path, id))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(postInteractionService, times(1)).like(id);
    }

    @Test
    void PostInteractionController_Like_Throw404ResourceNotFound() throws Exception {
        // api: POST /api/v1/post/{id}/like ==> : 404 : ResourceNotFound
        String path = ApiConfig.Post.LIKE;
        UUID id = UUID.randomUUID();

        doThrow(new ResourceNotFoundException()).when(postInteractionService).like(id);

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
        verify(postInteractionService, times(1)).like(id);
    }

    @Test
    void PostInteractionController_Like_Throw409AlreadyLiked() throws Exception {
        // api: POST /api/v1/post/{id}/like ==> : 409 : AlreadyLiked
        String path = ApiConfig.Post.LIKE;
        UUID id = UUID.randomUUID();

        doThrow(new AlreadyLikedException()).when(postInteractionService).like(id);

        String response = mockMvc
            .perform(post(path, id))
            .andDo(print())
            .andExpect(status().isConflict())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.CONFLICT,
            ErrorMessageConfig.Conflict.ALREADY_LIKED,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postInteractionService, times(1)).like(id);
    }

    @Test
    void PostInteractionController_Unike_Return204NoContent() throws Exception {
        // api: DELETE /api/v1/post/{id}/like ==> : 204 : No Content
        String path = ApiConfig.Post.LIKE;
        UUID id = UUID.randomUUID();

        doNothing().when(postInteractionService).unlike(id);

        mockMvc
            .perform(delete(path, id))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(postInteractionService, times(1)).unlike(id);
    }

}
