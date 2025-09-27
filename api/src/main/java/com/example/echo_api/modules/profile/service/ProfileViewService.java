package com.example.echo_api.modules.profile.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.profile.dto.response.ProfileDTO;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.shared.pagination.Paged;

public interface ProfileViewService {

    /**
     * Fetch the {@link ProfileDTO} of the authenticated user.
     * 
     * @return a {@link ProfileDTO} resembling the users profile
     */
    ProfileDTO getMe();

    /**
     * Fetch a {@link ProfileDTO} by {@code id}.
     * 
     * @param id the profile id
     * @return a {@link ProfileDTO} resembling the queried profile
     * @throws ApplicationException if no profile with the given id exists
     */
    ProfileDTO getById(UUID id);

    /**
     * Fetch a {@link ProfileDTO} by {@code username}.
     * 
     * @param username the profile username
     * @return a {@link ProfileDTO} resembling the queried profile
     * @throws ApplicationException if no profile with the given username exists
     */
    ProfileDTO getByUsername(String username);

    /**
     * Fetch a {@link Paged} of {@link SimplifiedProfileDTO} for the followers list
     * of the supplied {@code id} and {@code page} parameters.
     * 
     * @param id   the profile id
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link Paged} of {@link SimplifiedProfileDTO} for matches,
     *         otherwise empty
     * @throws ApplicationException if no profile with the given id exists
     */
    Paged<SimplifiedProfileDTO> getFollowers(UUID id, Pageable page);

    /**
     * Fetch a {@link Paged} of {@link SimplifiedProfileDTO} for the following list
     * of the supplied {@code id} and {@code page} parameters.
     * 
     * @param id   the profile id
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link Paged} of {@link SimplifiedProfileDTO} for matches,
     *         otherwise empty
     * @throws ApplicationException if no profile with the given id exists
     */
    Paged<SimplifiedProfileDTO> getFollowing(UUID id, Pageable page);

}
