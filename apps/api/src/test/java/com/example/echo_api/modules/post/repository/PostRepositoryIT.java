package com.example.echo_api.modules.post.repository;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

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

/**
 * TODO
 * 
 * 2. Adopt assertj assertThat usage
 * 3. Add additional test coverage if possible
 * 
 */

// TODO: finish JDocs
// TODO: SQL query testing for metrics/relationships

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
        return postRepository.save(Post.create(parentId, authorId, text));
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

    @Test
    void findPostDtoById_ReturnsPostDto_WhenPostByIdExists() {
        UUID postId = postWithReplies.getId();
        UUID authUserId = self.getId();

        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            postId,
            authUserId);

        assertNotNull(optPost);
        assertTrue(optPost.isPresent());
    }

    @Test
    void findPostDtoById_ReturnOptionalEmpty_WhenPostByIdDoesNotExist() {
        UUID postId = UUID.randomUUID();
        UUID authUserId = self.getId();

        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            postId,
            authUserId);

        assertNotNull(optPost);
        assertTrue(optPost.isEmpty());
    }

    @Test
    void findPostDtoById_ReturnPostDtoWithNullAuthorRelationshipDto_WhenPostAuthorIsSelf() {
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

    @Test
    void findPostDtoById_ReturnPostDtoWithAuthorRelationshipDto_WhenPostAuthorIsNotSelf() {
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

    @Test
    void findPostDtoById_ReturnsPostDtoWithNoEntities_WhenPostTextDoesNotContainAnyEntities() {
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

    @Test
    void FindPostDtoById_ReturnsPostDtoWithMultipleEntities_WhenPostTextDoesContainSomeEntities() {
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

    @Test
    void findRepliesById_ReturnsPageOfPostDto_WhenPostByIdHasReplies() {
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

    @Test
    void findRepliesById_ReturnsEmptyPage_WhenPostByIdHasNoReplies() {
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

    @Test
    void findRepliesById_ReplyWithOpResponseIsRankedHighest() {
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

    @Test
    void findRepliesById_ReplyWithEngagementIsRankedHigherThanReplyWithoutEngagement() {
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
    void findHomepagePosts_RankedByCreatedAtDescending() {
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
    void findHomepagePosts_ReturnsPostsBySelfOnly_WhenNotFollowingAnyUsers() {
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
    void findHomepagePosts_ReturnsEmmpty_WhenNoPostsAndNotFollowingAnyUsers() {
        UUID authUserId = randomUser.getId(); // randomUser has no root-level posts, only replies
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> query = postRepository.findHomepagePosts(
            authUserId,
            page);

        assertNotNull(query);
        assertTrue(query.isEmpty());
    }

    @Test
    void findDiscoverPosts_RankedByCreatedAtDescending() {
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
    void findPostsByProfileId_RankedByCreatedAtDescending() {
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
    void findPostsByProfileId_ContainsOnlyPostsAuthoredByProfileId() {
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
    void findRepliesByProfileId_RankedByCreatedAtDescending() {
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
    void findRepliesByProfileId_ContainsOnlyPostsAuthoredByProfileId() {
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
    void findPostsLikedByProfileId_RankedByCreatedAtDescending() {
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
    void findPostsLikedByProfileId_ContainsOnlyPostsLikedByProfileId() {
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
    void findPostsMentioningProfileId_RankedByCreatedAtDescending() {
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
    void findPostsMentioningProfileId_ContainsOnlyPostsMentioningUsernameAssociatedWithProfileId() {
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

    @Test
    void delete_CascadeDeletesRelatedPosts() {
        Post root = createPost(null, self.getId(), "root.");
        Post replyToRoot = createPost(root.getId(), self.getId(), "reply to root.");
        Post replyToReply = createPost(replyToRoot.getId(), self.getId(), "reply to reply.");

        postRepository.delete(root);

        assertFalse(postRepository.existsById(root.getId()));
        assertFalse(postRepository.existsById(replyToRoot.getId()));
        assertFalse(postRepository.existsById(replyToReply.getId()));
    }

}