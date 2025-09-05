package com.example.echo_api.modules.profile.service;

import java.util.UUID;

import com.example.echo_api.exception.ApplicationException;

public interface ProfileInteractionService {

    /**
     * Follows a profile by {@code id}.
     * 
     * @param id the profile id
     * @throws ApplicationException if no profile with the given id exists
     * @throws ApplicationException if attempting to follow self profile
     * @throws ApplicationException if already following the profile
     */
    void follow(UUID id);

    /**
     * Unfollows a profile by {@code id}.
     * 
     * <p>
     * This operation is idempotent.
     * 
     * @param id the profile id
     */
    void unfollow(UUID id);

}
