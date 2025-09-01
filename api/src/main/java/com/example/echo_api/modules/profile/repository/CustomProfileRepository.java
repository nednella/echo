package com.example.echo_api.modules.profile.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.example.echo_api.modules.profile.dto.ProfileDTO;
import com.example.echo_api.modules.profile.dto.SimplifiedProfileDTO;

public interface CustomProfileRepository {

    /**
     * Retrieves a {@link ProfileDTO} for the profile with the specified ID.
     *
     * @param id         the id of the profile to query
     * @param authUserId the id of the authenticated user, required for building
     *                   user relationships
     * @return an {@link Optional} containing the {@link ProfileDTO} if found, else
     *         empty
     */
    Optional<ProfileDTO> findProfileDtoById(@NonNull UUID id, @NonNull UUID authUserId);

    /**
     * Retrieves a {@link ProfileDTO} for the profile with the specified username.
     *
     * @param id         the username of the profile to query
     * @param authUserId the id of the authenticated user, required for building
     *                   user relationships
     * @return an {@link Optional} containing the {@link ProfileDTO} if found, else
     *         empty
     */
    Optional<ProfileDTO> findProfileDtoByUsername(@NonNull String username, @NonNull UUID authUserId);

    /**
     * Retrieves a paginated list of {@link SimplifiedProfileDTO} for users who
     * follow the profile with the specified ID.
     *
     * @param id         The id of the profile to query
     * @param authUserId The id of the authenticated user, required for building
     *                   user relationships
     * @param pageable   The pagination and sorting configuration
     * @return a {@link Page} of {@link SimplifiedProfileDTO} ordered by follow date
     *         (newest first)
     */
    Page<SimplifiedProfileDTO> findFollowerDtosById(@NonNull UUID id, @NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link SimplifiedProfileDTO} for users who are
     * followed by the profile with the specified ID.
     *
     * @param id         The id of the profile to query
     * @param authUserId The id of the authenticated user, required for building
     *                   user relationships
     * @param pageable   The pagination and sorting configuration
     * @return a {@link Page} of {@link SimplifiedProfileDTO} ordered by follow date
     *         (newest first).
     */
    Page<SimplifiedProfileDTO> findFollowingDtosById(@NonNull UUID id, @NonNull UUID authUserId, @NonNull Pageable p);

}
