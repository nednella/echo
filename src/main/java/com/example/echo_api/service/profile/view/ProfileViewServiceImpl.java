package com.example.echo_api.service.profile.view;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.profile.BaseProfileService;
import com.example.echo_api.service.session.SessionService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Service implementation for managing read-only {@link Profile} data
 * presentation operations.
 */
@Service
public class ProfileViewServiceImpl extends BaseProfileService implements ProfileViewService {

    private final HttpServletRequest httpServletRequest;

    // @formatter:off
    public ProfileViewServiceImpl(
        SessionService sessionService,
        ProfileRepository profileRepository,
        HttpServletRequest httpServletRequest
    ) {
        super(sessionService, profileRepository);
        this.httpServletRequest = httpServletRequest;
    }
    // @formatter:on

    @Override
    public ProfileDTO getSelf() {
        Account me = getAuthenticatedUser();

        return profileRepository.findProfileDtoById(me.getId(), me.getId())
            .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public ProfileDTO getById(UUID id) throws ResourceNotFoundException {
        Account me = getAuthenticatedUser();

        return profileRepository.findProfileDtoById(id, me.getId())
            .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public ProfileDTO getByUsername(String username) throws ResourceNotFoundException {
        Account me = getAuthenticatedUser();

        return profileRepository.findProfileDtoByUsername(username, me.getId())
            .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public PageDTO<SimplifiedProfileDTO> getFollowers(String username, Pageable page) throws ResourceNotFoundException {
        Profile target = getProfileEntityByUsername(username); // validate existence of username
        Account me = getAuthenticatedUser();

        Page<SimplifiedProfileDTO> query = profileRepository.findFollowerDtosById(
            target.getId(),
            me.getId(),
            page);

        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<SimplifiedProfileDTO> getFollowing(String username, Pageable page) throws ResourceNotFoundException {
        Profile target = getProfileEntityByUsername(username); // validate existence of username
        Account me = getAuthenticatedUser();

        Page<SimplifiedProfileDTO> query = profileRepository.findFollowingDtosById(
            target.getId(),
            me.getId(),
            page);

        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    /**
     * Internal method for obtaining the current HTTP request URI, to be returned as
     * part of a {@link PageDTO} response.
     * 
     * @return The current request's URI as a string.
     */
    private String getCurrentRequestUri() {
        return httpServletRequest.getRequestURI();
    }

}
