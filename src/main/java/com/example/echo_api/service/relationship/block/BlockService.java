package com.example.echo_api.service.relationship.block;

import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.persistence.model.profile.Profile;

public interface BlockService {

    /**
     * Creates a {@link Block} relationship between {@code source} and
     * {@code target} profiles.
     * 
     * @param source The initiating {@link Profile}.
     * @param target The target {@link Profile}.
     * @throws SelfActionException      If the {@code source} and {@code target}
     *                                  profiles point to the same user.
     * @throws AlreadyBlockingException If the {@code source} profile already blocks
     *                                  the {@code target} profile.
     */
    public void block(Profile source, Profile target) throws SelfActionException, AlreadyBlockingException;

    /**
     * Deletes a {@link Block} relationship between {@code source} and
     * {@code target} profiles.
     * 
     * @param source The initiating {@link Profile}.
     * @param target The target {@link Profile}.
     * @throws SelfActionException  If the {@code source} and {@code target}
     *                              profiles point to the same user.
     * @throws NotBlockingException If the {@code source} profile doesn't already
     *                              block the {@code target} profile.
     */
    public void unblock(Profile source, Profile target) throws SelfActionException, NotBlockingException;

    /**
     * Checks if a {@link Block} relationship exists between {@code source} and
     * {@code target} profiles.
     * 
     * @param source The initiating {@link Profile}.
     * @param target The target {@link Profile}.
     * @return
     */
    public boolean isBlocking(Profile source, Profile target);

    /**
     * Checks if a {@link Block} relationship exists between {@code target} and
     * {@code source} profiles.
     * 
     * @param source The initiating {@link Profile}.
     * @param target The target {@link Profile}.
     * @return
     */
    public boolean isBlockedBy(Profile source, Profile target);

}
