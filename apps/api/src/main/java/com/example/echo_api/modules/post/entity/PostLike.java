package com.example.echo_api.modules.post.entity;

import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

import com.example.echo_api.modules.profile.entity.Profile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a like relationship between a {@link Profile} and
 * {@link Post} in the application.
 */
@Entity
@Table(name = "post_likes")
@Getter
@NoArgsConstructor
@IdClass(PostLikePK.class)
public class PostLike {

    @Id
    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Id
    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public PostLike(UUID postId, UUID authorId) {
        this.postId = postId;
        this.authorId = authorId;
    }

}
