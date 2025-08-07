package com.example.echo_api.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.echo_api.persistence.model.user.User;

@Repository
public interface UserRepository extends ListCrudRepository<User, UUID> {

    /**
     * Find a {@link User} by {@code clerkId}.
     * 
     * @param clerkId The {@code clerkId} to search for
     * @return An {@link Optional} containing the {@link User} if found, otherwise
     *         empty
     */
    Optional<User> findByClerkId(String clerkId);

    /**
     * Find a user {@link UUID} by {@code clerkId}.
     * 
     * @param clerkId The {@code clerkId} to search for
     * @return An {@link Optional} containing the user's {@link UUID} if found,
     *         otherwise empty
     */
    @Query("SELECT u.id FROM User u WHERE u.clerkId = :clerkId")
    Optional<UUID> findIdByClerkId(String clerkId);

    /**
     * Check if a {@link User} exists by {@code clerkId}.
     * 
     * @param clerkId The {@code clerkId} to search for
     * @return True if exists, else false
     */
    boolean existsByClerkId(String clerkId);

    /**
     * Deletes the {@link User} with the given {@code clerkId}.
     * 
     * @param clerkId The clerk id of the user
     * @return The number of records deleted (0 or 1)
     */
    int deleteByClerkId(String clerkId);

}
