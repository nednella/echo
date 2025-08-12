package com.example.echo_api.unit.service.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.exception.custom.badrequest.ClerkIdAlreadyExistsException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.request.clerk.webhook.data.UserDeleted;
import com.example.echo_api.persistence.dto.request.clerk.webhook.data.UserUpdated;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.persistence.repository.UserRepository;
import com.example.echo_api.service.user.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createUserWithProfile_CreatesUserAndProfile() {
        // arrange
        String clerkId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        User user = User.forTest(UUID.randomUUID(), clerkId, username);
        Profile profile = Profile.forTest(user.getId(), user.getUsername());

        when(userRepository.existsByClerkId(clerkId)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        // act & assert
        assertDoesNotThrow(() -> userService.createUserWithProfile(clerkId, username, imageUrl));
        verify(userRepository).existsByClerkId(clerkId);
        verify(userRepository).save(user);
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    void createUserWithProfile_ThrowsWhenClerkIdAlreadyExists() {
        // arrange
        String clerkId = "user_someRandomStringThatIsUniqueApparently";
        when(userRepository.existsByClerkId(clerkId)).thenReturn(true);

        // act & assert
        assertThrows(ClerkIdAlreadyExistsException.class, () -> userService.createUserWithProfile(clerkId, null, null));
    }

    @Test
    void handleClerkUserDeleted_UpdatesUserAndProfile() {
        // arrange
        String clerkId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        UUID userId = UUID.randomUUID();
        User user = User.forTest(userId, clerkId, username);
        Profile profile = Profile.forTest(userId, username);
        UserUpdated data = new UserUpdated(clerkId, username, imageUrl);

        when(userRepository.findByClerkId(clerkId)).thenReturn(Optional.of(user));
        when(profileRepository.findById(user.getId())).thenReturn(Optional.of(profile));
        when(userRepository.save(user)).thenReturn(user);
        when(profileRepository.save(profile)).thenReturn(profile);

        // act & assert
        assertDoesNotThrow(() -> userService.handleClerkUserUpdated(data));
        verify(userRepository).findByClerkId(clerkId);
        verify(profileRepository).findById(user.getId());
        verify(userRepository).save(user);
        verify(profileRepository).save(profile);
    }

    @Test
    void handleClerkUserUpdated_ThowsWhenUserNotExistsByClerkId() {
        // arrange
        String clerkId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        UserUpdated data = new UserUpdated(clerkId, username, imageUrl);

        when(userRepository.findByClerkId(clerkId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> userService.handleClerkUserUpdated(data));
        verify(userRepository).findByClerkId(clerkId);
        verify(profileRepository, never()).findById(any(UUID.class));
        verify(userRepository, never()).save(any(User.class));
        verify(profileRepository, never()).save(any(Profile.class));

    }

    @Test
    void handleClerkUserUpdated_ThrowsWhenProfileNotExistsByUUID() {
        // arrange
        String clerkId = "user_someRandomStringThatIsUniqueApparently";
        String username = "username";
        String imageUrl = "imageUrl";
        User user = User.forTest(UUID.randomUUID(), clerkId, username);
        UserUpdated data = new UserUpdated(clerkId, username, imageUrl);

        when(userRepository.findByClerkId(clerkId)).thenReturn(Optional.of(user));
        when(profileRepository.findById(user.getId())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> userService.handleClerkUserUpdated(data));
        verify(userRepository).findByClerkId(clerkId);
        verify(profileRepository).findById(user.getId());
        verify(userRepository, never()).save(any(User.class));
        verify(profileRepository, never()).save(any(Profile.class));
    }

    @Test
    void handleClerkUserDeleted_Deletes1RecordWhenExistsByClerkId() {
        // arrange
        String clerkId = "user_someRandomStringThatIsUniqueApparently";
        boolean deleted = true;
        UserDeleted data = new UserDeleted(clerkId, deleted);
        when(userRepository.deleteByClerkId(clerkId)).thenReturn(1);

        // act
        int actual = userService.handleClerkUserDeleted(data);

        // assert
        assertEquals(1, actual);
        verify(userRepository).deleteByClerkId(clerkId);
    }

    @Test
    void handleClerkUserDeleted_Deletes0RecordsWhenDoesNotExistByClerkId() {
        // arrange
        String clerkId = "user_someRandomStringThatIsUniqueApparently";
        boolean deleted = true;
        UserDeleted data = new UserDeleted(clerkId, deleted);
        when(userRepository.deleteByClerkId(clerkId)).thenReturn(0);

        // act
        int actual = userService.handleClerkUserDeleted(data);

        // assert
        assertEquals(0, actual);
        verify(userRepository).deleteByClerkId(clerkId);
    }

}
