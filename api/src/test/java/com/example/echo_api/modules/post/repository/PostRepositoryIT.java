package com.example.echo_api.modules.post.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.entity.PostLike;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.modules.user.entity.User;
import com.example.echo_api.modules.user.repository.UserRepository;
import com.example.echo_api.testing.support.AbstractRepositoryTest;
import com.example.echo_api.util.PostEntityExtractor;

// TODO: finish JDocs

/**
 * Integration test class for {@link PostRepository}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostRepositoryIT extends AbstractRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;

    private Profile self;
    private Profile randomUser;

    private Post postWithReplies;
    private Post postWithEntities;
    private Post replyWithOpResponse;
    private Post replyWithLike;

    private Profile createProfile(String externalId, String username) {
        User user = User.fromExternalSource(externalId);
        user = userRepository.save(user);

        Profile profile = Profile.forTest(user.getId(), username);
        return profileRepository.save(profile);
    }

    private Post createPost(UUID parentId, UUID authorId, String text) {
        Post post = new Post(parentId, authorId, text);
        return postRepository.save(post);
    }

    /**
     * Assert that each subsequent post in the list was created before its previous,
     * i.e., the array of posts are ordered by newest first.
     */
    private void assertPostsRankedByCreatedAtDescending(List<PostDTO> posts) {
        for (int i = 0; i < posts.size() - 1; i++) {
            Instant prev = Instant.parse(posts.get(i).createdAt());
            Instant next = Instant.parse(posts.get(i + 1).createdAt());

            assertTrue(next.isBefore(prev));
        }
    }

    @BeforeAll // @formatter:off
    void setup() {
        self = createProfile("user_someUniqueId1", "self");
        randomUser = createProfile("user_someUniqueId2", "random_user");

        // Create root posts (no parent)
        postWithReplies = createPost(null, self.getId(), "This post has some replies.");
        postWithEntities = createPost(null, self.getId(),"Nice #SpringBoot app, github.com/repo");

        // create posts to form a conversation
        replyWithOpResponse = createPost(postWithReplies.getId(), randomUser.getId(),"A reply, where @self will reply back!");
        replyWithLike = createPost(postWithReplies.getId(), randomUser.getId(), "A reply that @self will like!");
        createPost(postWithReplies.getId(), randomUser.getId(), "A reply with no engagement.");
        createPost(replyWithOpResponse.getId(), self.getId(), "I'm replying back.");

        // persist relevant likes/entities
        postLikeRepository.save(new PostLike(replyWithOpResponse.getId(), self.getId()));
        postLikeRepository.save(new PostLike(replyWithLike.getId(), self.getId()));
        postEntityRepository.saveAll(PostEntityExtractor.extract(postWithEntities.getId(), postWithEntities.getText()));
        postEntityRepository.saveAll(PostEntityExtractor.extract(replyWithOpResponse.getId(), replyWithOpResponse.getText()));
        postEntityRepository.saveAll(PostEntityExtractor.extract(replyWithLike.getId(), replyWithLike.getText()));
    } // @formatter:on

    /**
     * Test {@link PostRepository#findPostDtoById(UUID, UUID)} to verify that a post
     * can be found by its {@code id}.
     */
    @Test
    void PostRepository_FindPostDtoById_ReturnOptionalPostDto() {
        UUID postId = postWithReplies.getId();
        UUID authUserId = self.getId();

        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            postId,
            authUserId);

        assertNotNull(optPost);
        assertTrue(optPost.isPresent());
    }

    /**
     * Test {@link PostRepository#findPostDtoById(UUID, UUID)} to verify that a
     * non-existent post {@code id} returns an empty {@link Optional}.
     */
    @Test
    void PostRepository_FindPostDtoById_ReturnOptionalEmpty() {
        UUID postId = UUID.randomUUID();
        UUID authUserId = self.getId();

        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            postId,
            authUserId);

        assertNotNull(optPost);
        assertTrue(optPost.isEmpty());
    }

    /**
     * Test {@link PostRepository#findPostDtoById(UUID, UUID)} to verify that a post
     * belonging to the authenticated user can be found by its {@code id}, and
     * returns a {@code null} author relationship object.
     */
    @Test
    void PostRepository_FindPostDtoById_ReturnPostDtoWithNullAuthorRelationshipDto() {
        UUID postId = postWithReplies.getId();
        UUID authUserId = self.getId();

        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            postId,
            authUserId);

        assertNotNull(optPost);
        assertTrue(optPost.isPresent());

        PostDTO post = optPost.get();
        assertNotNull(post.metrics());
        assertNotNull(post.relationship());
        assertNull(post.author().relationship()); // author is self, thus NULL
    }

    /**
     * Test {@link PostRepository#findPostDtoById(UUID, UUID)} to verify that a post
     * NOT belonging to the authenticated user can be found by its {@code id}, and
     * returns an author relationship object.
     */
    @Test
    void PostRepository_FindPostDtoById_ReturnPostDtoWithAuthorRelationshipDto() {
        UUID postId = replyWithOpResponse.getId();
        UUID authUserId = self.getId();

        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            postId,
            authUserId);

        assertNotNull(optPost);
        assertTrue(optPost.isPresent());

        PostDTO post = optPost.get();
        assertNotNull(post.metrics());
        assertNotNull(post.relationship());
        assertNotNull(post.author().relationship()); // author is not self, thus NOT NULL
    }

    /**
     * Test {@link PostRepository#findPostDtoById(UUID, UUID)} to verify that a post
     * containing no entities returns empty entity arrays.
     */
    @Test
    void PostRepository_FindPostDtoById_ReturnsNoEntities() {
        UUID postId = postWithReplies.getId();
        UUID authUserId = self.getId();

        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            postId,
            authUserId);

        assertNotNull(optPost);
        assertTrue(optPost.isPresent());

        PostDTO post = optPost.get();
        assertTrue(post.entities().hashtags().isEmpty()); // no related hashtags
        assertTrue(post.entities().mentions().isEmpty()); // no related mentions
    }

    /**
     * Test {@link PostRepository#findPostDtoById(UUID, UUID)} to verify that a post
     * containing some entities returns those entities within their respective
     * arrays.
     */
    @Test
    void PostRepository_FindPostDtoById_ReturnsMultipleEntities() {
        UUID postId = postWithEntities.getId();
        UUID authUserId = self.getId();

        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            postId,
            authUserId);

        assertNotNull(optPost);
        assertTrue(optPost.isPresent());

        PostDTO post = optPost.get();
        assertEquals(1, post.entities().hashtags().size()); // 1 related hashtag
        assertEquals(1, post.entities().urls().size()); // 1 related url
    }

    /**
     * Test {@link PostRepository#findRepliesById(UUID, UUID, Pageable)} to verify
     * that searching for a posts' replies by its {@code id} returns a {@link Page}
     * of {@link PostDTO}.
     */
    @Test
    void PostRepository_FindRepliesById_HasRepliesReturnsPageOfPostDto() {
        UUID postId = postWithReplies.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> replies = postRepository.findRepliesById(
            postId,
            authUserId,
            page);

        assertNotNull(replies);
        assertTrue(replies.hasContent());
        assertEquals(3, replies.getTotalElements());
    }

    /**
     * Test {@link PostRepository#findRepliesById(UUID, UUID, Pageable)} to verify
     * that searching for a posts' replies by its {@code id}, that has no replies,
     * returns an empty {@link Page}.
     */
    @Test
    void PostRepository_FindRepliesById_HasNoRepliesReturnsEmptyPage() {
        UUID postId = postWithEntities.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> replies = postRepository.findRepliesById(
            postId,
            authUserId,
            page);

        assertNotNull(replies);
        assertTrue(replies.isEmpty());
        assertEquals(0, replies.getTotalElements());
    }

    /**
     * Test {@link PostRepository#findRepliesById(UUID, UUID, Pageable)} to verify
     * that searching for a posts' replies by its {@code id}, correctly ranks the
     * reply with an original poster response highest.
     */
    @Test
    void PostRepository_FindRepliesById_ReplyWithOpResponseIsRankedHighest() {
        UUID postId = postWithReplies.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> replies = postRepository.findRepliesById(
            postId,
            authUserId,
            page);

        assertNotNull(replies);
        assertTrue(replies.hasContent());
        assertTrue(replies.getTotalElements() > 1);
        assertEquals("A reply, where @self will reply back!", replies.getContent().getFirst().text());
    }

    /**
     * Test {@link PostRepository#findRepliesById(UUID, UUID, Pageable)} to verify
     * that searching for a posts' replies by its {@code id}, correctly ranks the
     * reply with engagement higher than those without any engagement.
     */
    @Test
    void PostRepository_FindRepliesById_ReplyWithEngagementIsRankedHigherThanReplyWithoutEngagement() {
        UUID postId = postWithReplies.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> replies = postRepository.findRepliesById(
            postId,
            authUserId,
            page);

        assertNotNull(replies);
        assertTrue(replies.hasContent());
        assertTrue(replies.getTotalElements() > 1);
        assertEquals("A reply that @self will like!", replies.getContent().get(1).text());
        assertEquals("A reply with no engagement.", replies.getContent().get(2).text());
    }

    @Test
    void PostRepository_FindHomepagePosts_RankedByCreatedAtDescending() {
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findHomepagePosts(
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.hasContent());
        assertTrue(query.getTotalElements() > 1);

        List<PostDTO> posts = query.getContent();
        assertPostsRankedByCreatedAtDescending(posts);
    }

    @Test
    void PostRepository_FindHomepagePosts_NoFollowsReturnsSelfPostsOnly() {
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findHomepagePosts(
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.hasContent());

        List<PostDTO> posts = query.getContent();

        // assert all posts belong to authUser
        for (PostDTO post : posts) {
            UUID authorId = UUID.fromString(post.author().id());
            assertEquals(authUserId, authorId);
        }
    }

    @Test
    void PostRepository_FindHomepagePosts_NoFollowsAndNoPostsReturnsEmptyPage() {
        UUID authUserId = randomUser.getId(); // randomUser has no root-level posts, only replies
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findHomepagePosts(
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.isEmpty());
    }

    @Test
    void PostRepository_FindDiscoverPosts_RankedByCreatedAtDescending() {
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findDiscoverPosts(
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.hasContent());
        assertTrue(query.getTotalElements() > 1);

        List<PostDTO> posts = query.getContent();
        assertPostsRankedByCreatedAtDescending(posts);
    }

    @Test
    void PostRepository_FindPostsByProfileId_RankedByCreatedAtDescending() {
        UUID profileId = self.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findPostsByProfileId(
            profileId,
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.hasContent());
        assertTrue(query.getTotalElements() > 1);

        List<PostDTO> posts = query.getContent();
        assertPostsRankedByCreatedAtDescending(posts);
    }

    @Test
    void PostRepository_FindPostsByProfileId_ContainsOnlyPostsByProfileId() {
        UUID profileId = self.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findPostsByProfileId(
            profileId,
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.hasContent());
        assertTrue(query.getTotalElements() > 1);

        List<PostDTO> posts = query.getContent();

        // assert all posts belong to profileId
        for (PostDTO post : posts) {
            UUID authorId = UUID.fromString(post.author().id());
            assertEquals(profileId, authorId);
        }
    }

    @Test
    void PostRepository_FindRepliesByProfileId_RankedByCreatedAtDescending() {
        UUID profileId = randomUser.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findRepliesByProfileId(
            profileId,
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.hasContent());
        assertTrue(query.getTotalElements() > 1);

        List<PostDTO> posts = query.getContent();
        assertPostsRankedByCreatedAtDescending(posts);
    }

    @Test
    void PostRepository_FindRepliesByProfileId_ContainsOnlyPostsAuthoredByProfileId() {
        UUID profileId = randomUser.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findRepliesByProfileId(
            profileId,
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.hasContent());
        assertTrue(query.getTotalElements() > 1);

        List<PostDTO> posts = query.getContent();

        // assert all posts belong to profileId
        for (PostDTO post : posts) {
            UUID authorId = UUID.fromString(post.author().id());
            assertEquals(profileId, authorId);
        }
    }

    @Test
    void PostRepository_FindPostsLikedByProfileId_RankedByCreatedAtDescending() {
        UUID profileId = self.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findPostsLikedByProfileId(
            profileId,
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.hasContent());
        assertTrue(query.getTotalElements() > 1);

        List<PostDTO> posts = query.getContent();
        assertPostsRankedByCreatedAtDescending(posts);
    }

    @Test
    void PostRepository_FindPostsLikedByProfileId_ContainsOnlyPostsLikedByProfileId() {
        UUID profileId = self.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findPostsLikedByProfileId(
            profileId,
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.hasContent());
        assertTrue(query.getTotalElements() > 1);

        List<PostDTO> posts = query.getContent();

        // assert all liked posts do in fact have a like from the supplied profileId
        for (PostDTO post : posts) {
            UUID postId = UUID.fromString(post.id());
            assertTrue(postLikeRepository.existsByPostIdAndAuthorId(postId, profileId));
        }
    }

    @Test
    void PostRepository_FindPostsMentioningProfileId_RankedByCreatedAtDescending() {
        UUID profileId = self.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findPostsMentioningProfileId(
            profileId,
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.hasContent());
        assertTrue(query.getTotalElements() > 1);

        List<PostDTO> posts = query.getContent();
        assertPostsRankedByCreatedAtDescending(posts);
    }

    @Test
    void PostRepository_FindPostsMentioningProfileId_ContainsOnlyPostsMentioningUserWithProfileId() {
        String username = self.getUsername();
        UUID profileId = self.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findPostsMentioningProfileId(
            profileId,
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.hasContent());
        assertTrue(query.getTotalElements() > 1);

        List<PostDTO> posts = query.getContent();

        // assert all posts do in fact contain a mention of user with id of profileId
        for (PostDTO post : posts) {
            post.text().contains(username);
        }
    }

}