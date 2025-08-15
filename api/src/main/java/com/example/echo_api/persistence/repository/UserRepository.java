package com.example.echo_api.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.echo_api.persistence.model.user.User;

@Repository
public interface UserRepository extends ListCrudRepository<User, UUID> {

    /**
     * Find a {@link User} by {@code externalId}.
     * 
     * @param externalId The {@code externalId} to search for
     * @return An {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    Optional<User> findByExternalId(String externalId);

    /**
     * Check if a {@link User} exists by {@code externalId}.
     * 
     * @param externalId The {@code externalId} to search for
     * @return True if exists, else false
     */
    boolean existsByexternalId(String externalId);

    /**
     * Deletes the {@link User} with the given {@code externalId}.
     * 
     * <p>
     * This action is idempotent.
     * 
     * @param externalId The {@code externalId} to search for
     * @return The number of records deleted (0 or 1)
     */
    int deleteByExternalId(String externalId);

}
