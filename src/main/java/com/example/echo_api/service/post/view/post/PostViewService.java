package com.example.echo_api.service.post.view.post;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;

public interface PostViewService {

    /**
     * Fetch a {@link PostDTO} by {@code id}.
     * 
     * @param id The id of the post to query.
     * @return A {@link PostDTO} resembling the queried post.
     * @throws ResourceNotFoundException If no post by that id exists.
     */
    public PostDTO getPostById(UUID id);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for the posts that are in reply to
     * the supplied post {@code id}.
     * 
     * @param id   The id of the post to query.
     * @param page The {@link Pageable} containing the pagination parameters.
     * @return A {@link PageDTO} of {@link PostDTO} for matches, otherwise empty.
     * @throws ResourceNotFoundException If no post by that id exists.
     */
    public PageDTO<PostDTO> getPostRepliesById(UUID id, Pageable page);

}
