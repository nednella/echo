package com.example.echo_api.persistence.model.block;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import com.example.echo_api.persistence.model.profile.Profile;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Entity class representing a {@link Profile} blocking relationship in the
 * application.
 */
@Entity
@Table
@Getter
@NoArgsConstructor
@IdClass(BlockPK.class)
public class Block {

    @Id
    @Column(name = "blocker_id")
    private UUID blockerId;

    @Id
    @Column(name = "blocked_id")
    private UUID blockedId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Block(UUID source, UUID target) {
        this.blockerId = source;
        this.blockedId = target;
    }

}
