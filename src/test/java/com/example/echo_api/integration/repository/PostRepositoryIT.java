package com.example.echo_api.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.model.post.like.PostLike;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.AccountRepository;
import com.example.echo_api.persistence.repository.PostEntityRepository;
import com.example.echo_api.persistence.repository.PostLikeRepository;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.util.extractor.PostEntityExtractor;

/**
 * Integration test class for {@link PostRepository}.
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PostRepositoryIT extends RepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private PostEntityRepository postEntityRepository;

    private Profile self;
    private Profile notSelf;

    private Post postWithReplies;
    private Post postWithEntities;

    private Post replyWithOpResponse;
    private Post opResponse;
    private Post replyWithLike;
    private Post reply;

    @BeforeAll
    void setup() {
        Account selfAcc = new Account("self", "password");
        accountRepository.save(selfAcc); // save account to repository to generate a UUID
        self = new Profile(selfAcc.getId(), selfAcc.getUsername());
        self = profileRepository.save(self); // save profile to provide foreign key

        Account notSelfAcc = new Account("notSelf", "password");
        accountRepository.save(notSelfAcc); // save account to repository to generate a UUID
        notSelf = new Profile(notSelfAcc.getId(), notSelfAcc.getUsername());
        notSelf = profileRepository.save(notSelf); // save profile to provide foreign key

        // save a post that will be the root of a conversation
        postWithReplies = new Post(self.getId(), "This post has some replies.");
        postWithReplies = postRepository.save(postWithReplies);

        // save a post with some entities, but no replies
        postWithEntities = new Post(notSelf.getId(), "Hey @john_doe and @admin! Nice #SpringBoot app, github.com/repo");
        postWithEntities = postRepository.save(postWithEntities);
        postEntityRepository.saveAll(PostEntityExtractor.extract(postWithEntities.getId(), postWithEntities.getText()));

        // save a reply with an OP (original poster) response
        replyWithOpResponse = new Post(postWithReplies.getId(), notSelf.getId(), "A reply with an OP response.");
        replyWithOpResponse = postRepository.save(replyWithOpResponse);

        // save an OP response
        opResponse = new Post(replyWithOpResponse.getId(), self.getId(), "An original poster response to a reply.");
        opResponse = postRepository.save(opResponse);

        // save a reply with a like
        replyWithLike = new Post(postWithReplies.getId(), notSelf.getId(), "A reply with a like.");
        replyWithLike = postRepository.save(replyWithLike);
        postLikeRepository.save(new PostLike(replyWithLike.getId(), self.getId()));

        // save a reply with no engagement
        reply = new Post(postWithReplies.getId(), notSelf.getId(), "A reply with no engagement.");
        reply = postRepository.save(reply);
    }

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
        UUID postId = postWithEntities.getId();
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
        assertEquals(2, post.entities().mentions().size()); // 2 related mentions
        assertEquals(1, post.entities().urls().size()); // 1 related url
    }

    /**
     * Test {@link PostRepository#findReplyPostsById(UUID, UUID, Pageable)} to
     * verify that searching for a posts' replies by its {@code id} returns a
     * {@link Page} of {@link PostDTO}.
     */
    @Test
    void PostRepository_findReplyPostsById_HasRepliesReturnsPageOfPostDto() {
        UUID postId = postWithReplies.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> replies = postRepository.findReplyPostsById(
            postId,
            authUserId,
            page);

        assertNotNull(replies);
        assertTrue(replies.hasContent());
        assertEquals(3, replies.getTotalElements());
    }

    /**
     * Test {@link PostRepository#findReplyPostsById(UUID, UUID, Pageable)} to
     * verify that searching for a posts' replies by its {@code id}, that has no
     * replies, returns an empty {@link Page}.
     */
    @Test
    void PostRepository_findReplyPostsById_HasNoRepliesReturnsEmptyPage() {
        UUID postId = postWithEntities.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> replies = postRepository.findReplyPostsById(
            postId,
            authUserId,
            page);

        assertNotNull(replies);
        assertTrue(replies.isEmpty());
        assertEquals(0, replies.getTotalElements());
    }

    /**
     * Test {@link PostRepository#findReplyPostsById(UUID, UUID, Pageable)} to
     * verify that searching for a posts' replies by its {@code id}, correctly ranks
     * the reply with an original poster response highest.
     */
    @Test
    void PostRepository_findReplyPostsById_ReplyWithOpResponseIsRankedHighest() {
        UUID postId = postWithReplies.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> replies = postRepository.findReplyPostsById(
            postId,
            authUserId,
            page);

        assertNotNull(replies);
        assertTrue(replies.hasContent());
        assertTrue(replies.getTotalElements() > 1);
        assertEquals("A reply with an OP response.", replies.getContent().getFirst().text());
    }

    /**
     * Test {@link PostRepository#findReplyPostsById(UUID, UUID, Pageable)} to
     * verify that searching for a posts' replies by its {@code id}, correctly ranks
     * the reply with engagement higher than those without any engagement.
     */
    @Test
    void PostRepository_findReplyPostsById_ReplyWithEngagementIsRankedHigherThanReplyWithoutEngagement() {
        UUID postId = postWithReplies.getId();
        UUID authUserId = self.getId();
        Pageable page = PageRequest.of(0, 10);

        Page<PostDTO> replies = postRepository.findReplyPostsById(
            postId,
            authUserId,
            page);

        assertNotNull(replies);
        assertTrue(replies.hasContent());
        assertTrue(replies.getTotalElements() > 1);
        assertEquals("A reply with a like.", replies.getContent().get(1).text());
        assertEquals("A reply with no engagement.", replies.getContent().get(2).text());
    }

}