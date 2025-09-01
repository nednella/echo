package com.example.echo_api.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.model.post.like.PostLike;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.persistence.repository.PostLikeRepository;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.persistence.repository.UserRepository;

/**
 * Integration test class for {@link PostLikeRepository}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PostLikeRepositoryIT extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository likeRepository;

    private Profile profile;
    private Post postWithLike;
    private Post postNoLikes;

    @BeforeAll
    void setup() {
        User user = User.fromExternalSource("placeholderExtId1");
        user = userRepository.save(user);
        profile = Profile.forTest(user.getId(), "username");
        profile = profileRepository.save(profile);

        // save 2 posts to db
        postWithLike = new Post(user.getId(), "Test post with 1 like.");
        postWithLike = postRepository.save(postWithLike);

        postNoLikes = new Post(user.getId(), "Test post with 0 likes.");
        postNoLikes = postRepository.save(postNoLikes);

        // save a post like to db
        likeRepository.save(new PostLike(postWithLike.getId(), user.getId()));
    }

    /**
     * Test {@link PostLikeRepository#existsByPostIdAndAuthorId(UUID, UUID)} to
     * verify that it returns {@code true} in cases where a like exists between the
     * supplied {@code postId} and {@code authorId}.
     */
    @Test
    void LikeRepository_ExistsByPostIdAndAuthorId_ReturnTrue() {
        boolean exists = likeRepository.existsByPostIdAndAuthorId(postWithLike.getId(), profile.getId());

        assertTrue(exists);
    }

    /**
     * Test {@link PostLikeRepository#existsByPostIdAndAuthorId(UUID, UUID)} to
     * verify that it returns {@code false} in cases where no like exists between
     * the supplied {@code postId} and {@code authorId}.
     */
    @Test
    void LikeRepository_ExistsByPostIdAndAuthorId_ReturnFalse() {
        boolean exists = likeRepository.existsByPostIdAndAuthorId(postNoLikes.getId(), profile.getId());

        assertFalse(exists);
    }

    /**
     * Test {@link PostLikeRepository#deleteByPostIdAndAuthorId(UUID, UUID)} to
     * verify that it returns {@code 1} in cases where a like exists between the
     * supplied {@code postId} and {@code authorId}.
     */
    @Test
    @Transactional
    void LikeRepository_DeleteByPostIdAndAuthorId_Return1() {
        int deletedRows = likeRepository.deleteByPostIdAndAuthorId(postWithLike.getId(), profile.getId());

        assertEquals(1, deletedRows);
    }

    /**
     * Test {@link PostLikeRepository#deleteByPostIdAndAuthorId(UUID, UUID)} to
     * verify that it returns {@code 0} in cases where no like exists between the
     * supplied {@code postId} and {@code authorId}.
     */
    @Test
    void LikeRepository_DeleteByPostIdAndAuthorId_Return0() {
        int deletedRows = likeRepository.deleteByPostIdAndAuthorId(postNoLikes.getId(), profile.getId());

        assertEquals(0, deletedRows);
    }

}
