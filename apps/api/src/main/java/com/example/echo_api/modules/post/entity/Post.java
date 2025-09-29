package com.example.echo_api.modules.post.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.example.echo_api.util.Utils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a user's post in the system.
 */
@Entity
@Table
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "parent_id")
    private UUID parentId;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "conversation_id", nullable = false)
    private UUID conversationId;

    @Column(length = 280, nullable = false)
    private String text;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // ---- factory methods ----

    /**
     * Factory method to create a new {@link Post} with the supplied arguments.
     * 
     * @param parentId the UUID of the parent post (nullable)
     * @param authorId the UUID of the author
     * @param text     the post text content
     * @return new {@link Post} instance
     * @throws IllegalArgumentException if {@code authorId} or {@code text} is null
     */
    public static Post create(UUID parentId, UUID authorId, String text) {
        return Post.builder()
            .parentId(parentId)
            .authorId(Utils.checkNotNull(authorId, "Author ID"))
            .text(Utils.checkNotNull(text, "Text"))
            .build();
    }

    /**
     * Factory method to create a new {@link Post} for <b>testing only</b>, in cases
     * where an id is required to be set.
     * 
     * @param id       the placeholder UUID
     * @param parentId the placeholder UUID of the parent post (nullable)
     * @param authorId the placeholder UUID of the author
     * @param text     the placeholder post text content
     * @return new {@link Post} instance
     * @throws IllegalArgumentException if {@code id} {@code authorId} or
     *                                  {@code text} is null
     */
    public static Post forTest(UUID id, UUID parentId, UUID authorId, String text) {
        return Post.builder()
            .id(Utils.checkNotNull(id, "ID"))
            .parentId(parentId)
            .authorId(Utils.checkNotNull(authorId, "Author ID"))
            .text(Utils.checkNotNull(text, "Text"))
            .build();
    }

}
