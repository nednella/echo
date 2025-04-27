package com.example.echo_api.service.profile.interaction;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.echo_api.exception.custom.account.IdNotFoundException;
import com.example.echo_api.exception.custom.relationship.BlockedException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.block.BlockService;
import com.example.echo_api.service.follow.FollowService;
import com.example.echo_api.service.profile.BaseProfileService;
import com.example.echo_api.service.session.SessionService;

/**
 * Service implementation for managing CRUD operations between {@link Profile}
 * entities.
 */
@Service
public class ProfileInteractionServiceImpl extends BaseProfileService implements ProfileInteractionService {

    private final FollowService followService;
    private final BlockService blockService;

    // @formatter:off
    public ProfileInteractionServiceImpl(
        SessionService sessionService,
        ProfileRepository profileRepository,
        FollowService followService,
        BlockService blockService
    ) {
        super(sessionService, profileRepository);
        this.followService = followService;
        this.blockService = blockService;
    }
    // @formatter:on

    @Override
    public void follow(UUID id) throws IdNotFoundException {
        Profile target = getProfileById(id); // validate existence of id
        Account source = getAuthenticatedUser();
        validateNotSelfAction(source.getId(), target.getId());
        validateNoBlock(source.getId(), target.getId()); // validate the interaction is allowed

        followService.follow(source.getId(), target.getId());
    }

    @Override
    public void unfollow(UUID id) throws IdNotFoundException {
        Profile target = getProfileById(id); // validate existence of id
        Account source = getAuthenticatedUser();
        validateNotSelfAction(source.getId(), target.getId());

        followService.unfollow(source.getId(), target.getId());
    }

    @Override
    public void block(UUID id) throws IdNotFoundException {
        Profile target = getProfileById(id); // validate existence of id
        Account source = getAuthenticatedUser();
        validateNotSelfAction(source.getId(), target.getId());

        followService.mutualUnfollowIfExists(source.getId(), target.getId()); // remove follows before blocking
        blockService.block(source.getId(), target.getId());
    }

    @Override
    public void unblock(UUID id) throws IdNotFoundException {
        Profile target = getProfileById(id); // validate existence of id
        Account source = getAuthenticatedUser();
        validateNotSelfAction(source.getId(), target.getId());

        blockService.unblock(source.getId(), target.getId());
    }

    /**
     * Internal method for validating that the requested action is not being
     * performed on oneself, as such an action would throw a db exception.
     * 
     * @param source The source profile id.
     * @param target The target profile id.
     * @throws SelfActionException If arguments are equal.
     */
    private void validateNotSelfAction(UUID source, UUID target) throws SelfActionException {
        if (Objects.equals(source, target)) {
            throw new SelfActionException();
        }
    }

    /**
     * Internal method for validating that the requested action is allowed, i.e.,
     * there is no block relationship established between the supplied profiles.
     * 
     * @param source The source profile id.
     * @param target The target profile id.
     * @throws BlockedException If either party is blocking the other.
     */
    private void validateNoBlock(UUID source, UUID target) throws BlockedException {
        if (blockService.existsAnyBlockBetween(source, target)) {
            throw new BlockedException();
        }
    }

}
