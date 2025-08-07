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

import com.example.echo_api.exception.custom.internalserver.CloudinaryException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.request.profile.UpdateInformationDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.image.Image;
import com.example.echo_api.persistence.model.image.ImageType;
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

    private static Account authenticatedUser;
    private static Profile authenticatedUserProfile;

    @BeforeAll
    static void setup() {
        authenticatedUser = new Account("user", "password");
        authenticatedUserProfile = new Profile(UUID.randomUUID(), authenticatedUser.getUsername());
    }

    private void mockAuthenticatedUser() {
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findById(authenticatedUser.getId())).thenReturn(Optional.of(authenticatedUserProfile));
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
        verify(profileRepository, times(1)).findById(authenticatedUser.getId());
    }

    @Test
    void ProfileManagementService_UpdateAvatar_ReturnVoid() {
        // arrange
        Image expected = new Image(null, null, null, 0, 0, null, null);

        mockAuthenticatedUser();
        when(fileService.createImage(null, ImageType.AVATAR)).thenReturn(expected);

        // act & assert
        assertDoesNotThrow(() -> profileManagementService.updateAvatar(null));
    }

    @Test
    void ProfileManagementService_UpdateAvatar_ThrowResourceNotFound() {
        // arrange
        mockAuthenticatedUser();
        when(fileService.createImage(null, ImageType.AVATAR)).thenThrow(new ResourceNotFoundException());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileManagementService.updateAvatar(null));

    }

    @Test
    void ProfileManagementService_UpdateAvatar_ThrowCloudinaryException() {
        // arrange
        mockAuthenticatedUser();
        when(fileService.createImage(null, ImageType.AVATAR)).thenThrow(new CloudinaryException(null));

        // act & assert
        assertThrows(CloudinaryException.class, () -> profileManagementService.updateAvatar(null));
    }

    @Test
    void ProfileManagementService_DeleteAvatar_ReturnVoid() {
        // arrange
        Image avatar = new Image(null, null, null, 0, 0, null, null);

        mockAuthenticatedUser();
        authenticatedUserProfile.setAvatar(avatar);
        doNothing().when(fileService).deleteImage(authenticatedUserProfile.getAvatar().getId());

        // act & assert
        assertDoesNotThrow(() -> profileManagementService.deleteAvatar());
    }

    @Test
    void ProfileManagementService_DeleteAvatar_ThrowResourceNotFound() {
        // arrange
        Image avatar = new Image(null, null, null, 0, 0, null, null);

        mockAuthenticatedUser();
        authenticatedUserProfile.setAvatar(avatar);
        doThrow(new ResourceNotFoundException())
            .when(fileService)
            .deleteImage(authenticatedUserProfile.getAvatar().getId());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileManagementService.deleteAvatar());
    }

    @Test
    void ProfileManagementService_DeleteAvatar_ThrowCloudinaryException() {
        // arrange
        Image avatar = new Image(null, null, null, 0, 0, null, null);

        mockAuthenticatedUser();
        authenticatedUserProfile.setAvatar(avatar);
        doThrow(new CloudinaryException(null))
            .when(fileService)
            .deleteImage(authenticatedUserProfile.getAvatar().getId());

        // act & assert
        assertThrows(CloudinaryException.class, () -> profileManagementService.deleteAvatar());
    }

    @Test
    void ProfileManagementService_UpdateBanner_ReturnVoid() {
        // arrange
        Image expected = new Image(null, null, null, 0, 0, null, null);

        mockAuthenticatedUser();
        when(fileService.createImage(null, ImageType.BANNER)).thenReturn(expected);

        // act & assert
        assertDoesNotThrow(() -> profileManagementService.updateBanner(null));
    }

    @Test
    void ProfileManagementService_UpdateBanner_ThrowResourceNotFound() {
        // arrange
        mockAuthenticatedUser();
        when(fileService.createImage(null, ImageType.BANNER)).thenThrow(new ResourceNotFoundException());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileManagementService.updateBanner(null));

    }

    @Test
    void ProfileManagementService_UpdateBanner_ThrowCloudinaryException() {
        // arrange
        mockAuthenticatedUser();
        when(fileService.createImage(null, ImageType.BANNER)).thenThrow(new CloudinaryException(null));

        // act & assert
        assertThrows(CloudinaryException.class, () -> profileManagementService.updateBanner(null));
    }

    @Test
    void ProfileManagementService_DeleteBanner_ReturnVoid() {
        // arrange
        Image banner = new Image(null, null, null, 0, 0, null, null);

        mockAuthenticatedUser();
        authenticatedUserProfile.setBanner(banner);
        doNothing().when(fileService).deleteImage(authenticatedUserProfile.getBanner().getId());

        // act & assert
        assertDoesNotThrow(() -> profileManagementService.deleteBanner());
    }

    @Test
    void ProfileManagementService_DeleteBanner_ThrowResourceNotFound() {
        // arrange
        Image banner = new Image(null, null, null, 0, 0, null, null);

        mockAuthenticatedUser();
        authenticatedUserProfile.setBanner(banner);
        doThrow(new ResourceNotFoundException())
            .when(fileService)
            .deleteImage(authenticatedUserProfile.getBanner().getId());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileManagementService.deleteBanner());
    }

    @Test
    void ProfileManagementService_DeleteBanner_ThrowCloudinaryException() {
        // arrange
        Image banner = new Image(null, null, null, 0, 0, null, null);

        mockAuthenticatedUser();
        authenticatedUserProfile.setBanner(banner);
        doThrow(new CloudinaryException(""))
            .when(fileService)
            .deleteImage(authenticatedUserProfile.getBanner().getId());

        // act & assert
        assertThrows(CloudinaryException.class, () -> profileManagementService.deleteBanner());
    }

}
