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

    @Column(name = "image_url")
    private String imageUrl;

    @Column(length = 50)
    private String name;

    @Column(length = 160)
    private String bio;

    @Column(length = 30)
    private String location;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // ---- factory methods ----

    public Profile(UUID id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * Factory method to create a new {@link Profile} during onboarding.
     * 
     * @param userId   The UUID shared with the User entity
     * @param username The username from Clerk
     * @param imageUrl Profile image URL from Clerk
     * @return New Profile instance
     * @throws NullPointerException If {@code id} or {@code username} is null
     */
    public static Profile forUser(UUID id, String username, String imageUrl) {
        return Profile.builder()
            .id(Objects.requireNonNull(id))
            .username(Objects.requireNonNull(username))
            .imageUrl(imageUrl)
            .build();
    }

    // ---- setters ----

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
