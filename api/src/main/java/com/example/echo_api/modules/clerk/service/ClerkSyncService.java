package com.example.echo_api.modules.clerk.service;

import com.example.echo_api.modules.clerk.dto.ClerkUser;
import com.example.echo_api.modules.user.entity.User;

interface ClerkSyncService {

    /**
     * Ingests an upsert (create or update) event for a Clerk user and synchronises
     * it to the local database.
     * 
     * @param user the normalised Clerk user to be synchronised
     * @return the persisted local {@link User}
     */
    User ingestUserUpserted(ClerkUser user);

    /**
     * Ingests a delete event for a Clerk user and synchronises it to the local
     * database.
     * 
     * @param clerkId the unique identifier of the Clerk user to delete
     */
    void ingestUserDeleted(String clerkId);

}
