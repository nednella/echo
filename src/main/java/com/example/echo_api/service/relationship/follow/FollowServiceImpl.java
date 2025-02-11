package com.example.echo_api.service.relationship.follow;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.FollowRepository;
import com.example.echo_api.service.metrics.MetricsService;

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

    private final MetricsService metricsService;

    private final FollowRepository followRepository;

    @Override
    @Transactional
    public void follow(Profile source, Profile target) throws SelfActionException, AlreadyFollowingException {
        if (isSelfAction(source, target)) {
            throw new SelfActionException(ErrorMessageConfig.SELF_FOLLOW);
        }
        if (isFollowing(source, target)) {
            throw new AlreadyFollowingException();
        }

        metricsService.incrementFollowing(source);
        metricsService.incrementFollowers(target);
        followRepository.save(new Follow(source, target));
    }

    @Override
    @Transactional
    public void unfollow(Profile source, Profile target) throws SelfActionException, NotFollowingException {
        if (isSelfAction(source, target)) {
            throw new SelfActionException(ErrorMessageConfig.SELF_UNFOLLOW);
        }
        if (!isFollowing(source, target)) {
            throw new NotFollowingException();
        }

        metricsService.decrementFollowing(source);
        metricsService.decrementFollowers(target);
        followRepository.delete(new Follow(source, target));
    }

    @Override
    public boolean isFollowing(Profile source, Profile target) {
        return followRepository.existsByFollowerIdAndFollowingId(source.getProfileId(), target.getProfileId());
    }

    @Override
    public boolean isFollowedBy(Profile source, Profile target) {
        return isFollowing(target, source);
    }

    /**
     * Internal method for checking if {@link Profile} pairs match.
     * 
     * @param source The source {@link Profile}.
     * @param target The target {@link Profile}.
     * @return Boolean indicating whether the profiles are a match.
     */
    private boolean isSelfAction(Profile source, Profile target) {
        return Objects.equals(source.getProfileId(), target.getProfileId());
    }

}
