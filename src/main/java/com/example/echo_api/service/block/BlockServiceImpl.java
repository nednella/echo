package com.example.echo_api.service.block;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.repository.BlockRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing and validating CRD operations of
 * {@link Block} relationships.
 * 
 * @see Block
 * @see BlockRepository
 */
@Service
@RequiredArgsConstructor
public class BlockServiceImpl implements BlockService {

    private final BlockRepository blockRepository;

    @Override
    public void block(UUID source, UUID target) throws AlreadyBlockingException {
        if (isBlocking(source, target)) {
            throw new AlreadyBlockingException();
        }

        blockRepository.save(new Block(source, target));
    }

    @Override
    public void unblock(UUID source, UUID target) throws NotBlockingException {
        if (!isBlocking(source, target)) {
            throw new NotBlockingException();
        }

        blockRepository.delete(new Block(source, target));
    }

    @Override
    public boolean existsAnyBlockBetween(UUID profileId1, UUID profileId2) {
        return blockRepository.existsAnyBlockBetween(profileId1, profileId2);
    }

    /**
     * Internal method to check if a one-way block exists from the blocker to the
     * blocked profile.
     * 
     * @param source The id of the profile initiating the block.
     * @param target The id of the profile being blocked.
     * @return True if a unidirectional block exists from source to target, else
     *         false.
     */
    private boolean isBlocking(UUID source, UUID target) {
        return blockRepository.existsByBlockerIdAndBlockingId(source, target);
    }

}
