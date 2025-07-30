package com.example.echo_api.persistence.model.user;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a user in the system.
 */
@Entity
@Table(name = "\"user\"")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "clerkId", unique = true, nullable = false)
    private String clerkId;

    private String username;

    private boolean enabled;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // ---- production constructors ----

    public User(String clerkId, String username) {
        this.clerkId = clerkId;
        this.username = username;
    }

    // ---- test env constructors ----

    public User(UUID id, String clerkId, String username) {
        this.id = id;
        this.clerkId = clerkId;
        this.username = username;
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
     * Equality is determined based on the unique {@code username} field, as the
     * unique {@code id} field is only generated once the user is persisted.
     * 
     * @param o The object to be compared.
     * @return {@code true} if the username of both users is equal, else
     *         {@code false}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (this.getClass() != o.getClass())
            return false;

        User that = (User) o;
        return Objects.equals(this.username, that.username);
    }

    /**
     * Generates a hashcode for {@link User} objects based on the unique username
     * field.
     * 
     * <p>
     * Two equal {@link AccoUserunt} objects will always generate an equal
     * {@code hashCode}, but two equal hashcodes do not guarantee that the
     * {@link User} objects are also equal.
     * 
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }

}
