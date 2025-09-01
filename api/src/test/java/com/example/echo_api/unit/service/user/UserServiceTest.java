package com.example.echo_api.unit.service.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.persistence.repository.UserRepository;
import com.example.echo_api.service.user.UserServiceImpl;

/**
 * Unit test class for {@link UserService}.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void upsertFromExternalSource_PersistsNewUserAndProfile_WhenUserNotExistsByExternalId() {
        // arrange
        UUID id = UUID.randomUUID();
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        User newUser = User.forTest(id, externalId);
        Profile newProfile = Profile.forTest(id, username);

        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(profileRepository.save(any(Profile.class))).thenReturn(newProfile);

        // act
        User actual = userService.upsertFromExternalSource(externalId, username, imageUrl);

        // assert
        assertEquals(newUser, actual);
        verify(userRepository).findByExternalId(externalId);
        verify(userRepository).save(any(User.class)); // saves new user
        verify(profileRepository).save(any(Profile.class)); // saves new profile
    }

    @Test
    void upsertFromExternalSource_UpdatesProfile_WhenUserExistsByExternalId() {
        // arrange
        UUID id = UUID.randomUUID();
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        User expectedUser = User.forTest(id, externalId);
        Profile expectedProfile = Profile.forTest(id, username);

        when(userRepository.findByExternalId(externalId)).thenReturn(Optional.of(expectedUser));
        when(profileRepository.findById(id)).thenReturn(Optional.of(expectedProfile));

        // act
        User actual = userService.upsertFromExternalSource(externalId, username, imageUrl);

        // assert
        assertEquals(expectedUser, actual);
        verify(userRepository).findByExternalId(externalId);
        verify(userRepository, never()).save(any(User.class)); // never saves new user
        verify(profileRepository).findById(id);
        verify(profileRepository).save(expectedProfile); // updates profile

    }

    @Test
    void upsertFromExternalSource_ShouldThrowAndNotSaveAnything_WhenExternalIdIsNull() {
        // arrange
        String externalId = null;
        String username = "username";
        String imageUrl = "imageUrl";

        // act & assert
        Exception ex = assertThrows(IllegalArgumentException.class,
            () -> userService.upsertFromExternalSource(externalId, username, imageUrl));

        assertEquals("External ID cannot be null", ex.getMessage());
        verify(userRepository, never()).findByExternalId(externalId); // never performs a lookup
        verify(userRepository, never()).save(any(User.class)); // never saves new user
        verify(profileRepository, never()).save(any(Profile.class)); // never saves new profile
    }

    @Test
    void upsertFromExternalSource_ShouldThrowAndNotSaveAnything_WhenUsernameIsNull() {
        // arrange
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        String username = null;
        String imageUrl = "imageUrl";

        // act & assert
        Exception ex = assertThrows(IllegalArgumentException.class,
            () -> userService.upsertFromExternalSource(externalId, username, imageUrl));

        assertEquals("Username cannot be null", ex.getMessage());
        verify(userRepository, never()).findByExternalId(externalId); // never performs a lookup
        verify(userRepository, never()).save(any(User.class)); // never saves new user
        verify(profileRepository, never()).save(any(Profile.class)); // never saves new profile
    }

    @Test
    void deleteFromExternalSource_Returns1When1RecordAffected() {
        // arrange
        int expected = 1;
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        when(userRepository.deleteByExternalId(externalId)).thenReturn(expected);

        // act
        int actual = userService.deleteFromExternalSource(externalId);

        // assert
        assertEquals(expected, actual);
        verify(userRepository).deleteByExternalId(externalId);
    }

    @Test
    void deleteFromExternalSource_Returns0When0RecordsAffected() {
        // arrange
        int expected = 0;
        String externalId = "user_someRandomStringThatIsUniqueApparently";
        when(userRepository.deleteByExternalId(externalId)).thenReturn(expected);

        // act
        int actual = userService.deleteFromExternalSource(externalId);

        // assert
        assertEquals(expected, actual);
        verify(userRepository).deleteByExternalId(externalId);
    }

}
