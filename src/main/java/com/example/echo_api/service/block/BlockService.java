package com.example.echo_api.service.block;

import java.util.UUID;

import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.model.profile.Profile;

public interface BlockService {

    /**
     * Creates a {@link Block} relationship between {@code source} and
     * {@code target} profiles. The order of the supplied arguments does matter.
     * 
     * @param source The id of the initiating {@link Profile}.
     * @param target The id of the target {@link Profile}.
     * @throws AlreadyBlockingException If the {@code source} profile already blocks
     *                                  the {@code target} profile.
     */
    public void block(UUID source, UUID target) throws AlreadyBlockingException;

    /**
     * Deletes a {@link Block} relationship between {@code source} and
     * {@code target} profiles. The order of the supplied arguments does matter.
     * 
     * @param source The id of the initiating {@link Profile}.
     * @param target The id of the target {@link Profile}.
     * @throws NotBlockingException If the {@code source} profile doesn't already
     *                              block the {@code target} profile.
     */
    public void unblock(UUID source, UUID target) throws NotBlockingException;

    /**
     * Check if any block relationship exists between two profiles in either
     * direction (unilateral or mutual).
     * 
     * @param profileId1 The id of the first profile.
     * @param profileId2 The id of the second profile.
     * @return True if any block exists between the two profiles.
     */
    public boolean existsAnyBlockBetween(UUID profileId1, UUID profileId2);

}
