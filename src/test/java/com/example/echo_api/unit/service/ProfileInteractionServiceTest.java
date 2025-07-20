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

import com.example.echo_api.exception.custom.conflict.AlreadyFollowingException;
import com.example.echo_api.exception.custom.conflict.SelfActionException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;
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
     * Test ensures {@link ProfileInteractionServiceImpl#follow(UUID)} does not
     * throw any exceptions when called with a valid {@code id}.
     */
    @Test
    void ProfileInteractionService_Follow_ReturnVoid() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.of(target));

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.follow(id));
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(UUID)} throws
     * {@link ResourceNotFoundException} when called with an invalid {@code id}.
     */
    @Test
    void ProfileInteractionService_Follow_ThrowResourceNotFound() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.follow(id));
        verify(followRepository, times(0)).save(any(Follow.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#follow(UUID)} throws
     * {@link SelfActionException} when the supplied {@code id} is the {@code id} of
     * the authenticated user.
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
     * Test ensures {@link ProfileInteractionServiceImpl#follow(UUID)} throws
     * {@link AlreadyFollowingException} when the supplied {@code id} is already
     * followed by the authenticated user.
     */
    @Test
    void ProfileInteractionService_Follow_ThrowAlreadyFollowingException() {
        // arrange
        UUID id = target.getId();

        when(profileRepository.findById(id)).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(followRepository.existsByFollowerIdAndFollowedId(authenticatedUser.getId(), id)).thenReturn(true);

        // act & assert
        assertThrows(AlreadyFollowingException.class, () -> profileInteractionService.follow(id));
        verify(followRepository, times(0)).save(any(Follow.class));
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unfollow(UUID)} does not
     * throw any exceptions when called with a valid {@code id}.
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
     * Test ensures {@link ProfileInteractionServiceImpl#unfollow(UUID)} throws
     * {@link ResourceNotFoundException} when called with an invalid {@code id}.
     */
    @Test
    void ProfileInteractionService_Unfollow_ThrowResourceNotFound() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.unfollow(id));
        verify(followRepository, times(0)).deleteByFollowerIdAndFollowedId(authenticatedUser.getId(), id);
    }

    /**
     * Test ensures {@link ProfileInteractionServiceImpl#unfollow(UUID)} throws
     * {@link SelfActionException} when the supplied {@code id} is the {@code id} of
     * the authenticated user.
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

}
