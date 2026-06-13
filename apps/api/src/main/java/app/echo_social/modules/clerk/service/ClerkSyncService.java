package app.echo_social.modules.clerk.service;

import app.echo_social.modules.clerk.dto.ClerkUser;
import app.echo_social.modules.user.entity.User;

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
