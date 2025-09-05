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
     * Throws if a {@link Profile} does not exist with the given {@code id}.
     * 
     * @param id
     * @throws ApplicationException if no profile with the given id exists
     * 
     */
    protected void validateProfileExists(UUID id) {
        if (!profileRepository.existsById(id)) {
            throw ProfileErrorCode.ID_NOT_FOUND.buildAsException(id);
        }
    }

    /**
     * Throws if a {@link Profile} does not exist with the given {@code username}.
     * 
     * @param username
     * @throws ApplicationException if no profile with the given username exists
     * 
     */
    protected void validateProfileExists(String username) {
        if (!profileRepository.existsByUsername(username)) {
            throw ProfileErrorCode.USERNAME_NOT_FOUND.buildAsException(username);
        }
    }

}
