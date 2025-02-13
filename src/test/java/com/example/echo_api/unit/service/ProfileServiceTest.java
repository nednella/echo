package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.BlockedException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.exception.custom.username.UsernameNotFoundException;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.mapper.ProfileMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.metrics.profile.ProfileMetricsService;
import com.example.echo_api.service.profile.ProfileService;
import com.example.echo_api.service.profile.ProfileServiceImpl;
import com.example.echo_api.service.relationship.RelationshipService;
import com.example.echo_api.service.session.SessionService;

/**
 * Unit test class for {@link ProfileService}.
 */
@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private ProfileMetricsService profileMetricsService;

    @Mock
    private RelationshipService relationshipService;

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private static Account authenticatedUser;
    private static Profile me;

    @BeforeAll
    static void setup() throws Exception {
        authenticatedUser = new Account("me", "test");
        me = new Profile(authenticatedUser);

        // set id with reflection
        Field idField = Profile.class.getDeclaredField("profileId");
        idField.setAccessible(true);
        idField.set(me, UUID.randomUUID());
    }

    /**
     * Test ensures that the {@link ProfileServiceImpl#getByUsername(String)} method
     * correctly returns the profile when the username exists.
     */
    @Test
    void ProfileService_GetByUsername_ReturnProfileDTO() {
        // arrange
        Account account = new Account("test", "test");
        Profile profile = new Profile(account);
        MetricsDTO metrics = new MetricsDTO(0, 0, 0, 0);
        RelationshipDTO relationship = new RelationshipDTO(false, false, false, false);
        ProfileDTO expected = ProfileMapper.toDTO(profile, metrics, relationship);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(profile));
        when(profileMetricsService.getMetrics(profile)).thenReturn(metrics);
        when(relationshipService.getRelationship(me, profile)).thenReturn(relationship);

        // act
        ProfileDTO actual = profileService.getByUsername(profile.getUsername());

        // assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(profile.getUsername());
    }

    /**
     * Test ensures that the {@link ProfileServiceImpl#getByUsername(String)} method
     * correctly throws a {@link UsernameNotFoundException} when no such profile
     * with the supplied username exists.
     */
    @Test
    void ProfileService_GetByUsername_ThrowUsernameNotFound() {
        // arrange
        String username = "non-existent-user";
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.getByUsername(username));
        verify(profileRepository, times(1)).findByUsername(username);
    }

    /**
     * Test ensures that the {@link ProfileServiceImpl#getMe()} method correctly
     * returns the authenticated account's profile.
     */
    @Test
    void ProfileService_GetMe_ReturnProfileResponse() {
        // arrange
        MetricsDTO metrics = new MetricsDTO(0, 0, 0, 0);
        ProfileDTO expected = ProfileMapper.toDTO(me, metrics, null);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileMetricsService.getMetrics(me)).thenReturn(metrics);

        // act
        ProfileDTO actual = profileService.getMe();

        // assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
    }

    /**
     * Test ensures that the {@link ProfileServiceImpl#getMe()} method correctly
     * throws a {@link UsernameNotFoundException} when no such profile with the
     * authenticated account's username exists.
     */
    @Test
    void ProfileService_GetMe_ThrowUsernameNotFound() {
        // arrange
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.getMe());
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
    }

    /**
     * Test ensures that the
     * {@link ProfileServiceImpl#updateMeProfileInfo(UpdateProfileDTO)} method
     * correctly updates the authenticated account's profile information.
     */
    @Test
    void ProfileService_UpdateMeProfileInfo_ReturnVoid() {
        // arrange
        UpdateProfileDTO request = new UpdateProfileDTO(
            "John Doe",
            "Bio",
            "Location");

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));

        // act
        profileService.updateMeProfile(request);

        // assert
        assertEquals(request.name(), me.getName());
        assertEquals(request.bio(), me.getBio());
        assertEquals(request.location(), me.getLocation());
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
    }

    /**
     * Test ensures that the
     * {@link ProfileServiceImpl#updateMeProfileInfo(UpdateProfileDTO)} method
     * correctly throws a {@link UsernameNotFoundException} when no such profile
     * with the authenticated account's username exists.
     */
    @Test
    void ProfileService_UpdateMeProfileInfo_ThrowUsernameNotFound() {
        // arrange
        UpdateProfileDTO request = new UpdateProfileDTO(
            "name",
            "bio",
            "location");

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.updateMeProfile(request));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowers(String)} correctly
     * returns an empty {@link List} of {@link ProfileDTO} when the {@link Profile}
     * associated to the {@code username} has no followers.
     */
    @Test
    void ProfileService_GetFollowers_ReturnListOfEmpty() {
        // arrange
        Account account = new Account("test", "test");
        Profile profile = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(profile));
        when(profileRepository.findAllFollowersById(profile.getProfileId())).thenReturn(new ArrayList<>());

        // act
        List<ProfileDTO> list = profileService.getFollowers(profile.getUsername());

        // assert
        assertTrue(list.isEmpty());
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(profile.getUsername());
        verify(profileRepository, times(1)).findAllFollowersById(profile.getProfileId());
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowers(String)} correctly
     * returns a non-empty {@link List} of {@link ProfileDTO} when the
     * {@link Profile} associated to the {@code username} has at least 1 follower.
     */
    @Test
    void ProfileService_GetFollowers_ReturnListOfProfileDto() {
        // arrange
        Account account = new Account("test", "test");
        Profile profile = new Profile(account);
        MetricsDTO metrics = new MetricsDTO(0, 0, 0, 0);
        RelationshipDTO relationship = new RelationshipDTO(false, false, false, false);
        ProfileDTO expected = ProfileMapper.toDTO(profile, metrics, relationship);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(profile));
        when(profileRepository.findAllFollowersById(profile.getProfileId())).thenReturn(List.of(profile));
        when(profileMetricsService.getMetrics(profile)).thenReturn(metrics);
        when(relationshipService.getRelationship(me, profile)).thenReturn(relationship);

        // act
        List<ProfileDTO> list = profileService.getFollowers(profile.getUsername());

        // assert
        assertFalse(list.isEmpty());
        assertEquals(expected, list.get(0));
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(profile.getUsername());
        verify(profileRepository, times(1)).findAllFollowersById(profile.getProfileId());
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowers(String)} throws
     * {@link UsernameNotFoundException} when no such profile with the authenticated
     * account's username exists.
     */
    @Test
    void ProfileService_GetFollowers_ThrowsUsernameNotFound() {
        // arrange
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername("non-existent-username")).thenThrow(new UsernameNotFoundException());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.getFollowers("non-existent-username"));
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername("non-existent-username");
        verify(profileRepository, never()).findAllFollowersById(any(UUID.class));
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowing(String)} correctly
     * returns an empty {@link List} of {@link ProfileDTO} when the {@link Profile}
     * associated to the {@code username} has no followers.
     */
    @Test
    void ProfileService_GetFollowing_ReturnListOfEmpty() {
        // arrange
        Account account = new Account("test", "test");
        Profile profile = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(profile));
        when(profileRepository.findAllFollowingById(profile.getProfileId())).thenReturn(new ArrayList<>());

        // act
        List<ProfileDTO> list = profileService.getFollowing(profile.getUsername());

        // assert
        assertTrue(list.isEmpty());
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(profile.getUsername());
        verify(profileRepository, times(1)).findAllFollowingById(profile.getProfileId());
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowing(String)} correctly
     * returns a non-empty {@link List} of {@link ProfileDTO} when the
     * {@link Profile} associated to the {@code username} has at least 1 follower.
     */
    @Test
    void ProfileService_GetFollowing_ReturnListOfProfileDto() {
        // arrange
        Account account = new Account("test", "test");
        Profile profile = new Profile(account);
        MetricsDTO metrics = new MetricsDTO(0, 0, 0, 0);
        RelationshipDTO relationship = new RelationshipDTO(false, false, false, false);
        ProfileDTO expected = ProfileMapper.toDTO(profile, metrics, relationship);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(profile));
        when(profileRepository.findAllFollowingById(profile.getProfileId())).thenReturn(List.of(profile));
        when(profileMetricsService.getMetrics(profile)).thenReturn(metrics);
        when(relationshipService.getRelationship(me, profile)).thenReturn(relationship);

        // act
        List<ProfileDTO> list = profileService.getFollowing(profile.getUsername());

        // assert
        assertFalse(list.isEmpty());
        assertEquals(expected, list.get(0));
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(profile.getUsername());
        verify(profileRepository, times(1)).findAllFollowingById(profile.getProfileId());
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowing(String)} throws
     * {@link UsernameNotFoundException} when no such profile with the authenticated
     * account's username exists.
     */
    @Test
    void ProfileService_GetFollowing_ThrowsUsernameNotFound() {
        // arrange
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername("non-existent-username")).thenThrow(new UsernameNotFoundException());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.getFollowing("non-existent-username"));
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername("non-existent-username");
        verify(profileRepository, never()).findAllFollowersById(any(UUID.class));
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#follow(String)} does not throw
     * any exceptions when called.
     */
    @Test
    void ProfileService_Follow_ReturnVoid() {
        // arrange
        Account account = new Account("test", "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(account.getUsername())).thenReturn(Optional.of(target));
        doNothing().when(relationshipService).follow(me, target);

        // act & assert
        assertDoesNotThrow(() -> profileService.follow(account.getUsername()));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(account.getUsername());
        verify(relationshipService, times(1)).follow(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#follow(String)} throws
     * {@link UsernameNotFoundException} when the supplied username does not exist.
     */
    @Test
    void ProfileService_Follow_Throw400UsernameNotFoundException() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.follow(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, never()).follow(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#follow(String)} throws
     * {@link UsernameNotFoundException} when the supplied username is equal to the
     * current authenticated user.
     */
    @Test
    void ProfileService_Follow_ThrowSelfActionException() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_FOLLOW)).when(relationshipService).follow(me, target);

        // act & assert
        assertThrows(SelfActionException.class, () -> profileService.follow(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(account.getUsername());
        verify(relationshipService, times(1)).follow(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#follow(String)} throws
     * {@link UsernameNotFoundException} when the supplied username is already
     * followed by the authenticated user.
     */
    @Test
    void ProfileService_Follow_ThrowAlreadyFollowingException() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doThrow(new AlreadyFollowingException()).when(relationshipService).follow(me, target);

        // act & assert
        assertThrows(AlreadyFollowingException.class, () -> profileService.follow(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(account.getUsername());
        verify(relationshipService, times(1)).follow(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#follow(String)} throws
     * {@link UsernameNotFoundException} when the supplied username is blocking the
     * authenticated user.
     */
    @Test
    void ProfileService_Follow_ThrowBlockedException() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doThrow(new BlockedException()).when(relationshipService).follow(me, target);

        // act & assert
        assertThrows(BlockedException.class, () -> profileService.follow(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, times(1)).follow(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unfollow(String)} does not throw
     * any exceptions when called.
     */
    @Test
    void ProfileService_Unfollow_ReturnVoid() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doNothing().when(relationshipService).unfollow(me, target);

        // act & assert
        assertDoesNotThrow(() -> profileService.unfollow(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, times(1)).unfollow(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unfollow(String)} throws
     * {@link UsernameNotFoundException} when the supplied username does not exist.
     */
    @Test
    void ProfileService_Unfollow_Throw400UsernameNotFound() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.unfollow(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, never()).unfollow(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unfollow(String)} throws
     * {@link UsernameNotFoundException} when the supplied username is equal to the
     * current authenticated user.
     */
    @Test
    void ProfileService_Unfollow_ThrowSelfActionException() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_UNFOLLOW)).when(relationshipService).unfollow(me,
            target);

        // act & assert
        assertThrows(SelfActionException.class, () -> profileService.unfollow(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, times(1)).unfollow(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unfollow(String)} throws
     * {@link UsernameNotFoundException} when the supplied username is already
     * followed by the authenticated user.
     */
    @Test
    void ProfileService_Unfollow_ThrowNotFollowingException() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doThrow(new NotFollowingException()).when(relationshipService).unfollow(me, target);

        // act & assert
        assertThrows(NotFollowingException.class, () -> profileService.unfollow(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, times(1)).unfollow(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#block(String)} does not throw any
     * exceptions when called.
     */
    @Test
    void ProfileService_Block_ReturnVoid() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doNothing().when(relationshipService).block(me, target);

        // act & assert
        assertDoesNotThrow(() -> profileService.block(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, times(1)).block(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#block(String)} throws
     * {@link UsernameNotFoundException} when the supplied username does not exist.
     */
    @Test
    void ProfileService_Block_Throw400UsernameNotFound() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.block(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, never()).block(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#block(String)} throws
     * {@link UsernameNotFoundException} when the supplied username is equal to the
     * current authenticated user.
     */
    @Test
    void ProfileService_Block_ThrowSelfActionException() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_BLOCK)).when(relationshipService).block(me, target);

        // act & assert
        assertThrows(SelfActionException.class, () -> profileService.block(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, times(1)).block(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#block(String)} throws
     * {@link UsernameNotFoundException} when the supplied username is already
     * followed by the authenticated user.
     */
    @Test
    void ProfileService_Block_ThrowAlreadyBlockingException() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doThrow(new AlreadyBlockingException()).when(relationshipService).block(me, target);

        // act & assert
        assertThrows(AlreadyBlockingException.class, () -> profileService.block(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, times(1)).block(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unblock(String)} does not throw
     * any exceptions when called.
     */
    @Test
    void ProfileService_Unblock_ReturnVoid() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doNothing().when(relationshipService).unblock(me, target);

        // act & assert
        assertDoesNotThrow(() -> profileService.unblock(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, times(1)).unblock(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unblock(String)} throws
     * {@link UsernameNotFoundException} when the supplied username does not exist.
     */
    @Test
    void ProfileService_Unblock_Throw400UsernameNotFound() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.unblock(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, never()).unblock(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unblock(String)} throws
     * {@link UsernameNotFoundException} when the supplied username is equal to the
     * current authenticated user.
     */
    @Test
    void ProfileService_Unblock_ThrowSelfActionException() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_BLOCK)).when(relationshipService).unblock(me, target);

        // act & assert
        assertThrows(SelfActionException.class, () -> profileService.unblock(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, times(1)).unblock(me, target);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unblock(String)} throws
     * {@link UsernameNotFoundException} when the supplied username is already
     * followed by the authenticated user.
     */
    @Test
    void ProfileService_Unblock_ThrowNotBlockingException() {
        // arrange
        String username = "valid-username";
        Account account = new Account(username, "test");
        Profile target = new Profile(account);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findByUsername(authenticatedUser.getUsername())).thenReturn(Optional.of(me));
        when(profileRepository.findByUsername(username)).thenReturn(Optional.of(target));
        doThrow(new NotBlockingException()).when(relationshipService).block(me, target);

        // act & assert
        assertThrows(NotBlockingException.class, () -> profileService.block(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findByUsername(authenticatedUser.getUsername());
        verify(profileRepository, times(1)).findByUsername(username);
        verify(relationshipService, times(1)).block(me, target);
    }

}
