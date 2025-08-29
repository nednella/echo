package com.example.echo_api.unit.service.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.profile.management.ProfileManagementService;
import com.example.echo_api.service.profile.management.ProfileManagementServiceImpl;
import com.example.echo_api.shared.service.SessionService;

/**
 * Unit test class for {@link ProfileManagementService}.
 */
@ExtendWith(MockitoExtension.class)
class ProfileManagementServiceTest {

    @InjectMocks
    private ProfileManagementServiceImpl profileManagementService;

    @Mock
    private SessionService sessionService;

    @Mock
    private ProfileRepository profileRepository;

    private static UUID authenticatedUserId;

    @BeforeAll
    static void setup() {
        authenticatedUserId = UUID.randomUUID();
    }

    @Test
    void updateProfile_ReturnsVoid_WhenSuccessfullyUpdated() {
        // arrange
        var request = new UpdateProfileDTO(
            "John Doe",
            "Bio",
            "Location");

        var profile = Profile.forTest(UUID.randomUUID(), "username");

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findById(authenticatedUserId)).thenReturn(Optional.of(profile));

        // act
        profileManagementService.updateProfile(request);

        // assert
        assertThat(profile.getName()).isEqualTo(request.name());
        assertThat(profile.getBio()).isEqualTo(request.bio());
        assertThat(profile.getLocation()).isEqualTo(request.location());
        verify(profileRepository).findById(authenticatedUserId);
        verify(profileRepository).save(profile);
    }

}
