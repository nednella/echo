package com.example.echo_api.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.util.pagination.OffsetLimitRequest;
import com.example.echo_api.persistence.repository.AccountRepository;
import com.example.echo_api.persistence.repository.FollowRepository;

/**
 * Integration test class for {@link ProfileRepository}.
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ProfileRepositoryIT extends RepositoryTest {

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
     * Test {@link ProfileRepository#findByUsername(String)} to verify a profile can
     * be found by their username.
     */
    @Test
    void ProfileRepository_FindByUsername_ReturnProfile() {
        Optional<Profile> foundProfile = profileRepository.findByUsername(source.getUsername());

        assertNotNull(foundProfile);
        assertTrue(foundProfile.isPresent());
    }

    /**
     * Test {@link ProfileRepository#findByUsername(String)} to verify that
     * searching for a non-existent username returns an empty result.
     */
    @Test
    void ProfileRepository_FindByUsername_ReturnEmpty() {
        Optional<Profile> foundProfile = profileRepository.findByUsername("non-existent-username");

        assertNotNull(foundProfile);
        assertTrue(foundProfile.isEmpty());
    }

    @Test
    @Transactional
    void ProfileRepository_FindAllFollowersById_ReturnListOfEmpty() {
        Pageable page = new OffsetLimitRequest(0, 1);
        Page<Profile> followers = profileRepository.findAllFollowersById(source.getProfileId(), page);

        assertTrue(followers.getContent().isEmpty());
    }

    @Test
    @Transactional
    void ProfileRepository_FindAllFollowersById_ReturnListOfProfile() {
        Pageable page = new OffsetLimitRequest(0, 1);
        Page<Profile> followers = profileRepository.findAllFollowersById(target.getProfileId(), page);

        assertFalse(followers.isEmpty());
        assertEquals(1, followers.getContent().size());
        assertEquals(source.getProfileId(), followers.getContent().get(0).getProfileId());
    }

    @Test
    @Transactional
    void ProfileRepository_FindAllFollowingById_ReturnListOfEmpty() {
        Pageable page = new OffsetLimitRequest(0, 1);
        Page<Profile> following = profileRepository.findAllFollowingById(target.getProfileId(), page);

        assertTrue(following.getContent().isEmpty());
    }

    @Test
    @Transactional
    void ProfileRepository_FindAllFollowingById_ReturnListOfProfile() {
        Pageable page = new OffsetLimitRequest(0, 1);
        Page<Profile> following = profileRepository.findAllFollowingById(source.getProfileId(), page);

        assertFalse(following.isEmpty());
        assertEquals(1, following.getContent().size());
        assertEquals(target.getProfileId(), following.getContent().get(0).getProfileId());
    }

}
