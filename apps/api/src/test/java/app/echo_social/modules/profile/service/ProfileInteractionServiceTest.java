package app.echo_social.modules.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
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

import app.echo_social.exception.ApplicationException;
import app.echo_social.modules.profile.entity.ProfileFollow;
import app.echo_social.modules.profile.exception.ProfileErrorCode;
import app.echo_social.modules.profile.repository.ProfileFollowRepository;
import app.echo_social.modules.profile.repository.ProfileRepository;
import app.echo_social.shared.service.SessionService;

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
    private ProfileFollowRepository profileFollowRepository;

    private static UUID authenticatedUserId;

    @BeforeAll
    static void setup() {
        authenticatedUserId = UUID.randomUUID();
    }

    @Test
    void follow_ReturnsVoid_WhenFollowSuccessfullyCreated() {
        // arrange
        UUID id = UUID.randomUUID();

        when(profileRepository.existsById(id)).thenReturn(true);
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileFollowRepository.existsByFollowerIdAndFollowedId(authenticatedUserId, id)).thenReturn(false);

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.follow(id));
        verify(profileFollowRepository).save(any(ProfileFollow.class));
    }

    @Test
    void follow_ThrowsApplicationException_WhenProfileByIdDoesNotExist() {
        // arrange
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID id = UUID.randomUUID();

        when(profileRepository.existsById(id)).thenReturn(false);

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> profileInteractionService.follow(id));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage(id));

        verify(profileFollowRepository, never()).save(any(ProfileFollow.class));
    }

    @Test
    void follow_ThrowsApplicationException_WhenProfileByIdIsYou() {
        // arrange
        ProfileErrorCode errorCode = ProfileErrorCode.SELF_ACTION;

        when(profileRepository.existsById(authenticatedUserId)).thenReturn(true);
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> profileInteractionService.follow(authenticatedUserId));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage());

        verify(profileFollowRepository, never()).save(any(ProfileFollow.class));
    }

    @Test
    void follow_ThrowsApplicationException_WhenProfileByIdAlreadyFollowedByYou() {
        // arrange
        ProfileErrorCode errorCode = ProfileErrorCode.ALREADY_FOLLOWING;
        UUID id = UUID.randomUUID();

        when(profileRepository.existsById(id)).thenReturn(true);
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileFollowRepository.existsByFollowerIdAndFollowedId(authenticatedUserId, id)).thenReturn(true);

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> profileInteractionService.follow(id));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage(id));

        verify(profileFollowRepository, never()).save(any(ProfileFollow.class));
    }

    @Test
    void unfollow_ReturnsVoid() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);

        // act & assert
        assertDoesNotThrow(() -> profileInteractionService.unfollow(id));
    }

}
