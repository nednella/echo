package com.example.echo_api.persistence.repository;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.follow.FollowPK;

@Repository
public interface FollowRepository extends ListCrudRepository<Follow, FollowPK> {

    /**
     * Check if a unidirectional follow exists from the follower to the followed
     * profile id.
     * 
     * @param followerId the id of the profile initiating the follow
     * @param followedId the id of the profile being followed
     * @return {@code true} if a unidirectional follow exists from follower to
     *         followed, else {@code false}
     */
    boolean existsByFollowerIdAndFollowedId(UUID followerId, UUID followedId);

    /**
     * Delete the follow if one exists from the follower to followed profile id.
     * <p>
     * This action is idempotent.
     * 
     * @param followerId the id of the profile following
     * @param followedId the id of the profile being followed
     * @return the number of records deleted (0 or 1)
     */
    int deleteByFollowerIdAndFollowedId(UUID followerId, UUID followedId);

}
