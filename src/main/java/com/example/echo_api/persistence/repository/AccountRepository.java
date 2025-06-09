package com.example.echo_api.persistence.repository;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.echo_api.persistence.model.account.Account;

import java.util.Optional;

@Repository
public interface AccountRepository extends ListCrudRepository<Account, UUID> {

    /**
     * Find a {@link Account} by {@code username}.
     * 
     * @param username The username to search for.
     * @return An {@link Optional} containing the {@link Account} if found,
     *         otherwise empty.
     */
    Optional<Account> findByUsername(String username);

    /**
     * Check if an {@link Account} exists by {@code username}.
     * 
     * @param username The username to search for.
     * @return True if exists, else false.
     */
    boolean existsByUsername(String username);

}
