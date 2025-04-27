package com.example.echo_api.service.follow;

import java.util.UUID;

import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;

public interface FollowService {

    /**
     * Creates a {@link Follow} relationship between {@code source} and
     * {@code target} profiles. The order of the supplied arguments does matter.
     * 
     * @param source The id of the initiating {@link Profile}.
     * @param target The id of the target {@link Profile}.
     * @throws AlreadyFollowingException If the {@code source} profile already
     *                                   follows the {@code target} profile.
     */
    public void follow(UUID source, UUID target) throws AlreadyFollowingException;

    /**
     * Deletes a {@link Follow} relationship between {@code source} and
     * {@code target} profiles. The order of the supplied arguments does matter.
     * 
     * @param source The id of the initiating {@link Profile}.
     * @param target The id of the target {@link Profile}.
     * @throws NotFollowingException If the {@code source} profile doesn't already
     *                               follow the {@code target} profile.
     */
    public void unfollow(UUID source, UUID target) throws NotFollowingException;

    /**
     * Delete any follow relationship that exists between two profiles in either
     * direction (unilateral or mutual).
     * 
     * @param profileId1 The id of the first profile.
     * @param profileId2 The id of the second profile.
     */
    public void mutualUnfollowIfExists(UUID profileId1, UUID profileId2);

}
