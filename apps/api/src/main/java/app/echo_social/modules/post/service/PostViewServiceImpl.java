package app.echo_social.modules.post.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.echo_social.modules.post.dto.response.PostDTO;
import app.echo_social.modules.post.entity.Post;
import app.echo_social.modules.post.exception.PostErrorCode;
import app.echo_social.modules.post.repository.PostRepository;
import app.echo_social.shared.pagination.Paged;
import app.echo_social.shared.pagination.PageMapper;
import app.echo_social.shared.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Service implementation for managing read-only {@link Post} data presentation
 * operations associated with singular and paginated post objects.
 */
@Service
class PostViewServiceImpl extends BasePostService implements PostViewService {

    private final HttpServletRequest httpServletRequest;

    // @formatter:off
    protected PostViewServiceImpl(
        SessionService sessionService,
        PostRepository postRepository,
        HttpServletRequest httpServletRequest) {
        super(sessionService, postRepository);
        this.httpServletRequest = httpServletRequest;
    }
    // @formatter:on

    @Override
    @Transactional(readOnly = true)
    public PostDTO getPostById(UUID id) {
        UUID authUserId = getAuthenticatedUserId();

        return postRepository.findPostDtoById(id, authUserId)
            .orElseThrow(() -> PostErrorCode.ID_NOT_FOUND.buildAsException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Paged<PostDTO> getRepliesByPostId(UUID id, Pageable page) {
        validatePostExists(id);
        UUID authUserId = getAuthenticatedUserId();

        Page<PostDTO> query = postRepository.findRepliesById(id, authUserId, page);
        String uri = getCurrentRequestUri();
        return PageMapper.toDTO(query, uri);
    }

    /**
     * Fetch the current HTTP request URI.
     * 
     * @return the current request URI as a string
     */
    private String getCurrentRequestUri() {
        return httpServletRequest.getRequestURI();
    }

}
