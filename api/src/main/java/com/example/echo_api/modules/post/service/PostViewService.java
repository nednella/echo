package com.example.echo_api.modules.post.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.shared.pagination.PageDTO;

public interface PostViewService {

    /**
     * Fetch a {@link PostDTO} by {@code id}.
     * 
     * @param id the post id
     * @return a {@link PostDTO} resembling the queried post
     * @throws ApplicationException if no post with the given id exists
     */
    PostDTO getPostById(UUID id);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for the posts that are in reply to
     * the given post {@code id}.
     * 
     * @param id   the post id
     * @param page pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO}; empty if no matches
     * @throws ApplicationException if no post with the given id exists
     */
    PageDTO<PostDTO> getRepliesByPostId(UUID id, Pageable page);
    // TODO: refactor to getConversationByPostId

}
