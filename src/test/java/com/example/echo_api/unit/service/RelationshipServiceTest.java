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

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.BlockedException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.service.relationship.RelationshipService;
import com.example.echo_api.service.relationship.RelationshipServiceImpl;
import com.example.echo_api.service.relationship.block.BlockService;
import com.example.echo_api.service.relationship.follow.FollowService;

/**
 * Unit test class for {@link RelationshipService}.
 */
@ExtendWith(MockitoExtension.class)
class RelationshipServiceTest {

    @Mock
    private FollowService followService;

    @Mock
    private BlockService blockService;

    @InjectMocks
    private RelationshipServiceImpl relationshipService;

    private static Profile source;
    private static Profile target;

    @BeforeAll
    static void setup() throws Exception {
        Account sourceAcc = new Account("source", "test");
        source = new Profile(sourceAcc);

        Account targetAcc = new Account("target", "test");
        target = new Profile(targetAcc);

        // set ids with reflection
        Field idField = Profile.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(source, UUID.randomUUID());
        idField.set(target, UUID.randomUUID());
    }

    /**
     * Test ensures that
     * {@link RelationshipServiceImpl#getRelationship(Profile, Profile)} correctly
     * returns {@link RelationshipDTO}.
     */
    @Test
    void RelationshipService_GetRelationship_ReturnRelationshipDto() {
        // arrange
        RelationshipDTO expected = new RelationshipDTO(false, false, false, false);
        when(followService.isFollowing(source, target)).thenReturn(false);
        when(followService.isFollowedBy(source, target)).thenReturn(false);
        when(blockService.isBlocking(source, target)).thenReturn(false);
        when(blockService.isBlockedBy(source, target)).thenReturn(false);

        // act
        RelationshipDTO actual = relationshipService.getRelationship(source, target);

        // assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    /**
     * Test ensures that
     * {@link RelationshipServiceImpl#getRelationship(Profile, Profile)} correctly
     * returns {@code null} when the supplied {@link Profile} are equal.
     */
    @Test
    void RelationshipService_GetRelationship_ReturnNull() {
        // act
        RelationshipDTO relationship = relationshipService.getRelationship(source, source);

        // assert
        assertNull(relationship);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#follow(Profile, Profile)}
     * does not throw any exceptions when called.
     */
    @Test
    void RelationshipService_Follow_ReturnVoid() {
        // arrange
        when(blockService.isBlockedBy(source, target)).thenReturn(false);
        doNothing().when(followService).follow(source, target);

        // act & assert
        assertDoesNotThrow(() -> relationshipService.follow(source, target));
        verify(blockService, times(1)).isBlockedBy(source, target);
        verify(followService, times(1)).follow(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#follow(Profile, Profile)}
     * throws {@link BlockedException} when the target profile is currently blocking
     * the source profile.
     */
    @Test
    void RelationshipService_Follow_ThrowBlockedException() {
        // arrange
        when(blockService.isBlockedBy(source, target)).thenReturn(true);

        // act & assert
        assertThrows(BlockedException.class, () -> relationshipService.follow(source, target));
        verify(blockService, times(1)).isBlockedBy(source, target);
        verify(followService, times(0)).follow(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#follow(Profile, Profile)}
     * throws {@link SelfActionException} when called with two identical profiles.
     */
    @Test
    void RelationshipService_Follow_ThrowSelfActionException() {
        // arrange
        when(blockService.isBlockedBy(source, target)).thenReturn(false);
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_FOLLOW)).when(followService).follow(source, target);

        // act & assert
        assertThrows(SelfActionException.class, () -> relationshipService.follow(source, target));
        verify(blockService, times(1)).isBlockedBy(source, target);
        verify(followService, times(1)).follow(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#follow(Profile, Profile)}
     * throws {@link AlreadyFollowingException} when the source profile is already
     * following the target profile.
     */
    @Test
    void RelationshipService_Follow_ThrowAlreadyFollowingException() {
        // arrange
        when(blockService.isBlockedBy(source, target)).thenReturn(false);
        doThrow(new AlreadyFollowingException()).when(followService).follow(source, target);

        // act & assert
        assertThrows(AlreadyFollowingException.class, () -> relationshipService.follow(source, target));
        verify(blockService, times(1)).isBlockedBy(source, target);
        verify(followService, times(1)).follow(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#unfollow(Profile, Profile)}
     * does not throw any exceptions when called.
     */
    @Test
    void RelationshipService_Unfollow_ReturnVoid() {
        // arrange
        doNothing().when(followService).unfollow(source, target);

        // act & assert
        assertDoesNotThrow(() -> relationshipService.unfollow(source, target));
        verify(followService, times(1)).unfollow(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#unfollow(Profile, Profile)}
     * throws {@link SelfActionException} when called with two identical profiles.
     */
    @Test
    void RelationshipService_Unfollow_ThrowSelfActionException() {
        // arrange
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_UNFOLLOW)).when(followService).unfollow(source, target);

        // act & assert
        assertThrows(SelfActionException.class, () -> relationshipService.unfollow(source, target));
        verify(followService, times(1)).unfollow(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#unfollow(Profile, Profile)}
     * throws {@link NotFollowingException} when the source profile is not already
     * following the target profile.
     */
    @Test
    void RelationshipService_Unfollow_ThrowNotFollowingException() {
        // arrange
        doThrow(new NotFollowingException()).when(followService).unfollow(source, target);

        // act & assert
        assertThrows(NotFollowingException.class, () -> relationshipService.unfollow(source, target));
        verify(followService, times(1)).unfollow(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#block(Profile, Profile)}
     * does not throw any exceptions when called.
     */
    @Test
    void RelationshipService_Block_ReturnVoid() {
        // arrange
        doNothing().when(blockService).block(source, target);

        // act & assert
        assertDoesNotThrow(() -> relationshipService.block(source, target));
        verify(blockService, times(1)).block(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#block(Profile, Profile)}
     * throws {@link SelfActionException} when called with two identical profiles.
     */
    @Test
    void RelationshipService_Block_ThrowSelfActionException() {
        // arrange
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_BLOCK)).when(blockService).block(source, target);

        // act & assert
        assertThrows(SelfActionException.class, () -> relationshipService.block(source, target));
        verify(blockService, times(1)).block(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#block(Profile, Profile)}
     * throws {@link AlreadyBlockingException} when the source profile is already
     * blocking the target profile.
     */
    @Test
    void RelationshipService_Block_ThrowAlreadyBlockingException() {
        // arrange
        doThrow(new AlreadyBlockingException()).when(blockService).block(source, target);

        // act & assert
        assertThrows(AlreadyBlockingException.class, () -> relationshipService.block(source, target));
        verify(blockService, times(1)).block(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#unblock(Profile, Profile)}
     * does not throw any exceptions when called.
     */
    @Test
    void RelationshipService_Unblock_ReturnVoid() {
        // arrange
        doNothing().when(blockService).unblock(source, target);

        // act & assert
        assertDoesNotThrow(() -> relationshipService.unblock(source, target));
        verify(blockService, times(1)).unblock(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#unblock(Profile, Profile)}
     * throws {@link SelfActionException} when called with two identical profiles.
     */
    @Test
    void RelationshipService_Unblock_ThrowSelfActionException() {
        // arrange
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_UNBLOCK)).when(blockService).unblock(source, target);

        // act & assert
        assertThrows(SelfActionException.class, () -> relationshipService.unblock(source, target));
        verify(blockService, times(1)).unblock(source, target);
    }

    /**
     * Test ensures that {@link RelationshipServiceImpl#unblock(Profile, Profile)}
     * throws {@link NotBlockingException} when the source profile is not already
     * blocking the target profile.
     */
    @Test
    void RelationshipService_Unblock_ThrowNotBlockingException() {
        // arrange
        doThrow(new NotBlockingException()).when(blockService).unblock(source, target);

        // act & assert
        assertThrows(NotBlockingException.class, () -> relationshipService.unblock(source, target));
        verify(blockService, times(1)).unblock(source, target);
    }

}
