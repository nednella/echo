package com.example.echo_api.integration.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.AccountRepository;
import com.example.echo_api.persistence.repository.BlockRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;

/**
 * Integration test class for {@link BlockRepository}.
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BlockRepositoryIT extends RepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private BlockRepository blockRepository;

    private Profile source;
    private Profile target;

    @BeforeAll
    void setup() {
        Account sourceAcc = new Account("source", "test");
        accountRepository.save(sourceAcc); // save account to generate a UUID
        source = new Profile(sourceAcc.getId(), sourceAcc.getUsername());
        profileRepository.save(source); // save profile to provide foreign key for block table

        Account targetAcc = new Account("target", "test");
        accountRepository.save(targetAcc); // save account to generate a UUID
        target = new Profile(targetAcc.getId(), targetAcc.getUsername());
        profileRepository.save(target); // save profile to provide foreign key for block table

        Block block = new Block(source.getId(), target.getId());
        blockRepository.save(block);
    }

    /**
     * Test the {@link BlockRepository#existsByBlockerIdAndBlockingId(UUID, UUID)}
     * method to verify that the repository correctly identifies that a
     * {@link Block} relationship exists between the supplied source and target
     * profile UUIDs.
     */
    @Test
    void BlockRepository_ExistsByBlockerIdAndBlockingId_ReturnTrue() {
        boolean exists = blockRepository.existsByBlockerIdAndBlockingId(source.getId(), target.getId());

        assertTrue(exists);
    }

    /**
     * Test the {@link BlockRepository#existsByBlockerIdAndBlockingId(UUID, UUID)}
     * method to verify that the repository correctly identifies that a
     * {@link Block} relationship does not exist between the supplied source and
     * target profile UUIDs.
     */
    @Test
    void BlockRepository_ExistsByBlockerIdAndBlockingId_ReturnFalse() {
        boolean exists = blockRepository.existsByBlockerIdAndBlockingId(UUID.randomUUID(), UUID.randomUUID());

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

}
