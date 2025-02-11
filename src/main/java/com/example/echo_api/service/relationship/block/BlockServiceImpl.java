package com.example.echo_api.service.relationship.block;

import java.util.Objects;

import org.springframework.stereotype.Service;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.model.profile.Profile;
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
    public void block(Profile source, Profile target) throws SelfActionException, AlreadyBlockingException {
        if (isSelfAction(source, target)) {
            throw new SelfActionException(ErrorMessageConfig.SELF_BLOCK);
        }
        if (isBlocking(source, target)) {
            throw new AlreadyBlockingException();
        }

        blockRepository.save(new Block(source, target));
    }

    @Override
    public void unblock(Profile source, Profile target) throws SelfActionException, NotBlockingException {
        if (isSelfAction(source, target)) {
            throw new SelfActionException(ErrorMessageConfig.SELF_UNBLOCK);
        }
        if (!isBlocking(source, target)) {
            throw new NotBlockingException();
        }

        blockRepository.delete(new Block(source, target));
    }

    @Override
    public boolean isBlocking(Profile source, Profile target) {
        return blockRepository.existsByBlockerIdAndBlockingId(source.getProfileId(), target.getProfileId());
    }

    @Override
    public boolean isBlockedBy(Profile source, Profile target) {
        return isBlocking(target, source);
    }

    /**
     * Internal method for checking if {@link Profile} pairs match.
     * 
     * @param source The source {@link Profile}.
     * @param target The target {@link Profile}.
     * @return Boolean indicating whether the profiles are a match.
     */
    private boolean isSelfAction(Profile source, Profile target) {
        return Objects.equals(source.getProfileId(), target.getProfileId());
    }

}
