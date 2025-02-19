package com.example.echo_api.persistence.model.image;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import com.example.echo_api.persistence.model.account.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "image_id")
    private UUID id;

    @Column(name = "public_id")
    private String publicId;

    private int height;

    private int width;

    private String url;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ---- constructors ----

    public Image(String publicId, int height, int width, String url) {
        this.publicId = publicId;
        this.height = height;
        this.width = width;
        this.url = url;
    }

    // ---- setters ----

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
