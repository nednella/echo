package com.example.echo_api.persistence.model.user;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a user in the system.
 */
@Entity
@Table(name = "\"user\"")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(name = "clerk_id", unique = true, nullable = false, updatable = false)
    private String clerkId;

    @Column(unique = true, nullable = false)
    private String username;

    @Builder.Default
    @Column(nullable = false)
    private boolean enabled = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // ---- factory methods ----

    /**
     * Factory method to create a new {@link User} during onboarding using Clerk
     * user information.
     * 
     * @param clerkId  The unique identifier from Clerk
     * @param username The username from Clerk
     * @return New User instance
     * @throws NullPointerException if either parameter is null
     */
    public static User fromClerk(String clerkId, String username) {
        return User.builder()
            .clerkId(Objects.requireNonNull(clerkId))
            .username(Objects.requireNonNull(username))
            .build();
    }

    // ---- setters ----

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // ---- equals & hashcode ----

    /**
     * Compares {@link User} objects for equality.
     * 
     * <p>
     * Equality is determined based on the immutable {@code clerkId} field, which is
     * guaranteed to be unique and never change for a user's lifetime.
     * 
     * @param o The object to be compared
     * @return {@code true} if the clerkId of both users is equal, {@code false}
     *         otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User))
            return false;
        User that = (User) o;
        return Objects.equals(this.clerkId, that.clerkId);
    }

    /**
     * Generates a hashcode for {@link User} objects based on the immutable
     * {@code clerkId} field.
     * 
     * <p>
     * Two equal {@link User} objects will always generate an equal
     * {@code hashCode}, but two equal hashcodes do not guarantee that the
     * {@link User} objects are also equal.
     * 
     * @return hashcode value based on the clerkId
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.clerkId);
    }

}
