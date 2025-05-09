package com.example.echo_api.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.follow.FollowPK;

public interface FollowRepository extends ListCrudRepository<Follow, FollowPK> {

    /**
     * Check if a follow relationship exists between two profiles.
     * 
     * @param followerId  The id of the profile initiating the follow.
     * @param followingId The id of the profile being followed.
     * @return True if a unidirectional follow exists from blocker to blocking, else
     *         false.
     */
    boolean existsByFollowerIdAndFollowingId(UUID followerId, UUID followingId);

    /**
     * Delete any follow relationships that exist between the supplied profile ids
     * (unidirectional or mutual).
     * 
     * @param profileId1 The id of the first user.
     * @param profileId2 The id of the second user.
     */
    @Modifying
    @Query("""
        DELETE FROM Follow f
        WHERE (f.followerId = :profileId1 AND f.followingId = :profileId2)
           OR (f.followerId = :profileId2 AND f.followingId = :profileId1)
        """)
    void deleteAnyFollowIfExists(@Param("profileId1") UUID profileId1, @Param("profileId2") UUID profileId2);

}
