package com.example.echo_api.modules.post.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.exception.custom.badrequest.InvalidParentIdException;
import com.example.echo_api.exception.custom.forbidden.ResourceOwnershipException;
import com.example.echo_api.modules.post.dto.CreatePostDTO;
import com.example.echo_api.modules.post.service.PostManagementService;
import com.example.echo_api.shared.dto.ErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Unit test class for {@link PostManagementController}.
 */
@WebMvcTest(PostManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostManagementControllerTest {

    private static final String CREATE_PATH = ApiRoutes.POST.CREATE;
    private static final String BY_ID_PATH = ApiRoutes.POST.BY_ID;

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private PostManagementService postManagementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_Returns204NoContent_WhenPassesValidation() throws Exception {
        // api: POST /api/v1/post ==> 204 No Content
        CreatePostDTO post = new CreatePostDTO(null, "This is a new post.");
        String body = objectMapper.writeValueAsString(post);

        var response = mvc.post()
            .uri(CREATE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(response)
            .hasStatus(204)
            .body().isEmpty();

        verify(postManagementService).create(post);
    }

    static Stream<Arguments> invalidTextCases() {
        return Stream.of(
            Arguments.of(null, ValidationMessageConfig.TEXT_NULL_OR_BLANK),
            Arguments.of(" ", ValidationMessageConfig.TEXT_NULL_OR_BLANK),
            Arguments.of("x".repeat(281), ValidationMessageConfig.TEXT_TOO_LONG));
    }

    @ParameterizedTest(name = "create_Returns400BadRequest_WhenPostTextFieldIsInvalid: \"{0}\"")
    @MethodSource("invalidTextCases")
    void create_Returns400BadRequest_WhenPostTextFieldIsInvalid(String text, String expectedDetails) throws Exception {
        CreatePostDTO post = new CreatePostDTO(null, text);
        String body = objectMapper.writeValueAsString(post);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            expectedDetails,
            null);

        var response = mvc.post()
            .uri(CREATE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postManagementService, never()).create(post);
    }

    @Test
    void create_Returns400BadRequest_WhenPostByParentIdDoesNotExist() throws Exception {
        // api: POST /api/v1/post ==> 400 Bad Request : ErrorDTO
        CreatePostDTO post = new CreatePostDTO(UUID.randomUUID(), "This is a new post with a parent id.");
        String body = objectMapper.writeValueAsString(post);

        doThrow(new InvalidParentIdException()).when(postManagementService).create(post);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.BAD_REQUEST,
            ErrorMessageConfig.BadRequest.INVALID_REQUEST,
            ValidationMessageConfig.INVALID_PARENT_ID,
            null);

        var response = mvc.post()
            .uri(CREATE_PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .exchange();

        assertThat(response)
            .hasStatus(400)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postManagementService).create(post);
    }

    @Test
    void delete_Returns204NoContent_WhenPostByIdDoesNotExistOrIsOwnedByYou() {
        // api: DELETE /api/v1/post/{id} ==> 204 No Content
        UUID id = UUID.randomUUID();

        var response = mvc.delete()
            .uri(BY_ID_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(204)
            .body().isEmpty();

        verify(postManagementService).delete(id);
    }

    @Test
    void delete_Returns403Forbidden_WhenPostByIdExistsAndIsNotOwnedByYou() {
        // api: DELETE /api/v1/post/{id} ==> 403 Forbidden : ErrorDTO
        UUID id = UUID.randomUUID();

        doThrow(new ResourceOwnershipException()).when(postManagementService).delete(id);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.FORBIDDEN,
            ErrorMessageConfig.Forbidden.RESOURCE_OWNERSHIP_REQUIRED,
            null,
            null);

        var response = mvc.delete()
            .uri(BY_ID_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(403)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(postManagementService).delete(id);
    }

}
