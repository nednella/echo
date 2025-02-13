package com.example.echo_api.service.profile;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.custom.username.UsernameNotFoundException;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.mapper.ProfileMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.metrics.profile.ProfileMetricsService;
import com.example.echo_api.service.relationship.RelationshipService;
import com.example.echo_api.service.session.SessionService;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing CRUD operations of a {@link Profile}.
 * 
 * @see SessionService
 * @see ProfileRepository
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final SessionService sessionService;
    private final ProfileMetricsService profileMetricsService;
    private final RelationshipService relationshipService;

    private final ProfileRepository profileRepository;

    @Override
    public ProfileDTO getByUsername(String username) throws UsernameNotFoundException {
        Profile me = findMe();
        Profile target = findByUsername(username);
        MetricsDTO metrics = profileMetricsService.getMetrics(target);
        RelationshipDTO relationship = relationshipService.getRelationship(me, target);
        return ProfileMapper.toDTO(target, metrics, relationship);
    }

    @Override
    public ProfileDTO getMe() {
        Profile me = findMe();
        MetricsDTO metrics = profileMetricsService.getMetrics(me);
        return ProfileMapper.toDTO(me, metrics, null);
    }

    @Override
    public void updateMeProfile(UpdateProfileDTO request) {
        Profile me = findMe();
        ProfileMapper.updateProfile(request, me);
        profileRepository.save(me);
    }

    @Override
    public List<ProfileDTO> getFollowers(String username) throws UsernameNotFoundException {
        Profile me = findMe();
        Profile target = findByUsername(username);
        List<Profile> followersList = profileRepository.findAllFollowersById(target.getProfileId());

        return followersList.stream()
            .map(follower -> ProfileMapper.toDTO(
                follower,
                profileMetricsService.getMetrics(follower),
                relationshipService.getRelationship(me, follower)))
            .toList();
    }

    @Override
    public List<ProfileDTO> getFollowing(String username) throws UsernameNotFoundException {
        Profile me = findMe();
        Profile target = findByUsername(username);
        List<Profile> followingList = profileRepository.findAllFollowingById(target.getProfileId());

        return followingList.stream()
            .map(following -> ProfileMapper.toDTO(
                following,
                profileMetricsService.getMetrics(following),
                relationshipService.getRelationship(me, following)))
            .toList();
    }

    @Override
    @Transactional
    public void follow(String username) throws UsernameNotFoundException {
        Profile me = findMe();
        Profile target = findByUsername(username);
        relationshipService.follow(me, target);
    }

    @Override
    @Transactional
    public void unfollow(String username) throws UsernameNotFoundException {
        Profile me = findMe();
        Profile target = findByUsername(username);
        relationshipService.unfollow(me, target);
    }

    @Override
    public void block(String username) throws UsernameNotFoundException {
        Profile me = findMe();
        Profile target = findByUsername(username);
        relationshipService.block(me, target);
    }

    @Override
    public void unblock(String username) throws UsernameNotFoundException {
        Profile me = findMe();
        Profile target = findByUsername(username);
        relationshipService.unblock(me, target);
    }

    /**
     * Internal method for obtaining a {@link Profile} via {@code username} from
     * {@link ProfileRepository}.
     * 
     * @param username The username to search within the repository.
     * @return The found {@link Profile}.
     * @throws UsernameNotFoundException If no profile by that username exists.
     */
    private Profile findByUsername(String username) throws UsernameNotFoundException {
        return profileRepository.findByUsername(username)
            .orElseThrow(UsernameNotFoundException::new);
    }

    /**
     * Internal method for obtaining a {@link Profile} associated to the
     * authenticated user.
     * 
     * @return The found {@link Profile}.
     */
    private Profile findMe() {
        Account me = sessionService.getAuthenticatedUser();
        return findByUsername(me.getUsername());
    }

}
