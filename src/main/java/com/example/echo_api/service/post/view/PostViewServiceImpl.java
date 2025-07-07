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
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.BasePostService;
import com.example.echo_api.service.session.SessionService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Service implementation for managing read-only {@link Post} data presentation
 * operations associated with singlular post objects.
 */
@Service
public class PostViewServiceImpl extends BasePostService implements PostViewService {

    private final HttpServletRequest httpServletRequest;

    // @formatter:off
    protected PostViewServiceImpl(
        SessionService sessionService,
        PostRepository postRepository,
        HttpServletRequest httpServletRequest) {
        super(sessionService, postRepository);
        this.httpServletRequest = httpServletRequest;
    }
    // @formatter:on

    @Override
    public PostDTO getPostById(UUID id) throws ResourceNotFoundException {
        UUID authenticatedUserId = getAuthenticatedUser().getId();

        return postRepository.findPostDtoById(id, authenticatedUserId)
            .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public PageDTO<PostDTO> getPostRepliesById(UUID id, Pageable page) throws ResourceNotFoundException {
        Post post = getPostEntityById(id); // validate existence of id
        UUID authenticatedUserId = getAuthenticatedUser().getId();

        Page<PostDTO> query = postRepository.findReplyPostsById(post.getId(), authenticatedUserId, page);
        String uri = getCurrentRequestUri();

        return PageMapper.toDTO(query, uri);
    }

    /**
     * Internal method for obtaining the current HTTP request URI, to be returned as
     * part of a {@link PageDTO} response.
     * 
     * @return The current request's URI as a string.
     */
    private String getCurrentRequestUri() {
        return httpServletRequest.getRequestURI();
    }

}
