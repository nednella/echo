package com.example.echo_api.service.post.view;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.auth.session.SessionService;
import com.example.echo_api.service.post.BasePostService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Service implementation for managing read-only {@link Post} data presentation
 * operations associated with singular and paginated post objects.
 */
@Service
public class PostViewServiceImpl extends BasePostService implements PostViewService {

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
    public PostDTO getPostById(UUID id) throws ResourceNotFoundException {
        UUID authenticatedUserId = getAuthenticatedUserId();

        return postRepository.findPostDtoById(id, authenticatedUserId)
            .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public PageDTO<PostDTO> getRepliesById(UUID id, Pageable page) throws ResourceNotFoundException {
        UUID authenticatedUserId = getAuthenticatedUserId();
        UUID postId = getPostEntityById(id).getId(); // validate existence of id

        Page<PostDTO> query = postRepository.findRepliesById(postId, authenticatedUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getPostsByAuthorId(UUID id, Pageable page) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        UUID authorId = getProfileEntityById(id).getId(); // validate existence of author

        Page<PostDTO> query = postRepository.findPostsByProfileId(authorId, authenticatedUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getRepliesByAuthorId(UUID id, Pageable page) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        UUID authorId = getProfileEntityById(id).getId(); // validate existence of author

        Page<PostDTO> query = postRepository.findRepliesByProfileId(authorId, authenticatedUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getLikesByAuthorId(UUID id, Pageable page) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        UUID authorId = getProfileEntityById(id).getId(); // validate existence of author

        Page<PostDTO> query = postRepository.findPostsLikedByProfileId(authorId, authenticatedUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getMentionsOfAuthorId(UUID id, Pageable page) {
        UUID authenticatedUserId = getAuthenticatedUserId();
        UUID authorId = getProfileEntityById(id).getId(); // validate existence of author

        Page<PostDTO> query = postRepository.findPostsMentioningProfileId(authorId, authenticatedUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getHomepagePosts(Pageable page) {
        UUID authenticatedUserId = getAuthenticatedUserId();

        Page<PostDTO> query = postRepository.findHomepagePosts(authenticatedUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    @Override
    public PageDTO<PostDTO> getDiscoverPosts(Pageable page) {
        UUID authenticatedUserId = getAuthenticatedUserId();

        Page<PostDTO> query = postRepository.findDiscoverPosts(authenticatedUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    /**
     * Private method for obtaining a {@link Profile} via {@code id} from
     * {@link ProfileRepository}.
     * 
     * @param id The id of the profile.
     * @return The {@link Profile} entity.
     * @throws ResourceNotFoundException If no profile by that id exists.
     */
    private Profile getProfileEntityById(UUID id) throws ResourceNotFoundException {
        return profileRepository.findById(id)
            .orElseThrow(ResourceNotFoundException::new);
    }

    /**
     * Private method for obtaining the current HTTP request URI, to be returned as
     * part of a {@link PageDTO} response.
     * 
     * @return The current request's URI as a string.
     */
    private String getCurrentRequestUri() {
        return httpServletRequest.getRequestURI();
    }

}
