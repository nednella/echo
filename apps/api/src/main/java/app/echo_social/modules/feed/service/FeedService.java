package app.echo_social.modules.feed.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import app.echo_social.exception.ApplicationException;
import app.echo_social.modules.post.dto.response.PostDTO;
import app.echo_social.shared.pagination.Paged;

public interface FeedService {

    /**
     * Fetches a page of {@link PostDTO} for the home feed: posts authored by the
     * authenticated user and by profiles they follow.
     *
     * @param page pagination parameters
     * @return a {@link Paged} of {@link PostDTO}; empty if no matches
     */
    Paged<PostDTO> getHomeFeed(Pageable page);

    /**
     * Fetches a page of {@link PostDTO} for the discover feed (global timeline).
     *
     * @param page pagination parameters
     * @return a {@link Paged} of {@link PostDTO}; empty if no matches
     */
    Paged<PostDTO> getDiscoverFeed(Pageable page);

    /**
     * Fetches a page of top-level (no parent) posts authored by the given profile
     * by {@code id}.
     *
     * @param profileId
     * @param page      pagination parameters
     * @return a {@link Paged} of {@link PostDTO}; empty if no matches
     * @throws ApplicationException if no profile with the given id exists
     */
    Paged<PostDTO> getProfilePosts(UUID profileId, Pageable page);

    /**
     * Fetches a page of reply posts (posts with a parent) authored by the given
     * profile by {@code id}.
     *
     * @param profileId
     * @param page      pagination parameters
     * @return a {@link Paged} of {@link PostDTO}; empty if no matches
     * @throws ApplicationException if no profile with the given id exists
     */
    Paged<PostDTO> getProfileReplies(UUID profileId, Pageable page);

    /**
     * Fetches a page of posts liked by the given profile by {@code id}.
     *
     * @param profileId
     * @param page      pagination parameters
     * @return a {@link Paged} of {@link PostDTO}; empty if no matches
     * @throws ApplicationException if no profile with the given id exists
     */
    Paged<PostDTO> getProfileLikes(UUID profileId, Pageable page);

    /**
     * Fetches a page of posts that mention the given profile by {@code id}.
     *
     * @param profileId
     * @param page      pagination parameters
     * @return a {@link Paged} of {@link PostDTO}; empty if no matches
     * @throws ApplicationException if no profile with the given id exists
     */
    Paged<PostDTO> getProfileMentions(UUID profileId, Pageable page);

}
