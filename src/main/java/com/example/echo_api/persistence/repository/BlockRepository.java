package com.example.echo_api.persistence.repository;

import java.util.UUID;
import org.springframework.data.repository.ListCrudRepository;

import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.model.block.BlockPK;

public interface BlockRepository extends ListCrudRepository<Block, BlockPK> {

    boolean existsByBlockerIdAndBlockingId(UUID followerId, UUID followingId);

}
