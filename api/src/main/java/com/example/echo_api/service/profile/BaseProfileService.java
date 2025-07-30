package com.example.echo_api.service.profile;

import java.util.UUID;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.session.SessionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Base service implementation for managing CRUD operations surrounding
 * {@link Profile} entities.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseProfileService {

    protected final SessionService sessionService;
    protected final ProfileRepository profileRepository;

    protected UUID getAuthenticatedUserId() {
        return UUID.randomUUID(); // TODO: TEMP FIX
    }

    /**
     * Protected method for obtaining the {@link Profile} associated to the
     * authenticated user.
     * 
     * @return The found {@link Profile}.
     */
    protected Profile getAuthenticatedUserProfile() {
        return getProfileEntityById(getAuthenticatedUserId());
    }

    /**
     * Protected method for obtaining a {@link Profile} via {@code id} from
     * {@link ProfileRepository}.
     * 
     * @param id The id of the profile.
     * @return The {@link Profile} entity.
     * @throws ResourceNotFoundException If no profile by that id exists.
     */
    protected Profile getProfileEntityById(UUID id) throws ResourceNotFoundException {
        return profileRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Protected method for obtaining a {@link Profile} via {@code username} from
     * {@link ProfileRepository}.
     * 
     * @param username The username of the profile.
     * @return The {@link Profile} entity.
     * @throws ResourceNotFoundException If no profile by that username exists.
     */
    protected Profile getProfileEntityByUsername(String username) throws ResourceNotFoundException {
        return profileRepository.findByUsername(username)
                .orElseThrow(ResourceNotFoundException::new);
    }

}
