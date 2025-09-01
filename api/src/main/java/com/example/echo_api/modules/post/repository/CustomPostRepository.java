package com.example.echo_api.modules.post.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.example.echo_api.modules.post.dto.PostDTO;

public interface CustomPostRepository {

    /**
     * Retrieves a {@link PostDTO} for the post with the specified ID.
     * 
     * @param postId     the id of the post to query
     * @param authUserId the id of the authenticated user, required for building
     *                   user and post relationships
     * @return an {@link Optional} containing the {@link PostDTO} if found, else
     *         empty
     */
    Optional<PostDTO> findPostDtoById(@NonNull UUID postId, @NonNull UUID authUserId);

    /**
     * Retrieves a paginated list of {@link PostDTO} for posts that are in reply to
     * the post with the specified ID.
     * 
     * <p>
     * The replies are sorted in the following order of precedence before pagination
     * is applied:
     * <ul>
     * <li>Has a response from the original post's author
     * <li>Engagement metrics (sum of likes, replies and shares)
     * <li>Creation date (newest first)
     * </ul>
     * 
     * @param postId     the id of the post to query
     * @param authUserId the id of the authenticated user, required for building
     *                   user and post relationships
     * @param p          the pagination and sorting configuration
     * @return a {@link Page} of {@link PostDTO}
     */
    Page<PostDTO> findRepliesById(@NonNull UUID postId, @NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for root-level posts from the
     * authenticated user and profiles that the authenticated user follows, sorted
     * by newest first.
     * 
     * @param authUserId the id of the authenticated user, required for building
     *                   user and post relationships
     * @param p          the pagination and sorting configuration
     * @return a {@link Page} of {@link PostDTO}
     */
    Page<PostDTO> findHomepagePosts(@NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for root-level posts from all
     * profiles.
     * 
     * @param authUserId the id of the authenticated user, required for building
     *                   user and post relationships
     * @param p          the pagination and sorting configuration
     * @return a {@link Page} of {@link PostDTO}
     */
    Page<PostDTO> findDiscoverPosts(@NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for root-level posts from the
     * profile with the specified ID, sorted by newest first.
     * 
     * @param profileId  the id of the profile to query
     * @param authUserId the id of the authenticated user, required for building
     *                   user and post relationships
     * @param p          the pagination and sorting configuration
     * @return a {@link Page} of {@link PostDTO}
     */
    Page<PostDTO> findPostsByProfileId(@NonNull UUID profileId, @NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for posts from the profile with
     * the specified ID, that are in reply to another post, sorted by newest first.
     * 
     * @param profileId  the id of the profile to query
     * @param authUserId the id of the authenticated user, required for building
     *                   user and post relationships
     * @param p          the pagination and sorting configuration
     * @return a {@link Page} of {@link PostDTO}
     */
    Page<PostDTO> findRepliesByProfileId(@NonNull UUID profileId, @NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for posts liked by the profile
     * with the specified ID, sorted by newest first.
     * 
     * @param profileId  the id of the profile to query
     * @param authUserId the id of the authenticated user, required for building
     *                   user and post relationships
     * @param p          the pagination and sorting configuration
     * @return a {@link Page} of {@link PostDTO}
     */
    Page<PostDTO> findPostsLikedByProfileId(@NonNull UUID profileId, @NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for posts that include a
     * mention, e.g. {@code @elonmusk}, of the profile with the specified ID, sorted
     * by newest first.
     * 
     * @param profileId  the id of the profile to query
     * @param authUserId the id of the authenticated user, required for building
     *                   user and post relationships
     * @param p          the pagination and sorting configuration
     * @return a {@link Page} of {@link PostDTO}
     */
    Page<PostDTO> findPostsMentioningProfileId(@NonNull UUID profileId, @NonNull UUID authUserId, @NonNull Pageable p);

}
