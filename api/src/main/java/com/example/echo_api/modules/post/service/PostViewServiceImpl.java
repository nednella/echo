package com.example.echo_api.modules.post.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.exception.PostErrorCode;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.exception.ProfileErrorCode;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.shared.pagination.PageDTO;
import com.example.echo_api.shared.pagination.PageMapper;
import com.example.echo_api.shared.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Service implementation for managing read-only {@link Post} data presentation
 * operations associated with singular and paginated post objects.
 */
@Service
class PostViewServiceImpl extends BasePostService implements PostViewService {

    private final ProfileRepository profileRepository;

    private final HttpServletRequest httpServletRequest;

    // @formatter:off
    protected PostViewServiceImpl(
        SessionService sessionService,
        PostRepository postRepository,
        ProfileRepository profileRepository,
        HttpServletRequest httpServletRequest) {
        super(sessionService, postRepository);
        this.profileRepository = profileRepository;
        this.httpServletRequest = httpServletRequest;
    }
    // @formatter:on

    @Override
    public PostDTO getPostById(UUID id) {
        UUID authUserId = getAuthenticatedUserId();

        return postRepository.findPostDtoById(id, authUserId)
            .orElseThrow(() -> PostErrorCode.ID_NOT_FOUND.buildAsException(id));
    }

    @Override
    public PageDTO<PostDTO> getRepliesByPostId(UUID id, Pageable page) {
        validatePostExists(id);
        UUID authUserId = getAuthenticatedUserId();

        Page<PostDTO> query = postRepository.findRepliesById(id, authUserId, page);
        String uri = getCurrentRequestUri();
        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getPostsByAuthorId(UUID id, Pageable page) {
        UUID authUserId = getAuthenticatedUserId();
        UUID authorId = getProfileEntityById(id).getId(); // validate existence of author

        Page<PostDTO> query = postRepository.findPostsByProfileId(authorId, authUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getRepliesByAuthorId(UUID id, Pageable page) {
        UUID authUserId = getAuthenticatedUserId();
        UUID authorId = getProfileEntityById(id).getId(); // validate existence of author

        Page<PostDTO> query = postRepository.findRepliesByProfileId(authorId, authUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getLikesByAuthorId(UUID id, Pageable page) {
        UUID authUserId = getAuthenticatedUserId();
        UUID authorId = getProfileEntityById(id).getId(); // validate existence of author

        Page<PostDTO> query = postRepository.findPostsLikedByProfileId(authorId, authUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getMentionsOfProfileId(UUID id, Pageable page) {
        UUID authUserId = getAuthenticatedUserId();
        UUID authorId = getProfileEntityById(id).getId(); // validate existence of author

        Page<PostDTO> query = postRepository.findPostsMentioningProfileId(authorId, authUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getHomepagePosts(Pageable page) {
        UUID authUserId = getAuthenticatedUserId();

        Page<PostDTO> query = postRepository.findHomepagePosts(authUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getDiscoverPosts(Pageable page) {
        UUID authUserId = getAuthenticatedUserId();

        Page<PostDTO> query = postRepository.findDiscoverPosts(authUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    /**
     * Fetch a profile by {@code id} from {@link ProfileRepository}.
     * 
     * @param id the profile id
     * @return the {@link Profile} entity
     * @throws ApplicationException if no profile with the given id exists
     */
    protected Profile getProfileEntityById(UUID id) {
        return profileRepository.findById(id)
            .orElseThrow(() -> ProfileErrorCode.ID_NOT_FOUND.buildAsException(id));
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
