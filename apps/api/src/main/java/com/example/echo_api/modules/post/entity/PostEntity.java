package com.example.echo_api.modules.post.entity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_entities")
@Getter
@NoArgsConstructor
@IdClass(PostEntityPK.class)
public class PostEntity {

    @Id
    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Id
    @Column(name = "entity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private PostEntityType type;

    @Id
    @Column(name = "start_index", nullable = false)
    private int start;

    @Column(name = "end_index", nullable = false)
    private int end;

    @Column(nullable = false)
    private String text;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public PostEntity(UUID postId, PostEntityType type, int start, int end, String text) {
        this.postId = postId;
        this.type = type;
        this.start = start;
        this.end = end;
        this.text = text;
    }

    @Override
    public String toString() {
        return text + " (" + type + ") [" + start + "," + end + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PostEntity))
            return false;

        PostEntity that = (PostEntity) o;

        return this.postId.equals(that.postId) &&
            this.type.equals(that.type) &&
            this.start == that.start &&
            this.end == that.end &&
            this.text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, type, start, end, text);
    }

}
