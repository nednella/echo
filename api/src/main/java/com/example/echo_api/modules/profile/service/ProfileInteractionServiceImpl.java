package com.example.echo_api.modules.profile.service;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.profile.entity.ProfileFollow;
import com.example.echo_api.modules.profile.exception.ProfileErrorCode;
import com.example.echo_api.modules.profile.repository.ProfileFollowRepository;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.shared.service.SessionService;

/**
 * Service implementation for managing CRUD operations between {@link Profile}
 * entities.
 */
@Service
class ProfileInteractionServiceImpl extends BaseProfileService implements ProfileInteractionService {

    private final ProfileFollowRepository profileFollowRepository;

    // @formatter:off
    public ProfileInteractionServiceImpl(
        SessionService sessionService,
        ProfileRepository profileRepository,
        ProfileFollowRepository profileFollowRepository
    ) {
        super(sessionService, profileRepository);
        this.profileFollowRepository = profileFollowRepository;
    }
    // @formatter:on

    @Override
    @Transactional
    public void follow(UUID id) {
        validateProfileExists(id);

        UUID authUserId = getAuthenticatedUserId();
        validateNoSelfAction(authUserId, id);
        validateFollowDoesNotExist(authUserId, id);

        profileFollowRepository.save(new ProfileFollow(authUserId, id));
    }

    @Override
    @Transactional
    public void unfollow(UUID id) {
        UUID authUserId = getAuthenticatedUserId();
        profileFollowRepository.deleteByFollowerIdAndFollowedId(authUserId, id);
    }

    /**
     * Throws if {@code source} and {@code target} are equal.
     * 
     * @param source
     * @param target
     * @throws ApplicationException if the the source and target ids match
     */
    private void validateNoSelfAction(UUID source, UUID target) {
        if (Objects.equals(source, target)) {
            throw ProfileErrorCode.SELF_ACTION.buildAsException();
        }
    }

    /**
     * Throws if a {@link ProfileFollow} exists with the given {@code followerId}
     * and {@code followedId}.
     * 
     * @param followerId
     * @param followedId
     * @throws ApplicationException if a follow already exists
     */
    private void validateFollowDoesNotExist(UUID followerId, UUID followedId) {
        if (profileFollowRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {
            throw ProfileErrorCode.ALREADY_FOLLOWING.buildAsException(followedId);
        }
    }

}
