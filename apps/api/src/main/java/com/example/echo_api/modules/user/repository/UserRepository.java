package com.example.echo_api.modules.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.echo_api.modules.user.entity.User;

@Repository
public interface UserRepository extends ListCrudRepository<User, UUID> {

    /**
     * Find a {@link User} by {@code externalId}.
     * 
     * @param externalId the {@code externalId} to query
     * @return {@link Optional} containing {@link User} if found, else empty
     */
    Optional<User> findByExternalId(String externalId);

    /**
     * Check if a {@link User} exists by {@code externalId}.
     * 
     * @param externalId the {@code externalId} to query
     * @return {@code true} if exists, else {@code false}
     */
    boolean existsByexternalId(String externalId);

    /**
     * Deletes the {@link User} with the given {@code externalId}.
     * 
     * <p>
     * This action is idempotent.
     * 
     * @param externalId the {@code externalId} to query
     * @return the number of records deleted (0 or 1)
     */
    int deleteByExternalId(String externalId);

}
