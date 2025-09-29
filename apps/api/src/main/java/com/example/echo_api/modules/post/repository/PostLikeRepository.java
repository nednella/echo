package com.example.echo_api.modules.post.repository;

import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import com.example.echo_api.modules.post.entity.PostLike;
import com.example.echo_api.modules.post.entity.PostLikePK;

@Repository
public interface PostLikeRepository extends ListCrudRepository<PostLike, PostLikePK> {

    /**
     * Check if a post like exists between a post id and a profile id.
     * 
     * @param postId   the id of the post to query
     * @param authorId the id of the profile to query
     * @return {@code true} if exists, else {@code false}
     */
    boolean existsByPostIdAndAuthorId(UUID postId, UUID authorId);

    /**
     * Deletes any existing like relationship between a post and a profile.
     * 
     * <p>
     * This operation is idempotent.
     * 
     * @param postId   the id of the post to unlike
     * @param authorId the id of the profile unliking the post
     * @return the number of records deleted (0 or 1)
     */
    int deleteByPostIdAndAuthorId(UUID postId, UUID authorId);

}
