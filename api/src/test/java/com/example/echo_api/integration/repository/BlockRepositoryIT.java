package com.example.echo_api.integration.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.persistence.repository.BlockRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.persistence.repository.UserRepository;

/**
 * Integration test class for {@link BlockRepository}.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BlockRepositoryIT extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BlockRepository blockRepository;

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

        Block block = new Block(source.getId(), target.getId());
        blockRepository.save(block);
    }

    /**
     * Test the {@link BlockRepository#existsByBlockerIdAndBlockedId(UUID, UUID)}
     * method to verify that the repository correctly identifies that a
     * {@link Block} relationship exists between the supplied source and target
     * profile UUIDs.
     */
    @Test
    void BlockRepository_ExistsByBlockerIdAndBlockedId_ReturnTrue() {
        boolean exists = blockRepository.existsByBlockerIdAndBlockedId(source.getId(), target.getId());

        assertTrue(exists);
    }

    /**
     * Test the {@link BlockRepository#existsByBlockerIdAndBlockedId(UUID, UUID)}
     * method to verify that the repository correctly identifies that a
     * {@link Block} relationship does not exist between the supplied source and
     * target profile UUIDs.
     */
    @Test
    void BlockRepository_ExistsByBlockerIdAndBlockedId_ReturnFalse() {
        boolean exists = blockRepository.existsByBlockerIdAndBlockedId(UUID.randomUUID(), UUID.randomUUID());

        assertFalse(exists);
    }

    /**
     * Test the {@link BlockRepository#existsAnyBlockBetween(UUID, UUID)} method to
     * verify that the repository correctly identifies that a {@link Block}
     * relationship exists (in any or both directions) between the supplied profile
     * UUIDs.
     */
    @Test
    void BlockRepository_ExistsAnyBlockBetween_ReturnTrue() {
        boolean exists = blockRepository.existsAnyBlockBetween(source.getId(), target.getId());
        boolean existsOtherWayAround = blockRepository.existsAnyBlockBetween(target.getId(), source.getId());

        assertTrue(exists);
        assertTrue(existsOtherWayAround);
    }

    /**
     * Test the {@link BlockRepository#existsAnyBlockBetween(UUID, UUID)} method to
     * verify that the repository correctly identifies that a {@link Block}
     * relationship does not exist (in any direction) between the supplied profile
     * UUIDs.
     */
    @Test
    void BlockRepository_ExistsAnyBlockBetween_ReturnFalse() {
        boolean exists = blockRepository.existsAnyBlockBetween(UUID.randomUUID(), UUID.randomUUID());

        assertFalse(exists);
    }

    /**
     * Test the {@link BlockRepository#deleteByBlockerIdAndBlockedId(UUID, UUID)}
     * method to verify that the repository correctly deletes 1 {@link Block}
     * relationship established between the supplied profile UUIDs.
     */
    @Test
    @Transactional
    void BlockRepository_DeleteByBlockerIdAndBlockedId_Return1() {
        int deletedCount = blockRepository.deleteByBlockerIdAndBlockedId(source.getId(), target.getId());

        assertEquals(1, deletedCount);
    }

}
