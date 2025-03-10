package com.example.echo_api.persistence.model.image;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;

import com.example.echo_api.persistence.model.account.Account;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type")
    private ImageType type;

    @Column(name = "public_id")
    private String publicId;

    @Column(name = "asset_id")
    private String assetId;

    @Column(name = "original_width")
    private int originalWidth;

    @Column(name = "original_height")
    private int originalHeight;

    @Column(name = "original_url")
    private String originalUrl;

    @Column(name = "transformed_url")
    private String transformedUrl;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ---- constructors ----

    public Image(
        ImageType type,
        String publicId,
        String assetId,
        int originalWidth,
        int originalHeight,
        String originalUrl,
        String transformedUrl) {
        this.type = type;
        this.publicId = publicId;
        this.assetId = assetId;
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
        this.originalUrl = originalUrl;
        this.transformedUrl = transformedUrl;
    }

}
