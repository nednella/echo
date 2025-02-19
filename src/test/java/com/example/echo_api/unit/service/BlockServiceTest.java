package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
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
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.BlockRepository;
import com.example.echo_api.service.relationship.block.BlockService;
import com.example.echo_api.service.relationship.block.BlockServiceImpl;

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
    static void setup() throws Exception {
        Account sourceAcc = new Account("source", "test");
        source = new Profile(sourceAcc);

        Account targetAcc = new Account("target", "test");
        target = new Profile(targetAcc);

        // set ids with reflection
        Field idField = Profile.class.getDeclaredField("profileId");
        idField.setAccessible(true);
        idField.set(source, UUID.randomUUID());
        idField.set(target, UUID.randomUUID());
    }

    /**
     * Test ensures that {@link BlockServiceImpl#block(Profile, Profile)} does not
     * throw any exceptions when called.
     */
    @Test
    void BlockService_Block_ReturnVoid() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(source.getId(), target.getId())).thenReturn(false);

        // act & assert
        assertDoesNotThrow(() -> blockService.block(source, target));
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
        verify(blockRepository, times(1)).save(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#block(Profile, Profile)} throws
     * {@link SelfActionException} when called with two identical profiles.
     */
    @Test
    void BlockService_Block_ThrowSelfActionException() {
        // act & assert
        assertThrows(SelfActionException.class, () -> blockService.block(source, source));
        verify(blockRepository, times(0)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
        verify(blockRepository, times(0)).save(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#block(Profile, Profile)} throws
     * {@link AlreadyBlockingException} when called with a source profile that
     * already blocks the target profile.
     */
    @Test
    void BlockService_Block_ThrowAlreadyBlockingException() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(source.getId(), target.getId())).thenReturn(true);

        // act & assert
        assertThrows(AlreadyBlockingException.class, () -> blockService.block(source, target));
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
        verify(blockRepository, times(0)).save(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#unblock(Profile, Profile)} does not
     * throw any exceptions when called.
     */
    @Test
    void BlockService_Unblock_ReturnVoid() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(source.getId(), target.getId())).thenReturn(true);

        // act & assert
        assertDoesNotThrow(() -> blockService.unblock(source, target));
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
        verify(blockRepository, times(1)).delete(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#unblock(Profile, Profile)} throws
     * {@link SelfActionException} when called with two identical profiles.
     */
    @Test
    void BlockService_Unblock_ThrowSelfActionException() {
        // act & assert
        assertThrows(SelfActionException.class, () -> blockService.unblock(source, source));
        verify(blockRepository, times(0)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
        verify(blockRepository, times(0)).delete(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#unblock(Profile, Profile)} throws
     * {@link NotBlockingException} when called with a source profile that does not
     * block the target profile.
     */
    @Test
    void BlockService_Unblock_ThrowNotBlockingException() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(source.getId(), target.getId())).thenReturn(false);

        // act & assert
        assertThrows(NotBlockingException.class, () -> blockService.unblock(source, target));
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
        verify(blockRepository, times(0)).delete(any(Block.class));
    }

    /**
     * Test ensures that {@link BlockServiceImpl#isBlocking(Profile, Profile)}
     * returns true when the source profile is already blocking the target profile.
     */
    @Test
    void BlockService_IsBlocking_ReturnTrue() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(source.getId(), target.getId())).thenReturn(true);

        // act
        boolean isBlocking = blockService.isBlocking(source, target);

        // assert
        assertTrue(isBlocking);
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
    }

    /**
     * Test ensures that {@link BlockServiceImpl#isBlocking(Profile, Profile)}
     * returns false when the source profile is not blocking the target profile.
     */
    @Test
    void BlockService_IsBlocking_ReturnFalse() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(source.getId(), target.getId())).thenReturn(false);

        // act
        boolean isBlocking = blockService.isBlocking(source, target);

        // assert
        assertFalse(isBlocking);
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(source.getId(), target.getId());
    }

    /**
     * Test ensures that {@link BlockServiceImpl#isBlockedBy(Profile, Profile)}
     * returns true when the source profile is already blocked by the target
     * profile.
     */
    @Test
    void BlockService_IsBlockedBy_ReturnTrue() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(target.getId(), source.getId())).thenReturn(true);

        // act
        boolean isBlockedBy = blockService.isBlockedBy(source, target);

        // assert
        assertTrue(isBlockedBy);
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(target.getId(), source.getId());
    }

    /**
     * Test ensures that {@link BlockServiceImpl#isBlockedBy(Profile, Profile)}
     * returns false when the source profile is not blocked by the target profile.
     */
    @Test
    void BlockService_IsBlockedBy_ReturnFalse() {
        // arrange
        when(blockRepository.existsByBlockerIdAndBlockingId(target.getId(), source.getId())).thenReturn(false);

        // act
        boolean isBlockedBy = blockService.isBlockedBy(source, target);

        // assert
        assertFalse(isBlockedBy);
        verify(blockRepository, times(1)).existsByBlockerIdAndBlockingId(target.getId(), source.getId());
    }

}
