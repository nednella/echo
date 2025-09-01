package com.example.echo_api.modules.profile.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.modules.profile.dto.ProfileDTO;
import com.example.echo_api.modules.profile.dto.SimplifiedProfileDTO;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.shared.service.SessionService;

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
    public ProfileDTO getMe() {
        UUID authUserId = getAuthenticatedUserId();

        return profileRepository.findProfileDtoById(authUserId, authUserId)
            .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public ProfileDTO getById(UUID id) throws ResourceNotFoundException {
        UUID authUserId = getAuthenticatedUserId();

        return profileRepository.findProfileDtoById(id, authUserId)
            .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public ProfileDTO getByUsername(String username) throws ResourceNotFoundException {
        UUID authUserId = getAuthenticatedUserId();

        return profileRepository.findProfileDtoByUsername(username, authUserId)
            .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public PageDTO<SimplifiedProfileDTO> getFollowers(UUID id, Pageable page) throws ResourceNotFoundException {
        UUID authUserId = getAuthenticatedUserId();
        UUID profileId = getProfileEntityById(id).getId(); // validate existence of id

        Page<SimplifiedProfileDTO> query = profileRepository.findFollowerDtosById(profileId, authUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<SimplifiedProfileDTO> getFollowing(UUID id, Pageable page) throws ResourceNotFoundException {
        UUID authUserId = getAuthenticatedUserId();
        UUID profileId = getProfileEntityById(id).getId(); // validate existence of id

        Page<SimplifiedProfileDTO> query = profileRepository.findFollowingDtosById(profileId, authUserId, page);
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
