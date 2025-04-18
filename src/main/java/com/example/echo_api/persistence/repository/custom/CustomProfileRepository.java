package com.example.echo_api.persistence.repository.custom;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;

public interface CustomProfileRepository {

    /**
     * Retrieves a {@link ProfileDTO} for the profile with the specified ID.
     *
     * @param id                  the id of the target profile to query.
     * @param authenticatedUserId the id of the authenticated user, required for
     *                            obtaining user relationships.
     * @return an {@link Optional} containing the {@link ProfileDTO} if found, else
     *         empty.
     */
    Optional<ProfileDTO> findProfileDtoById(UUID id, UUID authenticatedUserId);

    /**
     * Retrieves a {@link ProfileDTO} for the profile with the specified username.
     *
     * @param id                  the username of the target profile to query.
     * @param authenticatedUserId the id of the authenticated user, required for
     *                            obtaining user relationships.
     * @return an {@link Optional} containing the {@link ProfileDTO} if found, else
     *         empty.
     */
    Optional<ProfileDTO> findProfileDtoByUsername(String username, UUID authenticatedUserId);

    /**
     * Retrieves a paginated list of {@link SimplifiedProfileDTO} for users who
     * follow the profile with the specified ID.
     *
     * @param id                  The id of the target profile whose followers are
     *                            retrieved.
     * @param authenticatedUserId The id of the authenticated user, required for
     *                            obtaining user relationships.
     * @param pageable            The pagination and sorting configuration.
     * @return a {@link Page} of {@link SimplifiedProfileDTO} ordered by follow date
     *         (newest first).
     */
    Page<SimplifiedProfileDTO> findFollowerDtosById(UUID id, UUID authenticatedUserId, Pageable p);

    /**
     * Retrieves a paginated list of {@link SimplifiedProfileDTO} for users who are
     * followed by the profile with the specified ID.
     *
     * @param id                  The id of the target profile whose followed users
     *                            are retrieved.
     * @param authenticatedUserId The id of the authenticated user, required for
     *                            obtaining user relationships.
     * @param pageable            The pagination and sorting configuration.
     * @return a {@link Page} of {@link SimplifiedProfileDTO} ordered by follow date
     *         (newest first).
     */
    Page<SimplifiedProfileDTO> findFollowingDtosById(UUID id, UUID authenticatedUserId, Pageable p);

}
