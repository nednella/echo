package com.example.echo_api.service.relationship;

import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.BlockedException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.model.profile.Profile;

public interface RelationshipService {

    /**
     * Fetches and returns an object that resembles a relationship between two
     * profiles.
     * 
     * @param source The initiating {@link Profile}.
     * @param target The target {@link Profile}.
     * @return
     */
    public RelationshipDTO getRelationship(Profile source, Profile target);

    /**
     * Attempts to form a {@link Follow} relationship between {@code source} and
     * {@code target} profiles.
     * 
     * @param source The initiating {@link Profile}.
     * @param target The target {@link Profile}.
     * @throws BlockedException          If the {@code source} profile is blocked by
     *                                   the {@code target} profile.
     * @throws SelfActionException       If the {@code source} and {@code target}
     *                                   profiles point to the same user.
     * @throws AlreadyFollowingException If the {@code source} profile already
     *                                   follows the {@code target} profile.
     */
    public void follow(Profile source, Profile target)
        throws BlockedException, SelfActionException, AlreadyFollowingException;

    /**
     * Attempts to remove a {@link Follow} relationship between {@code source} and
     * {@code target} profiles.
     * 
     * @param source The initiating {@link Profile}.
     * @param target The target {@link Profile}.
     * @throws SelfActionException   If the {@code source} and {@code target}
     *                               profiles point to the same user.
     * @throws NotFollowingException If the {@code source} profile doesn't already
     *                               follow the {@code target} profile.
     */
    public void unfollow(Profile source, Profile target) throws SelfActionException, NotFollowingException;

    /**
     * Attempts to form a {@link Block} relationship between {@code source} and
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
     * Attempts to remove a {@link Block} relationship between {@code source} and
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

}
