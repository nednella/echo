package com.example.echo_api.modules.profile.service;

import java.util.UUID;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.exception.ProfileErrorCode;
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
     * Fetch the {@link UUID} associated with the authenticated user.
     * 
     * @return the {@link UUID}
     */
    protected UUID getAuthenticatedUserId() {
        return sessionService.getAuthenticatedUserId();
    }

    /**
     * Fetch the profile associated with the authenticated user.
     * 
     * @return the {@link Profile} entity
     */
    protected Profile getAuthenticatedUserProfile() {
        return getProfileEntityById(getAuthenticatedUserId());
    }

    /**
     * Fetch a profile by {@code id} from {@link ProfileRepository}.
     * 
     * @param id the profile id
     * @return the {@link Profile} entity
     * @throws ApplicationException if no profile with the given id exists
     */
    protected Profile getProfileEntityById(UUID id) {
        return profileRepository.findById(id)
            .orElseThrow(() -> ProfileErrorCode.ID_NOT_FOUND.buildAsException(id));
    }

    /**
     * Fetch a profile by {@code username} from {@link ProfileRepository}.
     * 
     * @param username the profile username
     * @return the {@link Profile} entity
     * @throws ApplicationException if no profile with the given username exists
     */
    protected Profile getProfileEntityByUsername(String username) {
        return profileRepository.findByUsername(username)
            .orElseThrow(() -> ProfileErrorCode.USERNAME_NOT_FOUND.buildAsException(username));
    }

}
