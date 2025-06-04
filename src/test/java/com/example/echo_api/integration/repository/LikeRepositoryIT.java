package com.example.echo_api.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.like.Like;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.AccountRepository;
import com.example.echo_api.persistence.repository.LikeRepository;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;

/**
 * Integration test class for {@link LikeRepository}.
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class LikeRepositoryIT extends RepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    private Profile user;
    private Post postWithLike;
    private Post postNoLikes;

    @BeforeAll
    void setup() {
        Account userAcc = new Account("user", "password");
        accountRepository.save(userAcc); // save account to repository to generate a UUID & foreign key
        user = new Profile(userAcc.getId(), userAcc.getUsername());
        user = profileRepository.save(user); // save profile to provide foreign key

        // save 2 posts to db
        postWithLike = new Post(user.getId(), "Test post with 1 like.");
        postWithLike = postRepository.save(postWithLike);

        postNoLikes = new Post(user.getId(), "Test post with 0 likes.");
        postNoLikes = postRepository.save(postNoLikes);

        // save a post like to db
        likeRepository.save(new Like(postWithLike.getId(), user.getId()));
    }

    /**
     * Test {@link LikeRepository#existsByPostIdAndAuthorId(UUID, UUID)} to verify
     * that it returns {@code true} in cases where a like exists between the
     * supplied {@code postId} and {@code authorId}.
     */
    @Test
    void LikeRepository_ExistsByPostIdAndAuthorId_ReturnTrue() {
        boolean exists = likeRepository.existsByPostIdAndAuthorId(postWithLike.getId(), user.getId());

        assertTrue(exists);
    }

    /**
     * Test {@link LikeRepository#existsByPostIdAndAuthorId(UUID, UUID)} to verify
     * that it returns {@code false} in cases where no like exists between the
     * supplied {@code postId} and {@code authorId}.
     */
    @Test
    void LikeRepository_ExistsByPostIdAndAuthorId_ReturnFalse() {
        boolean exists = likeRepository.existsByPostIdAndAuthorId(postNoLikes.getId(), user.getId());

        assertFalse(exists);
    }

    /**
     * Test {@link LikeRepository#deleteByPostIdAndAuthorId(UUID, UUID)} to verify
     * that it returns {@code 1} in cases where a like exists between the supplied
     * {@code postId} and {@code authorId}.
     */
    @Test
    @Transactional
    void LikeRepository_DeleteByPostIdAndAuthorId_Return1() {
        int deletedRows = likeRepository.deleteByPostIdAndAuthorId(postWithLike.getId(), user.getId());

        assertEquals(1, deletedRows);
    }

    /**
     * Test {@link LikeRepository#deleteByPostIdAndAuthorId(UUID, UUID)} to verify
     * that it returns {@code 0} in cases where no like exists between the supplied
     * {@code postId} and {@code authorId}.
     */
    @Test
    void LikeRepository_DeleteByPostIdAndAuthorId_Return0() {
        int deletedRows = likeRepository.deleteByPostIdAndAuthorId(postNoLikes.getId(), user.getId());

        assertEquals(0, deletedRows);
    }

}
