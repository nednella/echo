package com.example.echo_api.persistence.model.profile;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.util.Utils;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a {@link User} profile in the local application.
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

    /**
     * Factory method to create a new {@link Profile} for a given {@link User}.
     * 
     * @param id the UUID of the matching {@link User} entity
     * @return new {@link Profile} instance
     * @throws IllegalArgumentException if {@code id} is null
     */
    public static Profile forUser(UUID id) {
        return Profile.builder()
            .id(Utils.checkNotNull(id, "ID"))
            .build();
    }

    // ---- setters ----

    /**
     * Update the profile username
     * 
     * @param username the new username
     * @throws IllegalArgumentException if {@code username} is null
     */
    public void setUsername(String username) {
        this.username = Utils.checkNotNull(username, "Username");
    }

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
