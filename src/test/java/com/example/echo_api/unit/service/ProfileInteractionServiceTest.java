package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.exception.custom.conflict.AlreadyBlockingException;
import com.example.echo_api.exception.custom.conflict.AlreadyFollowingException;
import com.example.echo_api.exception.custom.conflict.SelfActionException;
import com.example.echo_api.exception.custom.forbidden.BlockedException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.block.Block;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.BlockRepository;
import com.example.echo_api.persistence.repository.FollowRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.profile.interaction.ProfileInteractionService;
import com.example.echo_api.service.profile.interaction.ProfileInteractionServiceImpl;
import com.example.echo_api.service.session.SessionService;

/**
 * Unit test class for {@link ProfileInteractionService}.
 */
@ExtendWith(MockitoExtension.class)
class ProfileInteractionServiceTest {

    @InjectMocks
    private ProfileInteractionServiceImpl profileInteractionService;

    @Mock
    private SessionService sessionService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private BlockRepository blockRepository;

    private static Account authenticatedUser;
    private static Profile authenticatedUserProfile;
    private static Profile target;

    @BeforeAll
    static void setup() {
        authenticatedUser = new Account("user", "password");
        authenticatedUserProfile = new Profile(null, "source");
        target = new Profile(UUID.randomUUID(), "target");
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(String)} does not
     * throw any exceptions when called with a valid username.
     */
    @Test
    void ProfileInteractionService_Follow_ReturnVoid() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.of(target));
        when(blockRepository.existsAnyBlockBetween(authenticatedUser.getId(), id)).thenReturn(false);

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.follow(id));
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(String)} throws
     * {@link ResourceNotFoundException} when called with an invalid username.
     */
    @Test
    void ProfileInteractionService_Follow_ThrowResourceNotFound() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.follow(id));
        verify(followRepository, times(0)).save(any(Follow.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(String)} throws
     * {@link SelfActionException} when the supplied username is the username of the
     * authenticated user.
     */
    @Test
    void ProfileInteractionService_Follow_ThrowSelfActionException() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.of(authenticatedUserProfile));

        // act & assert
        assertThrows(SelfActionException.class, () -> profileInteractionService.follow(id));
        verify(followRepository, times(0)).save(any(Follow.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(String)} throws
     * {@link BlockedException} when there is a unidrectional or bidirectional block
     * relationship established between users in question.
     */
    @Test
    void ProfileInteractionService_Follow_ThrowBlockedException() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.of(target));
        when(blockRepository.existsAnyBlockBetween(authenticatedUser.getId(), id)).thenReturn(true);

        // act & assert
        assertThrows(BlockedException.class, () -> profileInteractionService.follow(id));
        verify(followRepository, times(0)).save(any(Follow.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(String)} throws
     * {@link AlreadyFollowingException} when the supplied username is already
     * followed by the authenticated user.
     */
    @Test
    void ProfileInteractionService_Follow_ThrowAlreadyFollowingException() {
        // arrange
        UUID id = target.getId();

        when(profileRepository.findById(id)).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(blockRepository.existsAnyBlockBetween(authenticatedUser.getId(), id)).thenReturn(false);
        when(followRepository.existsByFollowerIdAndFollowedId(authenticatedUser.getId(), id))
            .thenReturn(true);

        // act & assert
        assertThrows(AlreadyFollowingException.class, () -> profileInteractionService.follow(id));
        verify(followRepository, times(0)).save(any(Follow.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unfollow(String)} does not
     * throw any exceptions when called with a valid username.
     */
    @Test
    void ProfileInteractionService_Unfollow_ReturnVoid() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.of(target));

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.unfollow(id));
        verify(followRepository, times(1)).deleteByFollowerIdAndFollowedId(authenticatedUser.getId(), id);
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unfollow(String)} throws
     * {@link ResourceNotFoundException} when called with an invalid username.
     */
    @Test
    void ProfileInteractionService_Unfollow_ThrowResourceNotFound() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.unfollow(id));
        verify(followRepository, times(0)).deleteByFollowerIdAndFollowedId(authenticatedUser.getId(), id);
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unfollow(String)} throws
     * {@link SelfActionException} when the supplied username is the username of the
     * authenticated user.
     */
    @Test
    void ProfileInteractionService_Unfollow_ThrowSelfActionException() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.of(authenticatedUserProfile));

        // act & assert
        assertThrows(SelfActionException.class, () -> profileInteractionService.unfollow(id));
        verify(followRepository, times(0)).deleteByFollowerIdAndFollowedId(authenticatedUser.getId(), id);
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#block(String)} does not
     * throw any exceptions when called with a valid username.
     */
    @Test
    void ProfileInteractionService_Block_ReturnVoid() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.of(target));
        when(blockRepository.existsByBlockerIdAndBlockedId(authenticatedUser.getId(), id))
            .thenReturn(false);

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.block(id));
        verify(blockRepository, times(1)).save(any(Block.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#block(String)} throws
     * {@link ResourceNotFoundException} when called with an invalid username.
     */
    @Test
    void ProfileInteractionService_Block_ThrowResourceNotFound() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.block(id));
        verify(blockRepository, times(0)).save(any(Block.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#block(String)} throws
     * {@link SelfActionException} when the supplied username is the username of the
     * authenticated user.
     */
    @Test
    void ProfileInteractionService_Block_ThrowSelfActionException() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.of(authenticatedUserProfile));

        // act & assert
        assertThrows(SelfActionException.class, () -> profileInteractionService.block(id));
        verify(blockRepository, times(0)).save(any(Block.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unblock(String)} throws
     * {@link AlreadyBlockingException} when the supplied username is already
     * blocked by the authenticated user.
     */
    @Test
    void ProfileInteractionService_Block_ThrowAlreadyBlockingException() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.of(target));
        when(blockRepository.existsByBlockerIdAndBlockedId(authenticatedUser.getId(), id)).thenReturn(true);

        // act & assert
        assertThrows(AlreadyBlockingException.class, () -> profileInteractionService.block(id));
        verify(blockRepository, times(0)).save(any(Block.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unblock(String)} does not
     * throw any exceptions when called with a valid username.
     */
    @Test
    void ProfileInteractionService_Unblock_ReturnVoid() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.of(target));

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.unblock(id));
        verify(blockRepository, times(1)).deleteByBlockerIdAndBlockedId(authenticatedUser.getId(), id);
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unblock(String)} throws
     * {@link ResourceNotFoundException} when called with an invalid username.
     */
    @Test
    void ProfileInteractionService_Unblock_ThrowResourceNotFound() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.unblock(id));
        verify(blockRepository, times(0)).deleteByBlockerIdAndBlockedId(authenticatedUser.getId(), id);
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unblock(String)} throws
     * {@link SelfActionException} when the supplied username is the username of the
     * authenticated user.
     */
    @Test
    void ProfileInteractionService_Unblock_ThrowSelfActionException() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.of(authenticatedUserProfile));

        // act & assert
        assertThrows(SelfActionException.class, () -> profileInteractionService.unblock(id));
        verify(blockRepository, times(0)).deleteByBlockerIdAndBlockedId(authenticatedUser.getId(), id);
    }

}
