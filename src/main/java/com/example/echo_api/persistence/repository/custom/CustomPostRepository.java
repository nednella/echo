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
     * @param id                  The id of the target post to query.
     * @param authenticatedUserId The id of the authenticated user, required for
     *                            obtaining user and post relationships.
     * @return an {@link Optional} containing the {@link PostDTO} if found, else
     *         empty.
     */
    Optional<PostDTO> findPostDtoById(@NonNull UUID id, @NonNull UUID authenticatedUserId);

    /**
     * Retrieves a paginated list of {@link PostDTO} for posts that are in reply to
     * the post with the specified ID.
     * 
     * <p>
     * The list is sorted in the following order of precedence:
     * <ul>
     * <li>Has a response from the original post's author
     * <li>Engagement metrics (sum of likes, replies and shares)
     * <li>Creation date (newest first)
     * </ul>
     * 
     * @param id                  The id of the target post to query.
     * @param authenticatedUserId The id of the authenticated user, required for
     *                            obtaining user and post relationships.
     * @param p                   The pagination and sorting configuration.
     * @return a {@link Page} of {@link PostDTO}.
     */
    Page<PostDTO> findReplyDtosById(@NonNull UUID id, @NonNull UUID authenticatedUserId, @NonNull Pageable p);

}
