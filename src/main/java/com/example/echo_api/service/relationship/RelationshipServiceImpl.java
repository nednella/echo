package com.example.echo_api.service.relationship;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.BlockedException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.service.relationship.block.BlockService;
import com.example.echo_api.service.relationship.follow.FollowService;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing relationships between {@link Profile}
 * pairs.
 * 
 * <p>
 * The service exists to alleviate circular dependencies that would form between
 * relationship CRUD services.
 * 
 * @see Profile
 * @see FollowService
 * @see BlockService
 */
@Service
@RequiredArgsConstructor
public class RelationshipServiceImpl implements RelationshipService {

    private final FollowService followService;
    private final BlockService blockService;

    @Override
    public RelationshipDTO getRelationship(Profile source, Profile target) {
        boolean isFollowing = followService.isFollowing(source, target);
        boolean isFollowedBy = followService.isFollowedBy(source, target);
        boolean isBlocking = blockService.isBlocking(source, target);
        boolean isBlockedBy = blockService.isBlockedBy(source, target);
        return new RelationshipDTO(isFollowing, isFollowedBy, isBlocking, isBlockedBy);
    }

    // @formatter:off
    @Override
    public void follow(Profile source, Profile target)
        throws BlockedException, SelfActionException, AlreadyFollowingException
    {
        if (blockService.isBlockedBy(source, target)) {
            throw new BlockedException();
        }
        followService.follow(source, target);
    }
    // @formatter:on

    @Override
    public void unfollow(Profile source, Profile target) throws SelfActionException, NotFollowingException {
        followService.unfollow(source, target);
    }

    @Override
    @Transactional
    public void block(Profile source, Profile target) throws SelfActionException, AlreadyBlockingException {
        handleBlock(source, target);
        blockService.block(source, target);
    }

    @Override
    public void unblock(Profile source, Profile target) throws SelfActionException, NotBlockingException {
        blockService.unblock(source, target);
    }

    /**
     * Internal method for removing any existing relationships between profiles when
     * {@code source} requests to block {@code target}.
     * 
     * @param source The source {@link Profile}.
     * @param target The target {@link Profile}.
     */
    private void handleBlock(Profile source, Profile target) {
        if (followService.isFollowing(source, target)) {
            followService.unfollow(source, target);
        }
        if (followService.isFollowedBy(source, target)) {
            followService.unfollow(target, source);
        }
    }

}
