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
import com.example.echo_api.exception.custom.conflict.NotBlockingException;
import com.example.echo_api.exception.custom.conflict.NotFollowingException;
import com.example.echo_api.exception.custom.conflict.SelfActionException;
import com.example.echo_api.exception.custom.forbidden.BlockedException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.block.BlockService;
import com.example.echo_api.service.follow.FollowService;
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
    private FollowService followService;

    @Mock
    private BlockService blockService;

    private static Account authenticatedUser;
    private static Profile target;

    @BeforeAll
    static void setup() {
        authenticatedUser = new Account("user", "password");
        target = new Profile(UUID.randomUUID(), "target");
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(String)} does not
     * throw any exceptions when called with a valid username.
     */
    @Test
    void ProfileInteractionService_Follow_ReturnVoid() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(blockService.existsAnyBlockBetween(authenticatedUser.getId(), target.getId())).thenReturn(false);
        doNothing().when(followService).follow(authenticatedUser.getId(), target.getId());

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.follow(target.getUsername()));
        verify(followService, times(1)).follow(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(String)} throws
     * {@link ResourceNotFoundException} when called with an invalid username.
     */
    @Test
    void ProfileInteractionService_Follow_ThrowResourceNotFound() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.follow(target.getUsername()));
        verify(followService, times(0)).follow(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(String)} throws
     * {@link BlockedException} when there is a unidrectional or bidirectional block
     * relationship established between users in question.
     */
    @Test
    void ProfileInteractionService_Follow_ThrowBlockedException() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(blockService.existsAnyBlockBetween(authenticatedUser.getId(), target.getId())).thenReturn(true);

        // act & assert
        assertThrows(BlockedException.class, () -> profileInteractionService.follow(target.getUsername()));
        verify(followService, times(0)).follow(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(String)} throws
     * {@link SelfActionException} when the supplied username is the username of the
     * authenticated user.
     */
    @Test
    void ProfileInteractionService_Follow_ThrowSelfActionException() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(blockService.existsAnyBlockBetween(authenticatedUser.getId(), target.getId())).thenReturn(false);
        doThrow(SelfActionException.class).when(followService).follow(authenticatedUser.getId(), target.getId());

        // act & assert
        assertThrows(SelfActionException.class, () -> profileInteractionService.follow(target.getUsername()));
        verify(followService, times(1)).follow(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(String)} throws
     * {@link AlreadyFollowingException} when the supplied username is already
     * followed by the authenticated user.
     */
    @Test
    void ProfileInteractionService_Follow_ThrowAlreadyFollowingException() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(blockService.existsAnyBlockBetween(authenticatedUser.getId(), target.getId())).thenReturn(false);
        doThrow(AlreadyFollowingException.class).when(followService).follow(authenticatedUser.getId(), target.getId());

        // act & assert
        assertThrows(AlreadyFollowingException.class, () -> profileInteractionService.follow(target.getUsername()));
        verify(followService, times(1)).follow(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unfollow(String)} does not
     * throw any exceptions when called with a valid username.
     */
    @Test
    void ProfileInteractionService_Unfollow_ReturnVoid() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        doNothing().when(followService).unfollow(authenticatedUser.getId(), target.getId());

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.unfollow(target.getUsername()));
        verify(followService, times(1)).unfollow(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unfollow(String)} throws
     * {@link ResourceNotFoundException} when called with an invalid username.
     */
    @Test
    void ProfileInteractionService_Unfollow_ThrowResourceNotFound() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.unfollow(target.getUsername()));
        verify(followService, times(0)).unfollow(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unfollow(String)} throws
     * {@link SelfActionException} when the supplied username is the username of the
     * authenticated user.
     */
    @Test
    void ProfileInteractionService_Unfollow_ThrowSelfActionException() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        doThrow(SelfActionException.class).when(followService).unfollow(authenticatedUser.getId(), target.getId());

        // act & assert
        assertThrows(SelfActionException.class, () -> profileInteractionService.unfollow(target.getUsername()));
        verify(followService, times(1)).unfollow(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unfollow(String)} throws
     * {@link NotFollowingException} when the supplied username is not followed by
     * the authenticated user.
     */
    @Test
    void ProfileInteractionService_Unfollow_ThrowNotFollowingException() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        doThrow(NotFollowingException.class).when(followService).unfollow(authenticatedUser.getId(), target.getId());

        // act & assert
        assertThrows(NotFollowingException.class, () -> profileInteractionService.unfollow(target.getUsername()));
        verify(followService, times(1)).unfollow(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#block(String)} does not
     * throw any exceptions when called with a valid username.
     */
    @Test
    void ProfileInteractionService_Block_ReturnVoid() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        doNothing().when(blockService).block(authenticatedUser.getId(), target.getId());

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.block(target.getUsername()));
        verify(blockService, times(1)).block(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#block(String)} throws
     * {@link ResourceNotFoundException} when called with an invalid username.
     */
    @Test
    void ProfileInteractionService_Block_ThrowResourceNotFound() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.block(target.getUsername()));
        verify(blockService, times(0)).block(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#block(String)} throws
     * {@link SelfActionException} when the supplied username is the username of the
     * authenticated user.
     */
    @Test
    void ProfileInteractionService_Block_ThrowSelfActionException() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        doThrow(SelfActionException.class).when(blockService).block(authenticatedUser.getId(), target.getId());

        // act & assert
        assertThrows(SelfActionException.class, () -> profileInteractionService.block(target.getUsername()));
        verify(blockService, times(1)).block(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unblock(String)} throws
     * {@link AlreadyBlockingException} when the supplied username is already
     * blocked by the authenticated user.
     */
    @Test
    void ProfileInteractionService_Block_ThrowAlreadyBlockingException() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        doThrow(AlreadyBlockingException.class).when(blockService).block(authenticatedUser.getId(), target.getId());

        // act & assert
        assertThrows(AlreadyBlockingException.class, () -> profileInteractionService.block(target.getUsername()));
        verify(blockService, times(1)).block(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unblock(String)} does not
     * throw any exceptions when called with a valid username.
     */
    @Test
    void ProfileInteractionService_Unblock_ReturnVoid() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        doNothing().when(blockService).unblock(authenticatedUser.getId(), target.getId());

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.unblock(target.getUsername()));
        verify(blockService, times(1)).unblock(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unblock(String)} throws
     * {@link ResourceNotFoundException} when called with an invalid username.
     */
    @Test
    void ProfileInteractionService_Unblock_ThrowResourceNotFound() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.unblock(target.getUsername()));
        verify(blockService, times(0)).unblock(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unblock(String)} throws
     * {@link SelfActionException} when the supplied username is the username of the
     * authenticated user.
     */
    @Test
    void ProfileInteractionService_Unblock_ThrowSelfActionException() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        doThrow(SelfActionException.class).when(blockService).unblock(authenticatedUser.getId(), target.getId());

        // act & assert
        assertThrows(SelfActionException.class, () -> profileInteractionService.unblock(target.getUsername()));
        verify(blockService, times(1)).unblock(authenticatedUser.getId(), target.getId());
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unblock(String)} throws
     * {@link NotBlockingException} when the supplied username is not blocked by the
     * authenticated user.
     */
    @Test
    void ProfileInteractionService_Unblock_ThrowNotBlockingException() {
        // arrange
        when(profileRepository.findByUsername(target.getUsername())).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        doThrow(NotBlockingException.class).when(blockService).unblock(authenticatedUser.getId(), target.getId());

        // act & assert
        assertThrows(NotBlockingException.class, () -> profileInteractionService.unblock(target.getUsername()));
        verify(blockService, times(1)).unblock(authenticatedUser.getId(), target.getId());
    }

}
