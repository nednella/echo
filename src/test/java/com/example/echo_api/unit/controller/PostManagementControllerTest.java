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
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.controller.post.PostManagementController;
import com.example.echo_api.exception.custom.badrequest.InvalidParentIdException;
import com.example.echo_api.exception.custom.forbidden.ResourceOwnershipException;
import com.example.echo_api.persistence.dto.request.post.CreatePostDTO;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.service.post.management.PostManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link PostManagementController}.
 */
@WebMvcTest(PostManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostManagementService postManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void PostManagementController_Create_Return204NoContent() throws Exception {
        // api: POST /api/v1/post ==> : 204 : No Content
        String path = ApiConfig.Post.CREATE;

        CreatePostDTO post = new CreatePostDTO(null, "This is a new post.");
        String body = objectMapper.writeValueAsString(post);

        doNothing().when(postManagementService).create(post);

        mockMvc
            .perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(postManagementService, times(1)).create(post);
    }

    @Test
    void PostManagementController_Create_Throw400InvalidRequest_TextExceeds280Characters() throws Exception {
        // api: POST /api/v1/post ==> : 400 : InvalidRequest
        String path = ApiConfig.Post.CREATE;

        CreatePostDTO post = new CreatePostDTO(
            UUID.randomUUID(),
            "Thistextis281charactersThistextis281charactersThistextis281charactersThistextis281charactersThistextis281charactersThistextis281charactersThistextis281charactersThistextis281charactersThistextis281charactersThistextis281charactersThistextis281charactersThistextis281characters.....");

        String body = objectMapper.writeValueAsString(post);

        String response = mockMvc
            .perform(post(path)
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
            ValidationMessageConfig.TEXT_TOO_LONG,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postManagementService, never()).create(post);
    }

    @Test
    void PostManagementController_Create_Throw400InvalidParentId() throws Exception {
        // api: POST /api/v1/post ==> : 400 : InvalidParentId
        String path = ApiConfig.Post.CREATE;

        CreatePostDTO post = new CreatePostDTO(UUID.randomUUID(), "This is a new post with a parent id.");
        String body = objectMapper.writeValueAsString(post);

        doThrow(new InvalidParentIdException()).when(postManagementService).create(post);

        String response = mockMvc
            .perform(post(path)
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
            ValidationMessageConfig.INVALID_PARENT_ID,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postManagementService, times(1)).create(post);
    }

    @Test
    void PostManagementController_Delete_Return204NoContent() throws Exception {
        // api: DELETE /api/v1/post/{id} ==> : 204 : No Content
        String path = ApiConfig.Post.GET_BY_ID;
        UUID id = UUID.randomUUID();

        doNothing().when(postManagementService).delete(id);

        mockMvc
            .perform(delete(path, id))
            .andDo(print())
            .andExpect(status().isNoContent());

        verify(postManagementService, times(1)).delete(id);
    }

    @Test
    void PostManagementController_Delete_Throw403ResourceOwnershipRequired() throws Exception {
        // api: DELETE /api/v1/post/{id} ==> : 403 : ResourceOwnershipRequired
        String path = ApiConfig.Post.GET_BY_ID;
        UUID id = UUID.randomUUID();

        doThrow(new ResourceOwnershipException()).when(postManagementService).delete(id);

        String response = mockMvc
            .perform(delete(path, id))
            .andDo(print())
            .andExpect(status().isForbidden())
            .andReturn()
            .getResponse()
            .getContentAsString();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.FORBIDDEN,
            ErrorMessageConfig.Forbidden.RESOURCE_OWNERSHIP_REQUIRED,
            null,
            path);

        ErrorDTO actual = objectMapper.readValue(response, ErrorDTO.class);

        assertEquals(expected, actual);
        verify(postManagementService, times(1)).delete(id);
    }

}
