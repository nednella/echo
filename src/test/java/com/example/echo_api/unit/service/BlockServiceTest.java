package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.BlockRepository;
import com.example.echo_api.service.block.BlockService;
import com.example.echo_api.service.block.BlockServiceImpl;

/**
 * Unit test class for {@link BlockService}.
 */
@ExtendWith(MockitoExtension.class)
class BlockServiceTest {

    @Mock
    private BlockRepository blockRepository;

    @InjectMocks
    private BlockServiceImpl blockService;

    private static Profile source;
    private static Profile target;

    @BeforeAll
    static void setup() {
        source = new Profile(UUID.randomUUID(), "source");
        target = new Profile(UUID.randomUUID(), "target");
    }

    /**
     * Test ensures that {@link BlockServiceImpl#block(UUID, UUID)} does not throw
     * any exceptions when called.
     */
    @Test
    void BlockService_Block_ReturnVoid() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(source.getId(), target.getId())).thenReturn(false);

        // act & assert
        assertDoesNotThrow(() -> blockService.block(source.getId(), target.getId()));
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
        verify(blockRepository, times(1)).save(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#block(UUID, UUID)} throws
     * {@link SelfActionException} when called with two identical profile ids.
     */
    @Test
    void BlockService_Block_ThrowSelfActionException() {
        // act & assert
        assertThrows(SelfActionException.class, () -> blockService.block(source.getId(), source.getId()));
        verify(blockRepository, times(0)).existsByBlockerIdAndBlockingId(source.getId(), source.getId());
        verify(blockRepository, times(0)).save(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#block(UUID, UUID)} throws
     * {@link AlreadyBlockingException} when called with a source profile id that
     * already blocks the target profile id.
     */
    @Test
    void BlockService_Block_ThrowAlreadyBlockingException() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(source.getId(), target.getId())).thenReturn(true);

        // act & assert
        assertThrows(AlreadyBlockingException.class, () -> blockService.block(source.getId(), target.getId()));
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
        verify(blockRepository, times(0)).save(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#unblock(UUID, UUID)} does not throw
     * any exceptions when called.
     */
    @Test
    void BlockService_Unblock_ReturnVoid() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(source.getId(), target.getId())).thenReturn(true);

        // act & assert
        assertDoesNotThrow(() -> blockService.unblock(source.getId(), target.getId()));
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
        verify(blockRepository, times(1)).delete(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#unblock(UUID, UUID)} throws
     * {@link SelfActionException} when called with two identical profile ids.
     */
    @Test
    void BlockService_Unblock_ThrowSelfActionException() {
        // act & assert
        assertThrows(SelfActionException.class, () -> blockService.unblock(source.getId(), source.getId()));
        verify(blockRepository, times(0)).existsByBlockerIdAndBlockingId(source.getId(), source.getId());
        verify(blockRepository, times(0)).delete(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#unblock(UUID, UUID)} throws
     * {@link NotBlockingException} when called with a source profile id that does
     * not block the target profile id.
     */
    @Test
    void BlockService_Unblock_ThrowNotBlockingException() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(source.getId(), target.getId())).thenReturn(false);

        // act & assert
        assertThrows(NotBlockingException.class, () -> blockService.unblock(source.getId(), target.getId()));
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
        verify(blockRepository, times(0)).delete(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#existsAnyBlockBetween(UUID, UUID)}
     * returns true when called with two profile ids that share an established block
     * relationship (can be unidrectional or bidirectional).
     */
    @Test
    void BlockService_ExistsAnyBlockBetween_ReturnTrue() {
        // arrange
        when(blockRepository.existsAnyBlockBetween(source.getId(), target.getId())).thenReturn(true);

        // act
        boolean exists = blockService.existsAnyBlockBetween(source.getId(), target.getId());

        // assert
        assertTrue(exists);
        verify(blockRepository, times(1)).existsAnyBlockBetween(source.getId(), target.getId());
    }

    /**
     * Test ensures that {@link BlockServiceImpl#existsAnyBlockBetween(UUID, UUID)}
     * returns true when called with two profile ids that do not share an
     * established block relationship (unidrectional or bidirectional).
     */
    @Test
    void BlockService_ExistsAnyBlockBetween_ReturnFalse() {
        // arrange
        when(blockRepository.existsAnyBlockBetween(source.getId(), target.getId())).thenReturn(false);

        // act
        boolean exists = blockService.existsAnyBlockBetween(source.getId(), target.getId());

        // assert
        assertFalse(exists);
        verify(blockRepository, times(1)).existsAnyBlockBetween(source.getId(), target.getId());
    }

}
