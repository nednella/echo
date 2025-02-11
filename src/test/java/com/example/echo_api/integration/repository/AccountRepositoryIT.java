package com.example.echo_api.integration.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import com.example.echo_api.integration.util.RepositoryTest;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.repository.AccountRepository;

/**
 * Integration test class for {@link AccountRepository}.
 */
@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class AccountRepositoryIT extends RepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Save a {@link Account} object to the {@link AccountRepository}..
     */
    @BeforeAll
    void setup() {
        testAccount = new Account("test", "password1");
        accountRepository.save(testAccount);
    }

    /**
     * Test the {@link AccountRepository#findByUsername(String)} method to verify
     * that an account can be found by their username.
     */
    @Test
    void AccountRepository_FindByUsername_ReturnUser() {
        Optional<Account> account = accountRepository.findByUsername(testAccount.getUsername());

        assertNotNull(account);
        assertTrue(account.isPresent());
        assertEquals(testAccount, account.get());
    }

    /**
     * Test the {@link AccountRepository#findByUsername(String)} method to verify
     * that searching for a non-existent username returns an empty result.
     */
    @Test
    void AccountRepository_FindByUsername_ReturnEmpty() {
        Optional<Account> account = accountRepository.findByUsername("nonExistentUser");

        assertNotNull(account);
        assertTrue(account.isEmpty());
    }

    /**
     * Test the {@link AccountRepository#existsByUsername(String)} method to verify
     * that the repository correctly identifies that an account exists when
     * searching for a valid username.
     */
    @Test
    void AccountRepository_ExistsByUsername_ReturnTrue() {
        boolean exists = accountRepository.existsByUsername(testAccount.getUsername());

        assertTrue(exists);
    }

    /**
     * Test the {@link AccountRepository#existsByUsername(String)} method to verify
     * that the repository correctly identifies that a user does not exist when
     * searching for a non-existent username.
     */
    @Test
    void AccountRepository_ExistsByUsername_ReturnFalse() {
        boolean exists = accountRepository.existsByUsername("nonExistentUser");

        assertFalse(exists);
    }

}
