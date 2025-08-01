package com.example.echo_api.service.session;

import java.util.UUID;

public interface SessionService {

    /**
     * Retrieves the authenticated user Echo API UUID from the authenticated Clerk
     * token.
     * 
     * @return The UUID of the authenticated user.
     */
    public UUID getAuthenticatedUserId();

    /**
     * Retrieves the authenticated user Clerk ID from the authenticated Clerk token.
     * 
     * @return The Clerk ID of the authenticated user.
     */
    public String getAuthenticatedUserClerkId();

}
