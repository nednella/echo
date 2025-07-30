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
     * Find a user {@link UUID} by {@code clerkId}.
     * 
     * @param clerkId The clerk id to search for.
     * @return An {@link Optional} containing the user's {@link UUID} if found,
     *         otherwise empty.
     */
    @Query("SELECT u.id FROM User u WHERE u.clerkId = :clerkId")
    Optional<UUID> findIdByClerkId(String clerkId);

}
