package com.example.echo_api.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.echo_api.persistence.model.user.User;

@Repository
public interface UserRepository extends ListCrudRepository<User, UUID> {

    /**
     * Find a {@link User} by {@code clerkId}.
     * 
     * @param username The clerk id to search for.
     * @return An {@link Optional} containing the {@link User} if found,
     *         otherwise empty.
     */
    Optional<User> findByClerkId(String clerkId);

    /**
     * Find a {@link User} by {@code username}.
     * 
     * @param username The username to search for.
     * @return An {@link Optional} containing the {@link User} if found,
     *         otherwise empty.
     */
    Optional<User> findByUsername(String username);

}
