package com.example.echo_api.service.feed;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;

public interface FeedService {

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts that are relevant to the
     * authenticated users homepage feed.
     * 
     * <p>
     * This includes only <b>root-level</b> posts, both from the authenticated user,
     * and from users that the authenticated user follows.
     * 
     * @param page The {@link Pageable} containing the pagination parameters.
     * @return A {@link PageDTO} of {@link PostDTO} for matches, otherwise empty.
     */
    public PageDTO<PostDTO> getHomepage(Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts that are relevant to the
     * discovery page.
     * 
     * <p>
     * This includes only <b>root-level</b> posts from all users, except those
     * blocked by the authenticated user.
     * 
     * @param page The {@link Pageable} containing the pagination parameters.
     * @return A {@link PageDTO} of {@link PostDTO} for matches, otherwise empty.
     */
    public PageDTO<PostDTO> getDiscover(Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts that are relevant to the
     * main feed of a profile page by {@code username}.
     * 
     * <p>
     * This includes only <b>root-level</b> posts from that profile.
     * 
     * @param username The username of the profile to query.
     * @param page     The {@link Pageable} containing the pagination parameters.
     * @return A {@link PageDTO} of {@link PostDTO} for matches, otherwise empty.
     * @throws ResourceNotFoundException If no profile by that username exists.
     */
    public PageDTO<PostDTO> getProfile(String username, Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts that are relevant to the
     * reply tab of a profile page by {@code id}.
     * 
     * <p>
     * This includes only posts that are in reply to another post, from that
     * profile.
     * 
     * @param id   The id of the profile to query.
     * @param page The {@link Pageable} containing the pagination parameters.
     * @return A {@link PageDTO} of {@link PostDTO} for matches, otherwise empty.
     * @throws ResourceNotFoundException If no profile by that id exists.
     */
    public PageDTO<PostDTO> getProfileReplies(UUID id, Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts that are relevant to the
     * liked tab of a profile page by {@code id}.
     * 
     * <p>
     * This includes only posts that are liked by that profile.
     * 
     * @param id   The id of the profile to query.
     * @param page The {@link Pageable} containing the pagination parameters.
     * @return A {@link PageDTO} of {@link PostDTO} for matches, otherwise empty.
     * @throws ResourceNotFoundException If no profile by that id exists.
     */
    public PageDTO<PostDTO> getProfileLikes(UUID id, Pageable page);

    /**
     * Fetch a {@link PageDTO} of {@link PostDTO} for posts that are relevant to the
     * mentions tab of a profile page by {@code id}.
     * 
     * <p>
     * This includes only posts that include a mention of that profile.
     * 
     * @param id   The id of the profile to query.
     * @param page The {@link Pageable} containing the pagination parameters.
     * @return A {@link PageDTO} of {@link PostDTO} for matches, otherwise empty.
     * @throws ResourceNotFoundException If no profile by that id exists.
     */
    public PageDTO<PostDTO> getProfileMentions(UUID id, Pageable page);

}
