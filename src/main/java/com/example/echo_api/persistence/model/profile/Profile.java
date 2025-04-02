package com.example.echo_api.persistence.model.profile;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.image.Image;
import com.example.echo_api.validation.account.annotations.Username;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing an {@link Account} profile in the system.
 */
@Entity
@Table
@Getter
@NoArgsConstructor
public class Profile {

    @Id
    @Column(unique = true, nullable = false)
    private UUID id;

    @Username
    @Column(unique = true, nullable = false)
    private String username;

    @Column(length = 50)
    private String name;

    @Column(length = 160)
    private String bio;

    @Column(length = 30)
    private String location;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    private Image avatar;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banner_id")
    private Image banner;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // ---- constructors ----

    public Profile(Account account) {
        this.id = account.getId();
        this.username = account.getUsername();
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

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public void setBanner(Image banner) {
        this.banner = banner;
    }

}
