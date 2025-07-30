package com.example.echo_api.service.profile.interaction;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.conflict.AlreadyFollowingException;
import com.example.echo_api.exception.custom.conflict.SelfActionException;
import com.example.echo_api.persistence.model.follow.Follow;
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

    // @formatter:off
    public ProfileInteractionServiceImpl(
        SessionService sessionService,
        ProfileRepository profileRepository,
        FollowRepository followRepository
    ) {
        super(sessionService, profileRepository);
        this.followRepository = followRepository;
    }
    // @formatter:on

    @Override
    public void follow(UUID id) {
        UUID target = getProfileEntityById(id).getId(); // validate existence of id
        UUID source = getAuthenticatedUserId();

        validateNoSelfAction(source, target);
        if (followRepository.existsByFollowerIdAndFollowedId(source, target)) {
            throw new AlreadyFollowingException();
        }

        followRepository.save(new Follow(source, target));
    }

    @Override // TODO: refactor to idempotent
    public void unfollow(UUID id) {
        UUID source = getAuthenticatedUserId();
        UUID target = getProfileEntityById(id).getId();

        validateNoSelfAction(source, target);
        followRepository.deleteByFollowerIdAndFollowedId(source, target);
    }

    /**
     * Validate that the action is not being performed on self.
     * 
     * @param source The source profile id.
     * @param target The target profile id.
     * @throws SelfActionException If the the source and target ids match.
     */
    private void validateNoSelfAction(UUID source, UUID target) {
        if (Objects.equals(source, target)) {
            throw new SelfActionException();
        }
    }

}
