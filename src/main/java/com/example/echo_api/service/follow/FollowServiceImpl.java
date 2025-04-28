package com.example.echo_api.service.follow;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.repository.FollowRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing and validating CRD operations of
 * {@link Follow} relationships.
 * 
 * @see Follow
 * @see FollowRepository
 */
@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    @Override
    public void follow(UUID source, UUID target) throws AlreadyFollowingException {
        validateNoSelfAction(source, target);

        if (isFollowing(source, target)) {
            throw new AlreadyFollowingException();
        }

        followRepository.save(new Follow(source, target));
    }

    @Override
    public void unfollow(UUID source, UUID target) throws NotFollowingException {
        validateNoSelfAction(source, target);

        if (!isFollowing(source, target)) {
            throw new NotFollowingException();
        }

        followRepository.delete(new Follow(source, target));
    }

    @Override
    @Transactional
    public void mutualUnfollowIfExists(UUID profileId1, UUID profileId2) {
        followRepository.deleteAnyFollowIfExists(profileId1, profileId2);
    }

    /**
     * Internal method for validating that the requested action is not being
     * performed on oneself, as such an action would throw a db exception.
     * 
     * @param source The source profile id.
     * @param target The target profile id.
     * @throws SelfActionException If arguments are equal.
     */
    private void validateNoSelfAction(UUID source, UUID target) throws SelfActionException {
        if (Objects.equals(source, target)) {
            throw new SelfActionException();
        }
    }

    /**
     * Internal method to check if a one-way follow exists from the follower to the
     * followed profile.
     * 
     * @param source The id of the profile initiating the follow.
     * @param target The id of the profile being followed.
     * @return True if a unidirectional follow exists from source to target, else
     *         false.
     */
    private boolean isFollowing(UUID source, UUID target) {
        return followRepository.existsByFollowerIdAndFollowingId(source, target);
    }

}
