package com.example.echo_api.modules.post.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.exception.PostErrorCode;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.PageDTO;
import com.example.echo_api.testing.support.AbstractIntegrationTest;

/**
 * Integration test class for {@link PostViewController}.
 */
class PostViewControllerIT extends AbstractIntegrationTest {

    private static final String BY_ID_PATH = ApiRoutes.POST.BY_ID;
    private static final String REPLIES_PATH = ApiRoutes.POST.REPLIES;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void cleanDb() {
        cleaner.cleanPosts();
    }

    /**
     * Create and persist a new {@link Post} with the given parent, author and text
     * fields.
     * 
     * @param parentId the ID of the parent post, or {@code null} if not a reply
     * @param authorId the ID of the user creating the post
     * @param text     the text content of the post
     * @return the persisted {@link Post}
     */
    private Post createPost(UUID parentId, UUID authorId, String text) {
        return postRepository.save(Post.create(parentId, authorId, text));
    }

    /**
     * Helper funtion to validate pagination metadata.
     */
    private static void assertPageMetadata(
        PageDTO<?> dto,
        URI expectedPrevious,
        URI expectedNext,
        int expectedOffset,
        int expectedLimit,
        int expectedTotal) {
        assertThat(dto).isNotNull();
        assertThat(dto.previous()).isEqualTo(expectedPrevious);
        assertThat(dto.next()).isEqualTo(expectedNext);
        assertThat(dto.offset()).isEqualTo(expectedOffset);
        assertThat(dto.limit()).isEqualTo(expectedLimit);
        assertThat(dto.total()).isEqualTo(expectedTotal);
    }

    @Test
    void getPostById_Returns200PostDto_WhenPostByIdExists() {
        // api: GET /api/v1/post/{id} ==> 200 OK : PostDTO
        Post post = createPost(null, authUser.getId(), "Test post.");

        PostDTO response = authenticatedClient.get()
            .uri(BY_ID_PATH, post.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody(PostDTO.class)
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(post.getId().toString());
        assertThat(response.parentId()).isNull();
        assertThat(response.text()).isEqualTo("Test post.");
    }

    @Test
    void getPostById_Returns404NotFound_WhenPostByIdDoesNotExist() {
        // api: GET /api/v1/post/{id} ==> 404 Not Found : ErrorDTO
        PostErrorCode errorCode = PostErrorCode.ID_NOT_FOUND;
        UUID nonExistingPostId = UUID.randomUUID();

        ErrorResponse expected = new ErrorResponse(
            errorCode.getStatus(),
            errorCode.formatMessage(nonExistingPostId),
            null);

        authenticatedClient.get()
            .uri(BY_ID_PATH, nonExistingPostId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void getRepliesByPostId_Returns200PageDtoOfPostDto_WhenPostByIdExists() {
        // api: GET /api/v1/post/{id}/replies ==> 200 OK : PageDTO<PostDTO>
        Post post = createPost(null, authUser.getId(), "Test post.");
        Post reply1 = createPost(post.getId(), mockUser.getId(), "Test reply 1.");
        Post reply2 = createPost(post.getId(), mockUser.getId(), "Test reply 2.");

        PageDTO<PostDTO> response = authenticatedClient.get()
            .uri(REPLIES_PATH, post.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<PageDTO<PostDTO>>() {})
            .returnResult()
            .getResponseBody();

        assertPageMetadata(response, null, null, 0, 20, 2);
        assertThat(response.items())
            .hasSize(2)
            .extracting(PostDTO::id)
            .contains(reply1.getId().toString(), reply2.getId().toString());
    }

    @Test
    void getRepliesByPostId_Returns404NotFound_WhenPostByIdDoesNotExist() {
        // api: GET /api/v1/post/{id}/replies ==> 404 Not Found : ErrorDTO
        PostErrorCode errorCode = PostErrorCode.ID_NOT_FOUND;
        UUID nonExistingPostId = UUID.randomUUID();

        ErrorResponse expected = new ErrorResponse(
            errorCode.getStatus(),
            errorCode.formatMessage(nonExistingPostId),
            null);

        authenticatedClient.get()
            .uri(REPLIES_PATH, nonExistingPostId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

}
