package com.example.echo_api.service.auth;

import com.clerk.backend_api.Clerk;
import com.example.echo_api.exception.custom.badrequest.OnboardingCompleteException;
import com.example.echo_api.exception.custom.internalserver.ClerkException;
import com.example.echo_api.persistence.model.user.User;

public interface AuthService {

    /**
     * Onboards an authenticated Clerk user by creating a local user and profile.
     * 
     * <p>
     * Synchronisation is guaranteed by updating the Clerk user's
     * {@code external_id} and {@code metadata} via the {@link Clerk} SDK after
     * persisting a local reference.
     * 
     * 
     * @throws OnboardingCompleteException If the authenticated user's Clerk ID
     *                                     already has a synchronised {@link User}
     *                                     object in the database
     * @throws ClerkException              If any interaction with the {@link Clerk}
     *                                     SDK fails for whatever reason
     */
    public void onboard();

}
