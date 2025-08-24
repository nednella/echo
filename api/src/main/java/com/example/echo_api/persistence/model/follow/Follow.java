package com.example.echo_api.persistence.model.follow;

import java.time.Instant;
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
 * Entity class representing a {@link Profile} following relationship in the
 * application.
 */
@Entity
@Table
@Getter
@NoArgsConstructor
@IdClass(FollowPK.class)
public class Follow {

    @Id
    @Column(name = "follower_id", nullable = false, updatable = false)
    private UUID followerId;

    @Id
    @Column(name = "followed_id", nullable = false, updatable = false)
    private UUID followedId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Follow(UUID source, UUID target) {
        this.followerId = source;
        this.followedId = target;
    }

}
