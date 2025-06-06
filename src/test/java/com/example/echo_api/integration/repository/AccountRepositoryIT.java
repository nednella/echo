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

    private Account account;

    /**
     * Save a {@link Account} object to the {@link AccountRepository}..
     */
    @BeforeAll
    void setup() {
        account = new Account("test", "password1");
        account = accountRepository.save(account);
    }

    /**
     * Test the {@link AccountRepository#findByUsername(String)} method to verify
     * that an account can be found by their username.
     */
    @Test
    void AccountRepository_FindByUsername_ReturnUser() {
        Optional<Account> optAccount = accountRepository.findByUsername(account.getUsername());

        assertNotNull(optAccount);
        assertTrue(optAccount.isPresent());
        assertEquals(account, optAccount.get());
    }

    /**
     * Test the {@link AccountRepository#findByUsername(String)} method to verify
     * that searching for a non-existent username returns an empty result.
     */
    @Test
    void AccountRepository_FindByUsername_ReturnEmpty() {
        Optional<Account> optAccount = accountRepository.findByUsername("nonExistentUser");

        assertNotNull(optAccount);
        assertTrue(optAccount.isEmpty());
    }

    /**
     * Test the {@link AccountRepository#existsByUsername(String)} method to verify
     * that the repository correctly identifies that an account exists when
     * searching for a valid username.
     */
    @Test
    void AccountRepository_ExistsByUsername_ReturnTrue() {
        boolean exists = accountRepository.existsByUsername(account.getUsername());

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
