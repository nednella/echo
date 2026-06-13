package app.echo_social.modules.post.service;

import java.util.UUID;

import org.springframework.data.domain.Pageable;

import app.echo_social.exception.ApplicationException;
import app.echo_social.modules.post.dto.response.PostDTO;
import app.echo_social.shared.pagination.Paged;

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
     * Fetch a {@link Paged} of {@link PostDTO} for the posts that are in reply to
     * the given post {@code id}.
     * 
     * @param id   the post id
     * @param page pagination parameters
     * @return a {@link Paged} of {@link PostDTO}; empty if no matches
     * @throws ApplicationException if no post with the given id exists
     */
    Paged<PostDTO> getRepliesByPostId(UUID id, Pageable page);
    // TODO: refactor to getConversationByPostId

}
