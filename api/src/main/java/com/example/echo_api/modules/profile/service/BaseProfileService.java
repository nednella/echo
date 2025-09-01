package com.example.echo_api.modules.profile.service;

import java.util.UUID;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.shared.service.SessionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Base service implementation for managing CRUD operations surrounding
 * {@link Profile} entities.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class BaseProfileService {

    protected final SessionService sessionService;
    protected final ProfileRepository profileRepository;

    /**
     * Protected method for obtaining the {@link UUID} associated to the
     * authenticated user.
     * 
     * @return the {@link UUID}
     */
    protected UUID getAuthenticatedUserId() {
        return sessionService.getAuthenticatedUserId();
    }

    /**
     * Protected method for obtaining the {@link Profile} associated to the
     * authenticated user.
     * 
     * @return the found {@link Profile}
     */
    protected Profile getAuthenticatedUserProfile() {
        return getProfileEntityById(getAuthenticatedUserId());
    }

    /**
     * Protected method for obtaining a {@link Profile} via {@code id} from
     * {@link ProfileRepository}.
     * 
     * @param id the id of the profile
     * @return the {@link Profile} entity
     * @throws ResourceNotFoundException if no profile by that id exists
     */
    protected Profile getProfileEntityById(UUID id) throws ResourceNotFoundException {
        return profileRepository.findById(id)
            .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Protected method for obtaining a {@link Profile} via {@code username} from
     * {@link ProfileRepository}.
     * 
     * @param username the username of the profile
     * @return the {@link Profile} entity
     * @throws ResourceNotFoundException if no profile by that username exists
     */
    protected Profile getProfileEntityByUsername(String username) throws ResourceNotFoundException {
        return profileRepository.findByUsername(username)
            .orElseThrow(ResourceNotFoundException::new);
    }

}
