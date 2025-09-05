package com.example.echo_api.modules.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.profile.entity.Follow;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.exception.ProfileErrorCode;
import com.example.echo_api.modules.profile.repository.FollowRepository;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.shared.service.SessionService;

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
    void follow_ThrowsApplicationException_WhenProfileByIdDoesNotExist() {
        // arrange
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> profileInteractionService.follow(id));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage(id));

        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    void follow_ThrowsApplicationException_WhenProfileByIdIsYou() {
        // arrange
        ProfileErrorCode errorCode = ProfileErrorCode.SELF_ACTION;
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findById(id)).thenReturn(Optional.of(authenticatedUserProfile));

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> profileInteractionService.follow(id));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage());

        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    void follow_ThrowsApplicationException_WhenProfileByIdAlreadyFollowedByYou() {
        // arrange
        ProfileErrorCode errorCode = ProfileErrorCode.ALREADY_FOLLOWING;
        UUID id = target.getId();

        when(profileRepository.findById(id)).thenReturn(Optional.of(target));
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(followRepository.existsByFollowerIdAndFollowedId(authenticatedUserId, id)).thenReturn(true);

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> profileInteractionService.follow(id));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage(id));

        verify(followRepository, never()).save(any(Follow.class));
    }

    @Test
    void unfollow_ReturnsVoid() {
        // arrange
        UUID id = target.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.unfollow(id));
    }

}
