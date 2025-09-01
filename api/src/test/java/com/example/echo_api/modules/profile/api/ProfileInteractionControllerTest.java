package com.example.echo_api.modules.profile.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.exception.custom.conflict.AlreadyFollowingException;
import com.example.echo_api.exception.custom.conflict.SelfActionException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.modules.profile.service.ProfileInteractionService;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;

/**
 * Unit test class for {@link ProfileInteractionController}.
 */
@WebMvcTest(ProfileInteractionController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileInteractionControllerTest {

    private static final String FOLLOW_PATH = ApiRoutes.PROFILE.FOLLOW;

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private ProfileInteractionService profileInteractionService;

    @Test
    void follow_Returns204NoContent_WhenProfileByIdExistsAndNotAlreadyFollowed() {
        // api: POST /api/v1/profile/{id}/follow ==> 204 No Content
        UUID id = UUID.randomUUID();

        var response = mvc.post()
            .uri(FOLLOW_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(204)
            .body().isEmpty();

        verify(profileInteractionService).follow(id);
    }

    @Test
    void follow_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: POST /api/v1/profile/{id}/follow ==> 404 Not Found : ErrorDTO
        UUID id = UUID.randomUUID();
        doThrow(new ResourceNotFoundException()).when(profileInteractionService).follow(id);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        var response = mvc.post()
            .uri(FOLLOW_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(404)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileInteractionService).follow(id);
    }

    @Test
    void follow_Returns409Conflict_WhenProfileByIdIsYou() {
        // api: POST /api/v1/profile/{id}/follow ==> 409 Conflict : ErrorDTO
        UUID id = UUID.randomUUID();
        doThrow(new SelfActionException()).when(profileInteractionService).follow(id);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.CONFLICT,
            ErrorMessageConfig.Conflict.SELF_ACTION,
            null,
            null);

        var response = mvc.post()
            .uri(FOLLOW_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(409)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileInteractionService).follow(id);
    }

    @Test
    void follow_Returns409Conflict_WhenProfileByIdAlreadyFollowedByYou() {
        // api: POST /api/v1/profile/{id}/follow ==> 409 Conflict : ErrorDTO
        UUID id = UUID.randomUUID();
        doThrow(new AlreadyFollowingException()).when(profileInteractionService).follow(id);

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.CONFLICT,
            ErrorMessageConfig.Conflict.ALREADY_FOLLOWING,
            null,
            null);

        var response = mvc.post()
            .uri(FOLLOW_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(409)
            .bodyJson().convertTo(ErrorDTO.class).isEqualTo(expected);

        verify(profileInteractionService).follow(id);
    }

    @Test
    void unfollow_Returns204NoContent_IsIdempotent() {
        // api: DELETE /api/v1/profile/{id}/follow ==> 204 No Content
        UUID id = UUID.randomUUID();

        var response = mvc.delete()
            .uri(FOLLOW_PATH, id)
            .exchange();

        assertThat(response)
            .hasStatus(204)
            .body().isEmpty();

        verify(profileInteractionService).unfollow(id);
    }

}
