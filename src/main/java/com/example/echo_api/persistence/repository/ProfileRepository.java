package com.example.echo_api.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.example.echo_api.persistence.model.profile.Profile;

public interface ProfileRepository extends CrudRepository<Profile, UUID>, PagingAndSortingRepository<Profile, UUID> {

    /**
     * Find a {@link Profile} by {@code username}.
     * 
     * @param username The username to search for.
     * @return An {@link Optional} containing the {@link Profile} if found,
     *         otherwise empty.
     */
    Optional<Profile> findByUsername(String username);

    /**
     * Find all {@link Profile} for followers of the supplied {@code profileId}.
     * 
     * @param profileId The id of the profile to search against.
     * @return A list containing the profiles of matches if any exist, otherwise
     *         empty.
     */
    @Query("SELECT p FROM Profile p " +
        "JOIN Follow f ON p.id = f.followerId " +
        "WHERE f.followingId = :profileId")
    List<Profile> findAllFollowersById(@Param("profileId") UUID profileId);

    /**
     * Find all {@link Profile} for those followed by the supplied
     * {@code profileId}.
     * 
     * @param profileId The id of the profile to search against.
     * @return A list containing the profiles of matches if any exist, otherwise
     *         empty.
     */
    @Query("SELECT p FROM Profile p " +
        "JOIN Follow f ON p.id = f.followingId " +
        "WHERE f.followerId = :profileId")
    List<Profile> findAllFollowingById(@Param("profileId") UUID profileId);

}
