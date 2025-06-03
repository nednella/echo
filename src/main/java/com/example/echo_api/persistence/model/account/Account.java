package com.example.echo_api.persistence.model.account;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.echo_api.validation.account.annotations.Username;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing an account in the system.
 * 
 * <p>
 * This class is mapped to the {@code account} table in the database and stores
 * essential account information required for authentication and account
 * management in the system.
 *
 * <p>
 * Fields {@code id}, {@code createdAt}, and {@code updatedAt} are
 * auto-generated and managed by Hibernate.
 * 
 */
@Entity
@Table
@Getter
@NoArgsConstructor
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Username
    private String username;

    @Column(name = "encrypted_password")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // default = Role.USER

    private boolean enabled = true; // default = true

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // ---- production constructors ----

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ---- test constructors ----

    public Account(UUID id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // ---- setters ----

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    // ---- equals & hashcode ----

    /**
     * Compares {@link Account} objects for equality.
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

        Account that = (Account) o;
        return Objects.equals(this.username, that.username);
    }

    /**
     * Generates a hashcode for {@link Account} objects based on the unique username
     * field.
     * 
     * <p>
     * Two equal {@link Account} objects will always generate an equal
     * {@code hashCode}, but two equal hashcodes do not guarantee that the
     * {@link Account} objects are also equal.
     * 
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.username);
    }

}
