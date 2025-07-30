package com.example.echo_api.persistence.model.profile;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.echo_api.persistence.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a {@link User} profile in the system.
 */
@Entity
@Table
@Getter
@NoArgsConstructor
public class Profile {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(length = 50)
    private String name;

    @Column(length = 160)
    private String bio;

    @Column(length = 30)
    private String location;

    @Column(name = "avatar_image_url")
    private String avatarImageUrl;

    @Column(name = "banner_image_url")
    private String bannerImageUrl;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // ---- constructors ----

    public Profile(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    // ---- setters ----

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setAvatarUrl(String imageUrl) {
        this.avatarImageUrl = imageUrl;
    }

    public void setBannerUrl(String imageUrl) {
        this.bannerImageUrl = imageUrl;
    }

}
