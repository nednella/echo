package com.example.echo_api.persistence.repository;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

import com.example.echo_api.persistence.model.like.Like;
import com.example.echo_api.persistence.model.like.LikePK;

public interface LikeRepository extends ListCrudRepository<Like, LikePK> {

    /**
     * Check if a post like exists between a post id and a profile id.
     * 
     * @param postId   The id of the post being checked.
     * @param authorId The id of the profile being checked.
     * @return {@code true} if a like exists, else {@code false}.
     */
    boolean existsByPostIdAndAuthorId(UUID postId, UUID authorId);

    /**
     * Deletes any existing like relationship between a post and a profile. This
     * operation is idempotent.
     * 
     * @param postId   The id of the post to unlike.
     * @param authorId The id of the profile unliking the post.
     * @return The number of like records deleted (0 or 1).
     */
    int deleteByPostIdAndAuthorId(UUID postId, UUID authorId);

}
