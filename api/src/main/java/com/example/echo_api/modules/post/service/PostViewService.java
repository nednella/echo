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
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     * @throws ApplicationException if no post with the given id exists
     */
    PageDTO<PostDTO> getRepliesByPostId(UUID id, Pageable page); // TODO: refactor to getConversationByPostId

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts from the user with the
     * given profile {@code id}, that are root-level posts (no parent).
     * 
     * @param id   the post author id
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     * @throws ApplicationException if no profile with the given id exists
     */
    PageDTO<PostDTO> getPostsByAuthorId(UUID id, Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts from the user with the
     * given profile {@code id}, that are in reply to another post.
     * 
     * @param id   the post author id
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     * @throws ApplicationException if no profile with the given id exists
     */
    PageDTO<PostDTO> getRepliesByAuthorId(UUID id, Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts liked by the user with
     * the given profile {@code id}.
     * 
     * @param id   the post like author id
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     * @throws ApplicationException if no profile with the given id exists
     */
    PageDTO<PostDTO> getLikesByAuthorId(UUID id, Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts that contain a mention
     * of the user with the given profile {@code id}.
     * 
     * @param id   the profile id
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     * @throws ApplicationException if no profile with the given id exists
     */
    PageDTO<PostDTO> getMentionsOfProfileId(UUID id, Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts from the authenticated
     * user and profiles that the authenticated user follows.
     * 
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     */
    PageDTO<PostDTO> getHomepagePosts(Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts from all users.
     * 
     * @param page the {@link Pageable} containing the pagination parameters
     * @return a {@link PageDTO} of {@link PostDTO} for matches, otherwise empty
     */
    PageDTO<PostDTO> getDiscoverPosts(Pageable page);

}
