package com.example.echo_api.persistence.model.profile;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.echo_api.persistence.model.user.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a {@link User} profile in the system.
 */
@Entity
@Table
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Id
    @Column(updatable = false)
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

    // ---- factory methods ----

    /**
     * Factory method to create a new {@link Profile} during onboarding.
     * 
     * @param id       The UUID of the matching {@link User} entity
     * @param username The username from Clerk
     * @param imageUrl The optional profile image URL from Clerk
     * @return New Profile instance
     * @throws NullPointerException If {@code id} or {@code username} is null
     */
    public static Profile fromClerk(UUID id, String username, String imageUrl) {
        return Profile.builder()
            .id(Objects.requireNonNull(id))
            .username(Objects.requireNonNull(username))
            .avatarImageUrl(imageUrl)
            .build();
    }

    /**
     * Factory method to create a new {@link Profile} for <b>unit/integration
     * testing only</b>.
     * 
     * @param id       The placeholder UUID
     * @param username The placeholder unique Clerk username
     * @return New Profile instance
     * @throws NullPointerException if any parameter is null
     */
    public static Profile forTest(UUID id, String username) {
        return Profile.builder()
            .id(Objects.requireNonNull(id))
            .username(Objects.requireNonNull(username))
            .build();
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

    public void setAvatarImageUrl(String imageUrl) {
        this.avatarImageUrl = imageUrl;
    }

    public void setBannerImageUrl(String imageUrl) {
        this.bannerImageUrl = imageUrl;
    }

}
