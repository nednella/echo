package com.example.echo_api.modules.profile.entity;

import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a follow relationship between two {@link Profile}
 * in the application.
 */
@Entity
@Table(name = "profile_follows")
@Getter
@NoArgsConstructor
@IdClass(ProfileFollowPK.class)
public class ProfileFollow {

    @Id
    @Column(name = "follower_id", nullable = false, updatable = false)
    private UUID followerId;

    @Id
    @Column(name = "followed_id", nullable = false, updatable = false)
    private UUID followedId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public ProfileFollow(UUID source, UUID target) {
        this.followerId = source;
        this.followedId = target;
    }

}
