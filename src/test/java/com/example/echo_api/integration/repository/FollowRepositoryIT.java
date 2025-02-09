package com.example.echo_api.integration.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

}
