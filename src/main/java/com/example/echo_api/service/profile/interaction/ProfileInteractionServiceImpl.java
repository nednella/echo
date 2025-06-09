package com.example.echo_api.service.profile.interaction;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.conflict.AlreadyBlockingException;
import com.example.echo_api.exception.custom.conflict.AlreadyFollowingException;
import com.example.echo_api.exception.custom.conflict.SelfActionException;
import com.example.echo_api.exception.custom.forbidden.BlockedException;
import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.repository.BlockRepository;
import com.example.echo_api.persistence.repository.FollowRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.profile.BaseProfileService;
import com.example.echo_api.service.session.SessionService;

/**
 * Service implementation for managing CRUD operations between {@link Profile}
 * entities.
 */
@Service
@Transactional
public class ProfileInteractionServiceImpl extends BaseProfileService implements ProfileInteractionService {

    private final FollowRepository followRepository;
    private final BlockRepository blockRepository;

    // @formatter:off
    public ProfileInteractionServiceImpl(
        SessionService sessionService,
        ProfileRepository profileRepository,
        FollowRepository followRepository,
        BlockRepository blockRepository
    ) {
        super(sessionService, profileRepository);
        this.followRepository = followRepository;
        this.blockRepository = blockRepository;
    }
    // @formatter:on

    @Override
    public void follow(String username) {
        UUID source = getAuthenticatedUser().getId();
        UUID target = getProfileEntityByUsername(username).getId();

        validateSelfAction(source, target);
        validateNoBlock(source, target);
        if (followRepository.existsByFollowerIdAndFollowedId(source, target)) {
            throw new AlreadyFollowingException();
        }

        followRepository.save(new Follow(source, target));
    }

    @Override
    public void unfollow(String username) {
        UUID source = getAuthenticatedUser().getId();
        UUID target = getProfileEntityByUsername(username).getId();

        validateSelfAction(source, target);
        followRepository.deleteByFollowerIdAndFollowedId(source, target);
    }

    @Override
    public void block(String username) {
        UUID source = getAuthenticatedUser().getId();
        UUID target = getProfileEntityByUsername(username).getId();

        validateSelfAction(source, target);
        if (blockRepository.existsByBlockerIdAndBlockedId(source, target)) {
            throw new AlreadyBlockingException();
        }

        followRepository.deleteAnyFollowIfExists(source, target);
        blockRepository.save(new Block(source, target));
    }

    @Override
    public void unblock(String username) {
        UUID source = getAuthenticatedUser().getId();
        UUID target = getProfileEntityByUsername(username).getId();

        validateSelfAction(source, target);
        blockRepository.deleteByBlockerIdAndBlockedId(source, target);
    }

    /**
     * Validate that the action is not being performed on self.
     * 
     * @param source The source profile id.
     * @param target The target profile id.
     * @throws SelfActionException If the the source and target ids match.
     */
    private void validateSelfAction(UUID source, UUID target) {
        if (Objects.equals(source, target)) {
            throw new SelfActionException();
        }
    }

    /**
     * Validate that the authenticated user has permission to perform the action on
     * the target user.
     * 
     * @param source The source profile id.
     * @param target The target profile id.
     * @throws BlockedException
     */
    private void validateNoBlock(UUID source, UUID target) {
        if (blockRepository.existsAnyBlockBetween(source, target)) {
            throw new BlockedException();
        }
    }

}
