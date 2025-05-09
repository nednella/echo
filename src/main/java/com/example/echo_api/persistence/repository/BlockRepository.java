package com.example.echo_api.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.model.block.BlockPK;

public interface BlockRepository extends ListCrudRepository<Block, BlockPK> {

    /**
     * Checks if a one-way block exists from the blocker to the blocked profile.
     * 
     * @param blockerId  The id of the profile initiating the block.
     * @param blockingId The id of the profile being blocked.
     * @return True if a unidirectional block exists from blocker to blocking, else
     *         false.
     */
    boolean existsByBlockerIdAndBlockingId(UUID blockerId, UUID blockingId);

    /**
     * Check if any blocking relationship exists between two profiles in either
     * direction (unidirectional or bidrectional).
     * 
     * @param profileId1 The id of the first profile.
     * @param profileId2 The id of the second profile.
     * @return True if any block exists between the two profiles.
     */
    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END
        FROM Block b
        WHERE (b.blockerId = :profileId1 AND b.blockingId = :profileId2)
           OR (b.blockerId = :profileId2 AND b.blockingId = :profileId1)
        """)
    boolean existsAnyBlockBetween(@Param("profileId1") UUID profileId1, @Param("profileId2") UUID profileId2);

}
