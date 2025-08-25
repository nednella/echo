package com.example.echo_api.integration.controller.profile;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.controller.profile.ProfileViewController;
import com.example.echo_api.integration.util.IntegrationTest;
import com.example.echo_api.persistence.dto.response.error.ErrorDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.repository.FollowRepository;

/**
 * Integration test class for {@link ProfileViewController}.
 */
class ProfileViewControllerIT extends IntegrationTest {

    private static final String ME_PATH = ApiConfig.Profile.ME;
    private static final String GET_BY_USERNAME_PATH = ApiConfig.Profile.GET_BY_USERNAME;
    private static final String GET_FOLLOWERS_BY_ID_PATH = ApiConfig.Profile.GET_FOLLOWERS_BY_ID;
    private static final String GET_FOLLOWING_BY_ID_PATH = ApiConfig.Profile.GET_FOLLOWING_BY_ID;

    @Autowired
    private FollowRepository followRepository;

    /**
     * Create and persist a new {@link Follow} between the supplied profiles by ID.
     * 
     * @param yourId  your profile ID
     * @param theirId the profile ID of the user you want to follow
     */
    private void followProfile(UUID yourId, UUID theirId) {
        followRepository.save(new Follow(yourId, theirId));
    }

    /**
     * Helper funtion to validate pagination metadata.
     */
    private static void assertPageMetadata(
        PageDTO<?> dto,
        URI expectedPrevious,
        URI expectedNext,
        int expectedOffset,
        int expectedLimit,
        int expectedTotal) {
        assertThat(dto).isNotNull();
        assertThat(dto.previous()).isEqualTo(expectedPrevious);
        assertThat(dto.next()).isEqualTo(expectedNext);
        assertThat(dto.offset()).isEqualTo(expectedOffset);
        assertThat(dto.limit()).isEqualTo(expectedLimit);
        assertThat(dto.total()).isEqualTo(expectedTotal);
    }

    @Test
    void getMe_Returns200ProfileDto() {
        // api: GET /api/v1/profile/me ==> 200 OK : ProfileDTO
        ProfileDTO response = authenticatedClient.get()
            .uri(ME_PATH)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ProfileDTO.class)
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(authUser.getId().toString());
    }

    @Test
    void getByUsername_Returns200ProfileDto_WhenProfileByUsernameExists() {
        // api: GET /api/v1/profile/{username} ==> 200 : ProfileDTO
        String username = MOCK_USER_USERNAME;

        ProfileDTO response = authenticatedClient.get()
            .uri(GET_BY_USERNAME_PATH, username)
            .exchange()
            .expectStatus().isOk()
            .expectBody(ProfileDTO.class)
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(mockUser.getId().toString());
        assertThat(response.username()).isEqualTo(username);
    }

    @Test
    void getByUsername_Returns404NotFound_WhenProfileByUsernameDoesNotExist() {
        // api: GET /api/v1/profile/{username} ==> 404 Not Found : ErrorDTO
        String nonExistingUsername = "i_dont_exist";

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        authenticatedClient.get()
            .uri(GET_BY_USERNAME_PATH, nonExistingUsername)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test
    void getFollowers_Returns200PageDtoOfSimplifiedProfileDto_WhenProfileByIdExists() {
        // api: GET /api/v1/profile/{id}/followers ==> 200 OK : PageDTO<ProfileDTO>
        UUID profileId = mockUser.getId();
        followProfile(authUser.getId(), profileId); // follow mock user

        PageDTO<SimplifiedProfileDTO> response = authenticatedClient.get()
            .uri(GET_FOLLOWERS_BY_ID_PATH, profileId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<PageDTO<SimplifiedProfileDTO>>() {})
            .returnResult()
            .getResponseBody();

        assertPageMetadata(response, null, null, 0, 20, 1);
        assertThat(response.items())
            .hasSize(1)
            .extracting(SimplifiedProfileDTO::id)
            .containsExactly(authUser.getId().toString());
    }

    @Test
    void getFollowers_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/profile/{id}/followers ==> 404 Not Found : ErrorDTO
        UUID nonExistingProfileId = UUID.randomUUID();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        authenticatedClient.get()
            .uri(GET_FOLLOWERS_BY_ID_PATH, nonExistingProfileId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

    @Test
    void getFollowing_Returns200PageDtoOfSimplifiedProfileDto_WhenProfileByIdExists() {
        // api: GET /api/v1/profile/{id}/following ==> 200 OK : PageDTO<ProfileDTO>
        UUID profileId = authUser.getId();
        followProfile(profileId, mockUser.getId()); // follow mock user

        PageDTO<SimplifiedProfileDTO> response = authenticatedClient.get()
            .uri(GET_FOLLOWING_BY_ID_PATH, profileId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<PageDTO<SimplifiedProfileDTO>>() {})
            .returnResult()
            .getResponseBody();

        assertPageMetadata(response, null, null, 0, 20, 1);
        assertThat(response.items())
            .hasSize(1)
            .extracting(SimplifiedProfileDTO::id)
            .containsExactly(mockUser.getId().toString());
    }

    @Test
    void getFollowing_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/profile/{id}/following ==> 404 Not Found : ErrorDTO
        UUID nonExistingProfileId = UUID.randomUUID();

        ErrorDTO expected = new ErrorDTO(
            HttpStatus.NOT_FOUND,
            ErrorMessageConfig.NotFound.RESOURCE_NOT_FOUND,
            null,
            null);

        authenticatedClient.get()
            .uri(GET_FOLLOWING_BY_ID_PATH, nonExistingProfileId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorDTO.class).isEqualTo(expected);
    }

}
