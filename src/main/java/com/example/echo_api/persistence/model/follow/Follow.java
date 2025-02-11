package com.example.echo_api.persistence.model.follow;

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
    @Column(name = "follower_id")
    private UUID followerId;

    @Id
    @Column(name = "following_id")
    private UUID followingId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Follow(Profile follower, Profile following) {
        this.followerId = follower.getProfileId();
        this.followingId = following.getProfileId();
    }

}
