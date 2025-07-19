package com.example.echo_api.persistence.repository.custom;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import com.example.echo_api.persistence.dto.response.post.PostDTO;

public interface CustomPostRepository {

    /**
     * Retrieves a {@link PostDTO} for the post with the specified ID.
     * 
     * @param postId     The id of the target post to query.
     * @param authUserId The id of the authenticated user, required for obtaining
     *                   user and post relationships.
     * @return an {@link Optional} containing the {@link PostDTO} if found, else
     *         empty.
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
     * @param postId     The id of the target post to query.
     * @param authUserId The id of the authenticated user, required for obtaining
     *                   user and post relationships.
     * @param p          The pagination and sorting configuration.
     * @return a {@link Page} of {@link PostDTO}.
     */
    Page<PostDTO> findRepliesById(@NonNull UUID postId, @NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for root-level posts from the
     * authenticated user and profiles that the authenticated user follows, sorted
     * by newest first.
     * 
     * @param authUserId The id of the authenticated user, required for obtaining
     *                   profile and post relationships.
     * @param p          The pagination and sorting configuration.
     * @return a {@link Page} of {@link PostDTO}.
     */
    Page<PostDTO> findHomepagePosts(@NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for root-level posts from all
     * profiles, except those blocked by the authenticated user, sorted by newest
     * first.
     * 
     * @param authUserId The id of the authenticated user, required for obtaining
     *                   profile and post relationships.
     * @param p          The pagination and sorting configuration.
     * @return a {@link Page} of {@link PostDTO}.
     */
    Page<PostDTO> findDiscoverPosts(@NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for root-level posts from the
     * profile with the specified ID, sorted by newest first.
     * 
     * @param profileId  The id of the target profile to query.
     * @param authUserId The id of the authenticated user, required for obtaining
     *                   profile and post relationships.
     * @param p          The pagination and sorting configuration.
     * @return a {@link Page} of {@link PostDTO}.
     */
    Page<PostDTO> findPostsByProfileId(@NonNull UUID profileId, @NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for posts from the profile with
     * the specified ID, that are in reply to another post, sorted by newest first.
     * 
     * @param profileId  The id of the target profile to query.
     * @param authUserId The id of the authenticated user, required for obtaining
     *                   profile and post relationships.
     * @param p          The pagination and sorting configuration.
     * @return a {@link Page} of {@link PostDTO}.
     */
    Page<PostDTO> findRepliesByProfileId(@NonNull UUID profileId, @NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for posts liked by the profile
     * with the specified ID, sorted by newest first.
     * 
     * @param profileId  The id of the target profile to query.
     * @param authUserId The id of the authenticated user, required for obtaining
     *                   profile and post relationships.
     * @param p          The pagination and sorting configuration.
     * @return a {@link Page} of {@link PostDTO}.
     */
    Page<PostDTO> findPostsLikedByProfileId(@NonNull UUID profileId, @NonNull UUID authUserId, @NonNull Pageable p);

    /**
     * Retrieves a paginated list of {@link PostDTO} for posts that include a
     * mention, e.g. {@code @elonmusk}, of the profile with the specified ID, sorted
     * by newest first.
     * 
     * @param profileId  The id of the target profile to query.
     * @param authUserId The id of the authenticated user, required for obtaining
     *                   profile and post relationships.
     * @param p          The pagination and sorting configuration.
     * @return a {@link Page} of {@link PostDTO}.
     */
    Page<PostDTO> findPostsMentioningProfileId(@NonNull UUID profileId, @NonNull UUID authUserId, @NonNull Pageable p);

}
