package com.example.echo_api.modules.profile.service;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.profile.entity.Follow;
import com.example.echo_api.modules.profile.exception.ProfileErrorCode;
import com.example.echo_api.modules.profile.repository.FollowRepository;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.shared.service.SessionService;

/**
 * Service implementation for managing CRUD operations between {@link Profile}
 * entities.
 */
@Service
@Transactional // TODO: remove all class-level transactional
class ProfileInteractionServiceImpl extends BaseProfileService implements ProfileInteractionService {

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
            throw ProfileErrorCode.ALREADY_FOLLOWING.buildAsException(target);
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
     * @param source the source profile id
     * @param target the target profile id
     * @throws ApplicationException if the the source and target ids match
     */
    private void validateNoSelfAction(UUID source, UUID target) {
        if (Objects.equals(source, target)) {
            throw ProfileErrorCode.SELF_ACTION.buildAsException();
        }
    }

}
