package com.example.echo_api.service.relationship.follow;

import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;

public interface FollowService {

    /**
     * Creates a {@link Follow} relationship between {@code source} and
     * {@code target} profiles.
     * 
     * @param source The initiating {@link Profile}.
     * @param target The target {@link Profile}.
     * @throws SelfActionException       If the {@code source} and {@code target}
     *                                   profiles point to the same user.
     * @throws AlreadyFollowingException If the {@code source} profile already
     *                                   follows the {@code target} profile.
     */
    public void follow(Profile source, Profile target) throws SelfActionException, AlreadyFollowingException;

    /**
     * Deletes a {@link Follow} relationship between {@code source} and
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
     * Checks if a {@link Follow} relationship exists between {@code source} and
     * {@code target} profiles.
     * 
     * @param source The initiating {@link Profile}.
     * @param target The target {@link Profile}.
     * @return
     */
    public boolean isFollowing(Profile source, Profile target);

    /**
     * Checks if a {@link Follow} relationship exists between {@code target} and
     * {@code source} profiles.
     * 
     * @param source The initiating {@link Profile}.
     * @param target The target {@link Profile}.
     * @return
     */
    public boolean isFollowedBy(Profile source, Profile target);

}
