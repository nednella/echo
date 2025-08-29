package com.example.echo_api.service.dev;

import java.util.List;

import com.example.echo_api.modules.clerk.dto.sdk.ClerkUserDTO;
import com.example.echo_api.persistence.model.user.User;

public interface DevService {

    /**
     * <b>Untested method intended for development/testing only.</b>
     * 
     * <p>
     * Map the provided user from Clerk to a locally persisted {@link User}.
     * 
     * 
     * @param user the Clerk user to persist
     * @return the persisted {@link User}
     */
    public User persistClerkUser(ClerkUserDTO user);

    /**
     * <b>Untested method intended for development/testing only.</b>
     * 
     * <p>
     * Maps all users from Clerk to a locally persisted {@link User}.
     * 
     * @return {@link List} of the persisted {@link User}
     */
    public List<User> persistAllClerkUsers();

    /**
     * <b>Untested method intended for development/testing only.</b>
     * 
     * <p>
     * Unsynchronises all Clerk users by removing the {@code external_id} field and
     * clearing the {@code onboardingComplete} flag from {@code public_metadata}.
     */
    public void unsyncAllClerkUsers();

}
