package com.example.echo_api.service.profile.view;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.example.echo_api.exception.custom.account.IdNotFoundException;
import com.example.echo_api.exception.custom.username.UsernameNotFoundException;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;

public interface ProfileViewService {

    /**
     * Fetch the {@link ProfileDTO} of the authenticated user.
     * 
     * @return A {@link ProfileDTO} resembling the users profile.
     */
    public ProfileDTO getSelf();

    /**
     * Fetch a {@link ProfileDTO} by {@code id}.
     * 
     * @param id The id of the profile to query.
     * @return A {@link ProfileDTO} resembling the queried profile.
     * @throws IdNotFoundException If no profile by that id exists.
     */
    public ProfileDTO getById(UUID id) throws IdNotFoundException;

    /**
     * Fetch a {@link ProfileDTO} by {@code username}.
     * 
     * @param username The username of the profile to query.
     * @return A {@link ProfileDTO} resembling the queried profile.
     * @throws UsernameNotFoundException If no profile by that username exists.
     */
    public ProfileDTO getByUsername(String username) throws UsernameNotFoundException;

    /**
     * Fetch a {@link PageDTO} of {@link SimplifiedProfileDTO} for the followers
     * list of the supplied {@code id} and {@code page} parameters.
     * 
     * @param id   The id of the profile to query.
     * @param page The {@link Pageable} containing the pagination parameters.
     * @return A {@link PageDTO} of {@link SimplifiedProfileDTO} for matches,
     *         otherwise empty.
     * @throws IdNotFoundException If no profile by that id exists.
     */
    public PageDTO<SimplifiedProfileDTO> getFollowers(UUID id, Pageable page) throws IdNotFoundException;

    /**
     * Fetch a {@link PageDTO} of {@link SimplifiedProfileDTO} for the following
     * list of the supplied {@code id} and {@code page} parameters.
     * 
     * @param id   The id of the profile to query.
     * @param page The {@link Pageable} containing the pagination parameters.
     * @return A {@link PageDTO} of {@link SimplifiedProfileDTO} for matches,
     *         otherwise empty.
     * @throws IdNotFoundException If no profile by that id exists.
     */
    public PageDTO<SimplifiedProfileDTO> getFollowing(UUID id, Pageable page) throws IdNotFoundException;

}
