package com.example.echo_api.persistence.repository.custom;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;

public interface CustomProfileRepository {

    /**
     * Find the necessary information to build a {@link ProfileDTO} by {@code id}.
     * 
     * @param id                  The id to search within the repository.
     * @param authenticatedUserId The id of the authenticated user.
     * @return An {@link Optional} containing a {@link ProfileDTO} if found, else
     *         empty.
     */
    Optional<ProfileDTO> findProfileDtoForId(UUID id, UUID authenticatedUserId);

    /**
     * Find the necessary information to build a {@link ProfileDTO} by
     * {@code username}.
     * 
     * @param username            The username to search within the repository.
     * @param authenticatedUserId The id of the authenticated user.
     * @return An {@link Optional} containing a {@link ProfileDTO} if found, else
     *         empty.
     */
    Optional<ProfileDTO> findProfileDtoForUsername(String username, UUID authenticatedUserId);

    /**
     * Find the necessary information to build a {@link Page} of {@link ProfileDTO}
     * for each user following the queried profile by {@code id}.
     * 
     * @param id                  The id of the target profile to search within the
     *                            repository.
     * @param authenticatedUserId The id of the authenticated user.
     * @param p                   The pagination config for the query.
     * @return A {@link Page} of {@link ProfileDTO} ordered by follow date (newest
     *         first).
     */
    Page<SimplifiedProfileDTO> findAllFollowersSimplifiedProfileDtoForId(UUID id, UUID authenticatedUserId, Pageable p);

    /**
     * Find the necessary information to build a {@link Page} of {@link ProfileDTO}
     * for each user followed by the queried profile by {@code id}.
     * 
     * @param id                  The id of the target profile to search within the
     *                            repository.
     * @param authenticatedUserId The id of the authenticated user.
     * @param p                   The pagination config for the query.
     * @return A {@link Page} of {@link ProfileDTO} ordered by follow date (newest
     *         first).
     */
    Page<SimplifiedProfileDTO> findAllFollowingSimplifiedProfileDtoForId(UUID id, UUID authenticatedUserId, Pageable p);

}
