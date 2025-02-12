package com.example.echo_api.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.AccountRepository;
import com.example.echo_api.persistence.repository.FollowRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;

/**
 * Integration test class for {@link FollowRepository}.
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class FollowRepositoryIT extends RepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private FollowRepository followRepository;

    private Profile source;
    private Profile target;

    @BeforeAll
    void setup() {
        Account sourceAcc = new Account("source", "test");
        accountRepository.save(sourceAcc); // save account to repository to generate a UUID
        source = new Profile(sourceAcc);
        profileRepository.save(source); // save profile to provide foreign key for follow table

        Account targetAcc = new Account("target", "test");
        accountRepository.save(targetAcc); // save account to repository to generate a UUID
        target = new Profile(targetAcc);
        profileRepository.save(target); // save profile to provide foreign key for follow table

        // save a follow to db
        Follow follow = new Follow(source, target);
        followRepository.save(follow);
    }

    /**
     * Test the
     * {@link FollowRepository#existsByFollowerIdAndFollowingId(java.util.UUID, java.util.UUID)}
     * method to verify that the repository correctly identifies that a
     * {@link Follow} relationship exists between the supplied source and target
     * profile UUIDs.
     */
    @Test
    void FollowRepository_ExistsByFollowerIdAndFollowingId_ReturnTrue() {
        boolean exists = followRepository.existsByFollowerIdAndFollowingId(source.getProfileId(),
            target.getProfileId());

        assertTrue(exists);
    }

    /**
     * Test the
     * {@link FollowRepository#existsByFollowerIdAndFollowingId(java.util.UUID, java.util.UUID)}
     * method to verify that the repository correctly identifies that a
     * {@link Follow} relationship does not exist between the supplied source and
     * target profile UUIDs.
     */
    @Test
    void FollowRepository_ExistsByFollowerIdAndFollowingId_ReturnFalse() {
        boolean exists = followRepository.existsByFollowerIdAndFollowingId(UUID.randomUUID(), UUID.randomUUID());

        assertFalse(exists);
    }

    /**
     * Test ensures {@link FollowRepository#countFollowers(UUID)} returns a count of
     * 0 when supplied with an id of a profile which has no followers.
     */
    @Test
    void FollowRepository_CountFollowers_Return0() {
        int followers = followRepository.countFollowers(source.getProfileId());
        assertEquals(0, followers);
    }

    /**
     * Test ensures {@link FollowRepository#countFollowers(UUID)} returns a count of
     * 1 when supplied with an id of a profile which has 1 follower.
     */
    @Test
    void FollowRepository_CountFollowers_Return1() {
        int followers = followRepository.countFollowers(target.getProfileId());
        assertEquals(1, followers);
    }

    /**
     * Test ensures {@link FollowRepository#countFollowing(UUID)} returns a count of
     * 0 when supplied with an id of a profile which has no following.
     */
    @Test
    void FollowRepository_CountFollowing_Return0() {
        int following = followRepository.countFollowing(target.getProfileId());
        assertEquals(0, following);
    }

    /**
     * Test ensures {@link FollowRepository#countFollowing(UUID)} returns a count of
     * 1 when supplied with an id of a profile which has 1 following.
     */
    @Test
    void FollowRepository_CountFollowing_Return1() {
        int following = followRepository.countFollowing(source.getProfileId());
        assertEquals(1, following);
    }

}
