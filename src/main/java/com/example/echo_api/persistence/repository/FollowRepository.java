package com.example.echo_api.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.follow.FollowPK;

public interface FollowRepository extends ListCrudRepository<Follow, FollowPK> {

    /**
     * Check if a follow relationship exists between two profiles.
     * 
     * @param followerId  The id of the source profile.
     * @param followingId The id of the target profile.
     * @return True if the relationship exists, otherwise False.
     */
    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    /**
     * Count the number of profiles a given profile is followed by.
     * 
     * @param followingId The id of the profile.
     * @return The number of followers the profile has.
     */
    @Query("SELECT COUNT(*) FROM Follow WHERE followingId = :followingId")
    int countFollowers(UUID followingId);

    /**
     * Count the number of profiles a given profile is following.
     * 
     * @param followerId The id of the profile.
     * @return The number of profiles the profile is following.
     */
    @Query("SELECT COUNT(*) FROM Follow WHERE followerId = :followerId")
    int countFollowing(UUID followerId);

}
