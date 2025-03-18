package com.example.echo_api.service.profile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.exception.custom.cloudinary.CloudinaryException;
import com.example.echo_api.exception.custom.image.ImageException;
import com.example.echo_api.exception.custom.username.UsernameNotFoundException;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.persistence.mapper.ProfileMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.image.Image;
import com.example.echo_api.persistence.model.image.ImageType;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.file.FileService;
import com.example.echo_api.service.metrics.profile.ProfileMetricsService;
import com.example.echo_api.service.relationship.RelationshipService;
import com.example.echo_api.service.session.SessionService;

import jakarta.servlet.http.HttpServletRequest;
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
    private final FileService fileService;

    private final ProfileRepository profileRepository;

    private final HttpServletRequest httpRequest;

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
    @Transactional
    public void updateMeAvatar(MultipartFile image) throws ImageException, CloudinaryException {
        Profile me = findMe();
        if (me.getAvatar() != null) {
            fileService.deleteImage(me.getAvatar().getId());
        }

        Image avatar = fileService.createImage(image, ImageType.AVATAR);
        me.setAvatar(avatar);
        profileRepository.save(me);
    }

    @Override
    @Transactional
    public void deleteMeAvatar() throws ImageException, CloudinaryException {
        Profile me = findMe();
        if (me.getAvatar() == null) {
            return;
        }

        fileService.deleteImage(me.getAvatar().getId());
        me.setAvatar(null);
        profileRepository.save(me);
    }

    @Override
    @Transactional
    public void updateMeBanner(MultipartFile image) throws ImageException, CloudinaryException {
        Profile me = findMe();
        if (me.getBanner() != null) {
            fileService.deleteImage(me.getBanner().getId());
        }

        Image avatar = fileService.createImage(image, ImageType.BANNER);
        me.setBanner(avatar);
        profileRepository.save(me);
    }

    @Override
    @Transactional
    public void deleteMeBanner() throws ImageException, CloudinaryException {
        Profile me = findMe();
        if (me.getBanner() == null) {
            return;
        }

        fileService.deleteImage(me.getBanner().getId());
        me.setBanner(null);
        profileRepository.save(me);
    }

    @Override
    public PageDTO<ProfileDTO> getFollowers(String username, Pageable page) throws UsernameNotFoundException {
        Profile me = findMe();
        Profile target = findByUsername(username);
        Page<Profile> followersPage = profileRepository.findAllFollowersById(target.getId(), page);

        Page<ProfileDTO> followersDtoPage = followersPage.map(profile -> ProfileMapper.toDTO(
            profile,
            profileMetricsService.getMetrics(profile),
            relationshipService.getRelationship(me, profile)));

        String uri = getCurrentRequestUri();
        return PageMapper.toDTO(followersDtoPage, uri);
    }

    @Override
    public PageDTO<ProfileDTO> getFollowing(String username, Pageable page) throws UsernameNotFoundException {
        Profile me = findMe();
        Profile target = findByUsername(username);
        Page<Profile> followingPage = profileRepository.findAllFollowingById(target.getId(), page);

        Page<ProfileDTO> followingDtoPage = followingPage.map(profile -> ProfileMapper.toDTO(
            profile,
            profileMetricsService.getMetrics(profile),
            relationshipService.getRelationship(me, profile)));

        String uri = getCurrentRequestUri();
        return PageMapper.toDTO(followingDtoPage, uri);
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
     * Internal method for obtaining a {@link Profile} associated to the
     * authenticated user.
     * 
     * @return The found {@link Profile}.
     */
    private Profile findMe() {
        Account me = sessionService.getAuthenticatedUser();
        return findByUsername(me.getUsername());
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
     * Internal method for obtaining the current HTTP request URI, to be returned as
     * part of a {@link PageDTO} response.
     * 
     * @return The current request's URI as a string.
     */
    private String getCurrentRequestUri() {
        return httpRequest.getRequestURI();
    }

}
