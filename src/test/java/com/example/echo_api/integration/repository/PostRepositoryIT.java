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
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.AccountRepository;
import com.example.echo_api.persistence.repository.PostEntityRepository;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.util.extractor.PostEntityExtractor;
import com.example.echo_api.util.pagination.OffsetLimitRequest;

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
    private PostEntityRepository postEntityRepository;

    private Profile self;
    private Profile notSelf;
    private Post selfPostWithReply;
    private Post replyPost;
    private Post postWithEntities;

    @BeforeAll
    void setup() {
        Account selfAcc = new Account("self", "password");
        accountRepository.save(selfAcc); // save account to repository to generate a UUID & foreign key
        self = new Profile(selfAcc.getId(), selfAcc.getUsername());
        self = profileRepository.save(self); // save profile to provide foreign key

        Account notSelfAcc = new Account("notSelf", "password");
        accountRepository.save(notSelfAcc); // save account to repository to generate a UUID & foreign key
        notSelf = new Profile(notSelfAcc.getId(), notSelfAcc.getUsername());
        notSelf = profileRepository.save(notSelf); // save profile to provide foreign key

        // save a post to db
        selfPostWithReply = new Post(self.getId(), "Test post.");
        selfPostWithReply = postRepository.save(selfPostWithReply);

        // save a post reply to db
        replyPost = new Post(selfPostWithReply.getId(), notSelf.getId(), "A reply test post.");
        replyPost = postRepository.save(replyPost);

        // save a post with text entities to db
        postWithEntities = new Post(self.getId(), "Hey @john_doe and @admin! Nice #SpringBoot app, github.com/repo");
        postWithEntities = postRepository.save(postWithEntities);
        postEntityRepository.saveAll(PostEntityExtractor.extract(postWithEntities.getId(), postWithEntities.getText()));
    }

    /**
     * Test {@link PostRepository#findPostDtoById(UUID, UUID)} to verify that a post
     * can be found by its {@code id}.
     */
    @Test
    void PostRepository_FindPostDtoById_ReturnPostDto() {
        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            replyPost.getId(),
            self.getId());

        assertNotNull(optPost);
        assertTrue(optPost.isPresent());

        PostDTO post = optPost.get();
        assertNotNull(post.metrics());
        assertNotNull(post.relationship());
        assertNotNull(post.author().relationship());
    }

    /**
     * Test {@link PostRepository#findPostDtoById(UUID, UUID)} to verify that a post
     * belonging to the authenticated user can be found by its {@code id}, and
     * returns a {@code null} author relationship object.
     */
    @Test
    void PostRepository_FindPostDtoById_ReturnPostDtoWithNullAuthorRelationshipDto() {
        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            selfPostWithReply.getId(),
            self.getId());

        assertNotNull(optPost);
        assertTrue(optPost.isPresent());

        PostDTO post = optPost.get();
        assertNotNull(post.metrics());
        assertNotNull(post.relationship());
        assertNull(post.author().relationship()); // author is self thus null
    }

    /**
     * Test {@link PostRepository#findPostDtoById(UUID, UUID)} to verify that
     * searching for a non-existent post {@code id} returns an empty optional.
     */
    @Test
    void PostRepository_FindPostDtoById_ReturnEmpty() {
        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            UUID.randomUUID(),
            self.getId());

        assertNotNull(optPost);
        assertTrue(optPost.isEmpty());
    }

    @Test
    void PostRepository_FindPostDtoById_ReturnsNoEntities() {
        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            selfPostWithReply.getId(),
            self.getId());

        assertNotNull(optPost);
        assertTrue(optPost.isPresent());

        PostDTO post = optPost.get();
        assertTrue(post.entities().hashtags().isEmpty()); // no related hashtags
        assertTrue(post.entities().mentions().isEmpty()); // no related mentions
    }

    @Test
    void PostRepository_FindPostDtoById_ReturnsMultipleEntities() {
        Optional<PostDTO> optPost = postRepository.findPostDtoById(
            postWithEntities.getId(),
            self.getId());

        assertNotNull(optPost);
        assertTrue(optPost.isPresent());

        PostDTO post = optPost.get();
        assertEquals(1, post.entities().hashtags().size()); // 1 related hashtag
        assertEquals(2, post.entities().mentions().size()); // 2 related mentions
        assertEquals(1, post.entities().urls().size()); // 1 related url
    }

    /**
     * Test {@link PostRepository#findReplyDtosById(UUID, UUID, Pageable)} to verify
     * that searching for a posts' replies by its {@code id} returns a {@link Page}
     * of {@link PostDTO}.
     */
    @Test
    void PostRepository_FindReplyDtosById_ReturnPageOfPostDto() {
        Pageable page = new OffsetLimitRequest(0, 10);

        Page<PostDTO> repliesPage = postRepository.findReplyDtosById(
            selfPostWithReply.getId(),
            self.getId(),
            page);

        assertNotNull(repliesPage);
        assertTrue(repliesPage.hasContent());
        assertEquals(1, repliesPage.getTotalElements());
    }

    /**
     * Test {@link PostRepository#findReplyDtosById(UUID, UUID, Pageable)} to verify
     * that searching for a posts' replies by its {@code id}, that has no replies,
     * returns an empty {@link Page}.
     */
    @Test
    void PostRepository_FindReplyDtosById_ReturnPageOfEmpty() {
        Pageable page = new OffsetLimitRequest(0, 10);

        Page<PostDTO> repliesPage = postRepository.findReplyDtosById(
            replyPost.getId(),
            self.getId(),
            page);

        assertNotNull(repliesPage);
        assertTrue(repliesPage.isEmpty());
        assertEquals(0, repliesPage.getTotalElements());
    }

}
