package com.example.echo_api.modules.feed.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.entity.PostLike;
import com.example.echo_api.modules.post.repository.PostEntityRepository;
import com.example.echo_api.modules.post.repository.PostLikeRepository;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.modules.profile.exception.ProfileErrorCode;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.PageDTO;
import com.example.echo_api.testing.support.AbstractIntegrationTest;
import com.example.echo_api.util.PostEntityExtractor;

/**
 * Integration test class for {@link FeedController}.
 */
class FeedControllerIT extends AbstractIntegrationTest {

    private static final String HOMEPAGE_PATH = ApiRoutes.FEED.HOMEPAGE;
    private static final String DISCOVER_PATH = ApiRoutes.FEED.DISCOVER;
    private static final String POSTS_PATH = ApiRoutes.FEED.POSTS;
    private static final String REPLIES_PATH = ApiRoutes.FEED.REPLIES;
    private static final String LIKES_PATH = ApiRoutes.FEED.LIKES;
    private static final String MENTIONS_PATH = ApiRoutes.FEED.MENTIONS;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;

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
        Post post = postRepository.save(Post.create(parentId, authorId, text));
        postEntityRepository.saveAll(PostEntityExtractor.extract(post.getId(), post.getText()));
        return post;
    }

    /**
     * Persist a {@link PostLike} for the given post and author.
     * 
     * @param postId   the ID of the post being liked
     * @param authorId the ID of the user who liked the post
     */
    private void likePost(UUID postId, UUID authorId) {
        postLikeRepository.save(new PostLike(postId, authorId));
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
    void getHomeFeed_Returns200PageDtoOfPostDto() {
        // api: GET /api/v1/feed/homepage ==> 200 OK : PageDTO<PostDTO>
        Post post1 = createPost(null, authUser.getId(), "Test post 1.");
        Post post2 = createPost(null, authUser.getId(), "Test post 2.");

        PageDTO<PostDTO> response = authenticatedClient.get()
            .uri(HOMEPAGE_PATH)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<PageDTO<PostDTO>>() {})
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();

        assertPageMetadata(response, null, null, 0, 20, 2);
        assertThat(response.items())
            .hasSize(2)
            .extracting(PostDTO::id)
            .contains(post1.getId().toString(), post2.getId().toString());
    }

    @Test
    void getDiscoverFeed_Returns200PageDtoOfPostDto() {
        // api: GET /api/v1/feed/discover ==> 200 OK : PageDTO<PostDTO>
        Post post1 = createPost(null, mockUser.getId(), "Test post 1.");
        Post post2 = createPost(null, mockUser.getId(), "Test post 2.");

        PageDTO<PostDTO> response = authenticatedClient.get()
            .uri(DISCOVER_PATH)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<PageDTO<PostDTO>>() {})
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();

        assertPageMetadata(response, null, null, 0, 20, 2);
        assertThat(response.items())
            .hasSize(2)
            .extracting(PostDTO::id)
            .contains(post1.getId().toString(), post2.getId().toString());
    }

    @Test
    void getProfilePosts_Returns200PageDtoOfPostDto() {
        // api: GET /api/v1/feed/profile/{id}/posts ==> 200 OK : PageDTO<PostDTO>
        UUID profileId = mockUser.getId();
        Post post = createPost(null, mockUser.getId(), "Test post.");

        PageDTO<PostDTO> response = authenticatedClient.get()
            .uri(POSTS_PATH, profileId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<PageDTO<PostDTO>>() {})
            .returnResult()
            .getResponseBody();

        assertPageMetadata(response, null, null, 0, 20, 1);
        assertThat(response.items())
            .hasSize(1)
            .extracting(PostDTO::id)
            .contains(post.getId().toString());
    }

    @Test
    void getProfilePosts_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/posts ==> 404 Not Found : ErrorDTO
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID nonExistingProfileId = UUID.randomUUID();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            errorCode.formatMessage(nonExistingProfileId),
            null);

        authenticatedClient.get()
            .uri(POSTS_PATH, nonExistingProfileId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void getProfileReplies_Returns200PageDtoOfPostDto() {
        // api: GET /api/v1/feed/profile/{id}/replies ==> 200 OK : PageDTO<PostDTO>
        UUID profileId = mockUser.getId();
        Post post = createPost(null, authUser.getId(), "Test post.");
        Post reply1 = createPost(post.getId(), profileId, "Test reply 1.");
        Post reply2 = createPost(post.getId(), profileId, "Test reply 2.");

        PageDTO<PostDTO> response = authenticatedClient.get()
            .uri(REPLIES_PATH, profileId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<PageDTO<PostDTO>>() {})
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();

        assertPageMetadata(response, null, null, 0, 20, 2);
        assertThat(response.items())
            .hasSize(2)
            .extracting(PostDTO::id)
            .contains(reply1.getId().toString(), reply2.getId().toString());
    }

    @Test
    void getProfileReplies_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/replies ==> 404 Not Found : ErrorDTO
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID nonExistingProfileId = UUID.randomUUID();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            errorCode.formatMessage(nonExistingProfileId),
            null);

        authenticatedClient.get()
            .uri(REPLIES_PATH, nonExistingProfileId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void getProfileLikes_ReturnPageDtoOfPostDto() {
        // api: GET /api/v1/feed/profile/{id}/likes ==> 200 OK : PageDTO<PostDTO>
        Post post = createPost(null, authUser.getId(), "Test post with 1 like.");

        UUID profileId = mockUser.getId();
        likePost(post.getId(), profileId);

        PageDTO<PostDTO> response = authenticatedClient.get()
            .uri(LIKES_PATH, profileId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<PageDTO<PostDTO>>() {})
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();

        assertPageMetadata(response, null, null, 0, 20, 1);
        assertThat(response.items())
            .hasSize(1)
            .extracting(PostDTO::id)
            .contains(post.getId().toString());
    }

    @Test
    void getProfileLikes_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/likes ==> 404 Not Found : ErrorDTO
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID nonExistingProfileId = UUID.randomUUID();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            errorCode.formatMessage(nonExistingProfileId),
            null);

        authenticatedClient.get()
            .uri(LIKES_PATH, nonExistingProfileId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

    @Test
    void getProfileMentions_ReturnPageDtoOfPostDto() {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> 200 OK : PageDTO<PostDTO>
        UUID profileId = authUser.getId();
        String textWithMention = String.format("Hello, @%s.", AUTH_USER_USERNAME);
        Post post = createPost(null, mockUser.getId(), textWithMention);

        PageDTO<PostDTO> response = authenticatedClient.get()
            .uri(MENTIONS_PATH, profileId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(new ParameterizedTypeReference<PageDTO<PostDTO>>() {})
            .returnResult()
            .getResponseBody();

        assertThat(response).isNotNull();

        assertPageMetadata(response, null, null, 0, 20, 1);
        assertThat(response.items())
            .hasSize(1)
            .extracting(PostDTO::id)
            .contains(post.getId().toString());
    }

    @Test
    void getProfileMentions_Returns404NotFound_WhenProfileByIdDoesNotExist() {
        // api: GET /api/v1/feed/profile/{id}/mentions ==> 404 Not Found : ErrorDTO
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID nonExistingProfileId = UUID.randomUUID();

        ErrorResponse expected = new ErrorResponse(
            HttpStatus.NOT_FOUND,
            errorCode.formatMessage(nonExistingProfileId),
            null);

        authenticatedClient.get()
            .uri(MENTIONS_PATH, nonExistingProfileId)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorResponse.class).isEqualTo(expected);
    }

}
