package com.example.echo_api.modules.post.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.modules.post.dto.PostDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;

public interface PostViewService {

    /**
     * Fetch a {@link PostDTO} by {@code id}.
     * 
     * @param id the id of the post to query
     * @return a {@link PostDTO} resembling the queried post
     * @throws ResourceNotFoundException if no post by that id exists
     */
    public PostDTO getPostById(UUID id);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for the posts that are in reply to
     * the supplied post {@code id}.
     * 
     * @param id   the id of the post to query
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     * @throws ResourceNotFoundException if no post by that id exists
     */
    public PageDTO<PostDTO> getRepliesById(UUID id, Pageable page); // TODO: refactor to getConversationByPostId

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts from the user with the
     * supplied profile {@code id}, that are root-level posts (no parent).
     * 
     * @param id   the id of the profile (author) to query
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     * @throws ResourceNotFoundException if no profile by that id exists
     */
    public PageDTO<PostDTO> getPostsByAuthorId(UUID id, Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts from the user with the
     * supplied profile {@code id}, that are in reply to another post.
     * 
     * @param id   the id of the profile (author) to query
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     * @throws ResourceNotFoundException if no profile by that id exists
     */
    public PageDTO<PostDTO> getRepliesByAuthorId(UUID id, Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts liked by the user with
     * the supplied profile {@code id}.
     * 
     * @param id   the id of the profile (author) to query
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     * @throws ResourceNotFoundException if no profile by that id exists
     */
    public PageDTO<PostDTO> getLikesByAuthorId(UUID id, Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts that contain a mention
     * of the user with the supplied profile {@code id}.
     * 
     * @param id   the id of the profile (author) to query
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     * @throws ResourceNotFoundException if no profile by that id exists
     */
    public PageDTO<PostDTO> getMentionsOfAuthorId(UUID id, Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts from the authenticated
     * user and profiles that the authenticated user follows.
     * 
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     */
    public PageDTO<PostDTO> getHomepagePosts(Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts from all users.
     * 
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     */
    public PageDTO<PostDTO> getDiscoverPosts(Pageable page);

}
