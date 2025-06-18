package com.example.echo_api.persistence.model.post.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@MappedSuperclass
@IdClass(PostEntityPK.class)
public abstract class PostEntity {

    @Id
    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Id
    @Column(name = "start_index", nullable = false)
    private int start;

    @Column(name = "end_index", nullable = false)
    private int end;

    @Column(nullable = false)
    private String text;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected PostEntity(UUID postId, int start, int end, String text) {
        this.postId = postId;
        this.start = start;
        this.end = end;
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text + " (" + this.start + ":" + this.end + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (this.getClass() != o.getClass())
            return false;

        PostEntity that = (PostEntity) o;
        return this.start == that.start &&
            this.end == that.end &&
            this.postId.equals(that.postId) &&
            this.text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, start, end, text);
    }

}
