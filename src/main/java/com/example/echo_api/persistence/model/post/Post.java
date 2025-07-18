package com.example.echo_api.persistence.model.post;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a user's post in the system.
 */
@Entity
@Table
@Getter
@NoArgsConstructor
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

    @Column(length = 140, nullable = false)
    private String text;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // ---- production constructors ----

    public Post(UUID parentId, UUID authorId, String text) {
        this.parentId = parentId;
        this.authorId = authorId;
        this.text = text;
    }

    public Post(UUID authorId, String text) {
        this(null, authorId, text);
    }

    // ---- test constructors ----

    public Post(UUID id, UUID parentId, UUID authorId, String text) {
        this.id = id;
        this.parentId = parentId;
        this.authorId = authorId;
        this.text = text;
    }

}
