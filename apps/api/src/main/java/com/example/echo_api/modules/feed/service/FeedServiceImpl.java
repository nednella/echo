package com.example.echo_api.modules.feed.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.exception.ProfileErrorCode;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.shared.pagination.Paged;
import com.example.echo_api.shared.pagination.PageMapper;
import com.example.echo_api.shared.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing read-only {@link Post} data presentation
 * operations associated with paginated post objects required to build user
 * feeds.
 */
@Service
@RequiredArgsConstructor
class FeedServiceImpl implements FeedService {

    private final SessionService sessionService;
    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;
    private final HttpServletRequest httpServletRequest;

    @Override
    @Transactional(readOnly = true)
    public Paged<PostDTO> getHomeFeed(Pageable page) {
        UUID authUserId = sessionService.getAuthenticatedUserId();

        var query = postRepository.findHomepagePosts(authUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    @Transactional(readOnly = true)
    public Paged<PostDTO> getDiscoverFeed(Pageable page) {
        UUID authUserId = sessionService.getAuthenticatedUserId();

        var query = postRepository.findDiscoverPosts(authUserId, page);
        String uri = getCurrentRequestUri();
        return PageMapper.toDTO(query, uri);
    }

    @Override
    @Transactional(readOnly = true)
    public Paged<PostDTO> getProfilePosts(UUID profileId, Pageable page) {
        validateProfileExists(profileId);
        UUID authUserId = sessionService.getAuthenticatedUserId();

        var query = postRepository.findPostsByProfileId(profileId, authUserId, page);
        String uri = getCurrentRequestUri();
        return PageMapper.toDTO(query, uri);
    }

    @Override
    @Transactional(readOnly = true)
    public Paged<PostDTO> getProfileReplies(UUID profileId, Pageable page) {
        validateProfileExists(profileId);
        UUID authUserId = sessionService.getAuthenticatedUserId();

        var query = postRepository.findRepliesByProfileId(profileId, authUserId, page);
        String uri = getCurrentRequestUri();
        return PageMapper.toDTO(query, uri);
    }

    @Override
    @Transactional(readOnly = true)
    public Paged<PostDTO> getProfileLikes(UUID profileId, Pageable page) {
        validateProfileExists(profileId);
        UUID authUserId = sessionService.getAuthenticatedUserId();

        var query = postRepository.findPostsLikedByProfileId(profileId, authUserId, page);
        String uri = getCurrentRequestUri();
        return PageMapper.toDTO(query, uri);
    }

    @Override
    @Transactional(readOnly = true)
    public Paged<PostDTO> getProfileMentions(UUID profileId, Pageable page) {
        validateProfileExists(profileId);
        UUID authUserId = sessionService.getAuthenticatedUserId();

        var query = postRepository.findPostsMentioningProfileId(profileId, authUserId, page);
        String uri = getCurrentRequestUri();
        return PageMapper.toDTO(query, uri);

    }

    /**
     * Throws if a {@link Profile} does not exist with the given {@code id}.
     * 
     * @param id
     * @throws ApplicationException if no profile with the given id exists
     * 
     */
    private void validateProfileExists(UUID id) {
        if (!profileRepository.existsById(id)) {
            throw ProfileErrorCode.ID_NOT_FOUND.buildAsException(id);
        }
    }

    /**
     * Fetch the current HTTP request URI.
     * 
     * @return the current request URI as a string
     */
    private String getCurrentRequestUri() {
        return httpServletRequest.getRequestURI();
    }

}
