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

import com.example.echo_api.persistence.dto.request.profile.UpdateInformationDTO;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.auth.session.SessionService;
import com.example.echo_api.service.file.FileService;
import com.example.echo_api.service.profile.management.ProfileManagementService;
import com.example.echo_api.service.profile.management.ProfileManagementServiceImpl;

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

    @Mock
    private FileService fileService;

    private static UUID authenticatedUserId;
    private static Profile authenticatedUserProfile;

    @BeforeAll
    static void setup() {
        authenticatedUserId = UUID.randomUUID();
        authenticatedUserProfile = Profile.forTest(authenticatedUserId, "test");
    }

    private void mockAuthenticatedUser() {
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findById(authenticatedUserId)).thenReturn(Optional.of(authenticatedUserProfile));
    }

    @Test
    void ProfileManagementService_UpdateInformation_ReturnVoid() {
        // arrange
        UpdateInformationDTO request = new UpdateInformationDTO(
            "John Doe",
            "Bio",
            "Location");

        mockAuthenticatedUser();

        // act
        profileManagementService.updateInformation(request);

        // assert
        assertEquals(request.name(), authenticatedUserProfile.getName());
        assertEquals(request.bio(), authenticatedUserProfile.getBio());
        assertEquals(request.location(), authenticatedUserProfile.getLocation());
        verify(profileRepository).findById(authenticatedUserId);
    }

}
