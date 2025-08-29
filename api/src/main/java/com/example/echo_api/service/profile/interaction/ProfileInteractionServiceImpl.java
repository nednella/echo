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
import com.example.echo_api.shared.service.SessionService;

/**
 * Service implementation for managing CRUD operations between {@link Profile}
 * entities.
 */
@Service
@Transactional // TODO: remove all class-level transactional
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
        UUID source = getAuthenticatedUserId();
        UUID target = getProfileEntityById(id).getId(); // validate existence of id

        validateNoSelfAction(source, target);
        if (followRepository.existsByFollowerIdAndFollowedId(source, target)) {
            throw new AlreadyFollowingException();
        }

        followRepository.save(new Follow(source, target));
    }

    @Override
    public void unfollow(UUID id) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        followRepository.deleteByFollowerIdAndFollowedId(authenticatedUserId, id);
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
