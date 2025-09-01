package com.example.echo_api.modules.profile.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.modules.profile.dto.ProfileDTO;
import com.example.echo_api.modules.profile.dto.SimplifiedProfileDTO;
import com.example.echo_api.shared.dto.PageDTO;

public interface ProfileViewService {

    /**
     * Fetch the {@link ProfileDTO} of the authenticated user.
     * 
     * @return A {@link ProfileDTO} resembling the users profile.
     */
    public ProfileDTO getMe();

    /**
     * Fetch a {@link ProfileDTO} by {@code id}.
     * 
     * @param id The id of the profile to query.
     * @return A {@link ProfileDTO} resembling the queried profile.
     * @throws ResourceNotFoundException If no profile by that id exists.
     */
    public ProfileDTO getById(UUID id) throws ResourceNotFoundException;

    /**
     * Fetch a {@link ProfileDTO} by {@code username}.
     * 
     * @param username The username of the profile to query.
     * @return A {@link ProfileDTO} resembling the queried profile.
     * @throws ResourceNotFoundException If no profile by that username exists.
     */
    public ProfileDTO getByUsername(String username) throws ResourceNotFoundException;

    /**
     * Fetch a {@link PageDTO} of {@link SimplifiedProfileDTO} for the followers
     * list of the supplied {@code id} and {@code page} parameters.
     * 
     * @param id   The id of the profile to query.
     * @param page The {@link Pageable} containing the pagination parameters.
     * @return A {@link PageDTO} of {@link SimplifiedProfileDTO} for matches,
     *         otherwise empty.
     * @throws ResourceNotFoundException If no profile by that id exists.
     */
    public PageDTO<SimplifiedProfileDTO> getFollowers(UUID id, Pageable page) throws ResourceNotFoundException;

    /**
     * Fetch a {@link PageDTO} of {@link SimplifiedProfileDTO} for the following
     * list of the supplied {@code id} and {@code page} parameters.
     * 
     * @param id   The id of the profile to query.
     * @param page The {@link Pageable} containing the pagination parameters.
     * @return A {@link PageDTO} of {@link SimplifiedProfileDTO} for matches,
     *         otherwise empty.
     * @throws ResourceNotFoundException If no profile by that id exists.
     */
    public PageDTO<SimplifiedProfileDTO> getFollowing(UUID id, Pageable page) throws ResourceNotFoundException;

}
