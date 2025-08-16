package com.example.echo_api.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.persistence.repository.FollowRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.persistence.repository.UserRepository;

/**
 * Integration test class for {@link FollowRepository}.
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class FollowRepositoryIT extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private FollowRepository followRepository;

    private Profile source;
    private Profile target;

    @BeforeAll
    void setup() {
        User sourceU = User.fromExternalSource("placeholderExtId1");
        User targetU = User.fromExternalSource("placeholderExtId2");
        userRepository.saveAll(List.of(sourceU, targetU));

        source = Profile.forTest(sourceU.getId(), "source");
        target = Profile.forTest(targetU.getId(), "target");
        profileRepository.saveAll(List.of(source, target));

        // save a follow to db
        Follow follow = new Follow(source.getId(), target.getId());
        Follow followOtherWayAround = new Follow(target.getId(), source.getId());
        followRepository.saveAll(List.of(follow, followOtherWayAround));
    }

    /**
     * Test the {@link FollowRepository#existsByFollowerIdAndFollowedId(UUID, UUID)}
     * method to verify that the repository correctly identifies that a
     * {@link Follow} relationship exists between the supplied source and target
     * profile UUIDs.
     */
    @Test
    void FollowRepository_ExistsByFollowerIdAndFollowedId_ReturnTrue() {
        boolean exists = followRepository.existsByFollowerIdAndFollowedId(source.getId(), target.getId());
        boolean existsOtherWayAround = followRepository.existsByFollowerIdAndFollowedId(target.getId(), source.getId());

        assertTrue(exists);
        assertTrue(existsOtherWayAround);
    }

    /**
     * Test the {@link FollowRepository#existsByFollowerIdAndFollowedId(UUID, UUID)}
     * method to verify that the repository correctly identifies that a
     * {@link Follow} relationship does not exist between the supplied source and
     * target profile UUIDs.
     */
    @Test
    void FollowRepository_ExistsByFollowerIdAndFollowedId_ReturnFalse() {
        boolean exists = followRepository.existsByFollowerIdAndFollowedId(UUID.randomUUID(), UUID.randomUUID());

        assertFalse(exists);
    }

    /**
     * Test the {@link FollowRepository#deleteByFollowerIdAndFollowedId(UUID, UUID)}
     * method to verify that the repository correctly deletes 1 {@link Follow}
     * relationship established between the supplied profile UUIDs.
     */
    @Test
    @Transactional
    void FollowRepository_DeleteByFollowingIdAndFolloedId_Return1() {
        int deletedCount = followRepository.deleteByFollowerIdAndFollowedId(source.getId(), target.getId());

        assertEquals(1, deletedCount);
    }

    /**
     * Test the {@link FollowRepository#deleteAnyFollowIfExists(UUID, UUID)} method
     * to verify that the repository correctly deletes 2 {@link Follow}
     * relationships established between the supplied profile UUIDs.
     */
    @Test
    @Transactional
    void FollowRepository_DeleteAnyFollowIfExistsBetween_Return2() {
        int deletedCount = followRepository.deleteAnyFollowIfExistsBetween(source.getId(), target.getId());

        assertEquals(2, deletedCount);
    }

}
