package com.example.echo_api.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.model.block.BlockPK;

@Repository
public interface BlockRepository extends ListCrudRepository<Block, BlockPK> {

    /**
     * Checks if a unidirectional block exists from the blocker to the blocked
     * profile id.
     * 
     * @param blockerId The id of the profile initiating the block.
     * @param blockedId The id of the profile being blocked.
     * @return True if a unidirectional block exists from blocker to blocked, else
     *         false.
     */
    boolean existsByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);

    /**
     * Check if any blocks exist between supplied profile id in either direction
     * (unidirectional or bidrectional).
     * 
     * @param profileId1 The id of the first profile.
     * @param profileId2 The id of the second profile.
     * @return True if any block exists between the two profiles.
     */
    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END
        FROM Block b
        WHERE (b.blockerId = :profileId1 AND b.blockedId = :profileId2)
           OR (b.blockerId = :profileId2 AND b.blockedId = :profileId1)
        """)
    boolean existsAnyBlockBetween(@Param("profileId1") UUID profileId1, @Param("profileId2") UUID profileId2);

    /**
     * Delete the block if one exists from the blocker to blocked profile id.
     * <p>
     * This action is idempotent.
     * 
     * @param blockerId The id of the profile initiating the block.
     * @param blockedId The id of the profile being blocked.
     */
    int deleteByBlockerIdAndBlockedId(UUID blockerId, UUID blockedId);

}
