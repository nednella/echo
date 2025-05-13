package com.example.echo_api.persistence.model.like;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import com.example.echo_api.persistence.model.profile.Profile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a {@link Like} relationship between a
 * {@link Profile} and {@link Post} in the application.
 */
@Entity
@Table(name = "post_like")
@Getter
@NoArgsConstructor
@IdClass(LikePK.class)
public class Like {

    @Id
    @Column(name = "post_id")
    private UUID postId;

    @Id
    @Column(name = "author_id")
    private UUID authorId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Like(UUID postId, UUID authorId) {
        this.postId = postId;
        this.authorId = authorId;
    }

}
