package com.example.echo_api.modules.profile.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.profile.dto.response.ProfileDTO;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.modules.profile.entity.ProfileFollow;
import com.example.echo_api.modules.profile.exception.ProfileErrorCode;
import com.example.echo_api.modules.profile.repository.ProfileFollowRepository;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.Paged;
import com.example.echo_api.testing.support.AbstractIntegrationTest;

/**
 * Integration test class for {@link ProfileViewController}.
 */
class ProfileViewControllerIT extends AbstractIntegrationTest {

    private static final String ME_PATH = ApiRoutes.PROFILE.ME;
    private static final String BY_USERNAME_PATH = ApiRoutes.PROFILE.BY_USERNAME;
    private static final String FOLLOWERS_BY_ID_PATH = ApiRoutes.PROFILE.FOLLOWERS;
    private static final String FOLLOWING_BY_ID_PATH = ApiRoutes.PROFILE.FOLLOWING;

    @Autowired
    private ProfileFollowRepository profileFollowRepository;

    /**
     * Create and persist a new {@link ProfileFollow} between the supplied profiles
     * by ID.
     * 
     * @param yourId  your profile ID
     * @param theirId the profile ID of the user you want to follow
     */
    private void followProfile(UUID yourId, UUID theirId) {
        profileFollowRepository.save(new ProfileFollow(yourId, theirId));
    }

    /**
     * Helper funtion to validate pagination metadata.
     */
    private static void assertPageMetadata(
        Paged<?> dto,
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
            .uri(BY_USERNAME_PATH, username)
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
        ProfileErrorCode errorCode = ProfileErrorCode.USERNAME_NOT_FOUND;
        String nonExistingUsername = "i_dont_exist";

        ErrorResponse expected = new ErrorResponse(
            errorCode.getStatus(),
            errorCode.formatMessage(nonExistingUsername),
            null);

        authenticatedClient.get()
            .uri(BY_USERNAME_PATH, nonExistingUsername)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void getFollowers_Returns200PageDtoOfSimplifiedProfileDto_WhenProfileByIdExists() {
        // api: GET /api/v1/profile/{id}/followers ==> 200 OK : PageDTO<ProfileDTO>
        UUID profileId = mockUser.getId();
        followProfile(authUser.getId(), profileId); // follow mock user

        Paged<SimplifiedProfileDTO> response = authenticatedClient.get()
            .uri(FOLLOWERS_BY_ID_PATH, profileId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<Paged<SimplifiedProfileDTO>>() {})
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
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID nonExistingProfileId = UUID.randomUUID();

        ErrorResponse expected = new ErrorResponse(
            errorCode.getStatus(),
            errorCode.formatMessage(nonExistingProfileId),
            null);

        authenticatedClient.get()
            .uri(FOLLOWERS_BY_ID_PATH, nonExistingProfileId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void getFollowing_Returns200PageDtoOfSimplifiedProfileDto_WhenProfileByIdExists() {
        // api: GET /api/v1/profile/{id}/following ==> 200 OK : PageDTO<ProfileDTO>
        UUID profileId = authUser.getId();
        followProfile(profileId, mockUser.getId()); // follow mock user

        Paged<SimplifiedProfileDTO> response = authenticatedClient.get()
            .uri(FOLLOWING_BY_ID_PATH, profileId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<Paged<SimplifiedProfileDTO>>() {})
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
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID nonExistingProfileId = UUID.randomUUID();

        ErrorResponse expected = new ErrorResponse(
            errorCode.getStatus(),
            errorCode.formatMessage(nonExistingProfileId),
            null);

        authenticatedClient.get()
            .uri(FOLLOWING_BY_ID_PATH, nonExistingProfileId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

}
