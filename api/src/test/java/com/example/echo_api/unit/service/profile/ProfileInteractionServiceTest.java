package com.example.echo_api.unit.service.profile;

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

    private static UUID authenticatedUserId;
    private static Profile authenticatedUserProfile;
    private static Profile target;

    @BeforeAll
    static void setup() {
        authenticatedUserId = UUID.randomUUID();
        authenticatedUserProfile = Profile.forTest(authenticatedUserId, "test");
        target = Profile.forTest(UUID.randomUUID(), "target");
    }

    @Test
    void follow_ReturnsVoid_WhenFollowSuccessfullyCreated() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findById(id)).thenReturn(Optional.of(target));

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.follow(id));
        verify(followRepository).save(any(Follow.class));
    }

    @Test
    void follow_ThrowsResourceNotFound_WhenProfileByIdDoesNotExist() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.follow(id));
        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    void follow_ThrowsSelfAction_WhenProfileByIdIsYou() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findById(id)).thenReturn(Optional.of(authenticatedUserProfile));

        // act & assert
        assertThrows(SelfActionException.class, () -> profileInteractionService.follow(id));
        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    void follow_ThrowsAlreadyFollowing_WhenProfileByIdAlreadyFollowedByYou() {
        // arrange
        UUID id = target.getId();

        when(profileRepository.findById(id)).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(followRepository.existsByFollowerIdAndFollowedId(authenticatedUserId, id)).thenReturn(true);

        // act & assert
        assertThrows(AlreadyFollowingException.class, () -> profileInteractionService.follow(id));
        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test // TODO: remove tests after unfollow refactored to idempotent operation
    void unfollow_ReturnVoid() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findById(id)).thenReturn(Optional.of(target));

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.unfollow(id));
        verify(followRepository).deleteByFollowerIdAndFollowedId(authenticatedUserId, id);
    }

    @Test
    void unfollow_ThrowResourceNotFound() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileInteractionService.unfollow(id));
        verify(followRepository, never()).deleteByFollowerIdAndFollowedId(authenticatedUserId, id);
    }

    @Test
    void unfollow_ThrowSelfActionException() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findById(id)).thenReturn(Optional.of(authenticatedUserProfile));

        // act & assert
        assertThrows(SelfActionException.class, () -> profileInteractionService.unfollow(id));
        verify(followRepository, never()).deleteByFollowerIdAndFollowedId(authenticatedUserId, id);
    }

}
