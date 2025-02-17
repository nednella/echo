package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.exception.custom.relationship.AlreadyBlockingException;
import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.BlockedException;
import com.example.echo_api.exception.custom.relationship.NotBlockingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.exception.custom.username.UsernameNotFoundException;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.persistence.mapper.ProfileMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.metrics.profile.ProfileMetricsService;
import com.example.echo_api.service.profile.ProfileService;
import com.example.echo_api.service.profile.ProfileServiceImpl;
import com.example.echo_api.service.relationship.RelationshipService;
import com.example.echo_api.service.session.SessionService;
import com.example.echo_api.util.pagination.OffsetLimitRequest;

import jakarta.servlet.http.HttpServletRequest;

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

    @Mock
    private HttpServletRequest httpRequest;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private final Account authenticatedAccount = new Account("authenticatedUser", "password");
    private final Profile authenticatedProfile = new Profile(authenticatedAccount);

    // ---- helper methods ----

    private Profile createTestProfile(String username, String password) {
        Account account = new Account(username, password);
        return new Profile(account);
    }

    private MetricsDTO createMetricsDto() {
        return new MetricsDTO(0, 0, 0, 0);
    }

    private RelationshipDTO createRelationshipDto() {
        return new RelationshipDTO(false, false, false, false);
    }

    private ProfileDTO createProfileDto(Profile profile, MetricsDTO metrics, RelationshipDTO relationship) {
        return ProfileMapper.toDTO(profile, metrics, relationship);
    }

    private void mockAuthenticatedUser() {
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedAccount);
        when(profileRepository.findByUsername(authenticatedAccount.getUsername()))
            .thenReturn(Optional.of(authenticatedProfile));
    }

    private void mockProfileRepository(Profile profile) {
        when(profileRepository.findByUsername(profile.getUsername())).thenReturn(Optional.of(profile));
    }

    private void mockMetricsService(Profile profile) {
        when(profileMetricsService.getMetrics(profile)).thenReturn(createMetricsDto());
    }

    private void mockRelationshipService(Profile profile) {
        when(relationshipService.getRelationship(authenticatedProfile, profile)).thenReturn(createRelationshipDto());
    }

    // ---- tests ----

    /**
     * Test ensures that the {@link ProfileServiceImpl#getByUsername(String)} method
     * correctly returns the profile when the username exists.
     */
    @Test
    void ProfileService_GetByUsername_ReturnProfileDTO() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");
        ProfileDTO expected = createProfileDto(testProfile, createMetricsDto(), createRelationshipDto());

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        mockMetricsService(testProfile);
        mockRelationshipService(testProfile);

        // act
        ProfileDTO actual = profileService.getByUsername(username);

        // assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findByUsername(username);
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

        // mock
        mockAuthenticatedUser();
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
    void ProfileService_GetMe_ReturnProfileDTO() {
        // arrange
        ProfileDTO expected = createProfileDto(authenticatedProfile, createMetricsDto(), null);

        // mock
        mockAuthenticatedUser();
        mockMetricsService(authenticatedProfile);

        // act
        ProfileDTO actual = profileService.getMe();

        // assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findByUsername(authenticatedAccount.getUsername());
    }

    /**
     * Test ensures that the {@link ProfileServiceImpl#getMe()} method correctly
     * throws a {@link UsernameNotFoundException} when no such profile with the
     * authenticated account's username exists.
     */
    @Test
    void ProfileService_GetMe_ThrowsUsernameNotFound() {
        // arrange
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedAccount);
        when(profileRepository.findByUsername(authenticatedAccount.getUsername())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.getMe());
        verify(profileRepository, times(1)).findByUsername(authenticatedAccount.getUsername());
    }

    /**
     * Test ensures that the
     * {@link ProfileServiceImpl#updateMeProfile(UpdateProfileDTO)} method correctly
     * updates the authenticated account's profile information.
     */
    @Test
    void ProfileService_UpdateMeProfile_ReturnVoid() {
        // arrange
        UpdateProfileDTO request = new UpdateProfileDTO(
            "John Doe",
            "Bio",
            "Location");

        mockAuthenticatedUser();

        // act
        profileService.updateMeProfile(request);

        // assert
        assertEquals(request.name(), authenticatedProfile.getName());
        assertEquals(request.bio(), authenticatedProfile.getBio());
        assertEquals(request.location(), authenticatedProfile.getLocation());
        verify(profileRepository, times(1)).findByUsername(authenticatedProfile.getUsername());
    }

    /**
     * Test ensures that the
     * {@link ProfileServiceImpl#updateMeProfile(UpdateProfileDTO)} method correctly
     * throws a {@link UsernameNotFoundException} when no such profile with the
     * authenticated account's username exists.
     */
    @Test
    void ProfileService_UpdateMeProfile_ThrowUsernameNotFound() {
        // arrange
        UpdateProfileDTO request = new UpdateProfileDTO(
            "John Doe",
            "Bio",
            "Location");

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedAccount);
        when(profileRepository.findByUsername(authenticatedAccount.getUsername())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.updateMeProfile(request));
        verify(profileRepository, times(1)).findByUsername(authenticatedAccount.getUsername());
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowers(String)} correctly
     * returns a non-empty {@link PageDTO} of {@link ProfileDTO} when the
     * {@link Profile} associated to the {@code username} has at least 1 follower.
     */
    @Test
    void ProfileService_GetFollowers_ReturnPageOfProfileDto() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");
        ProfileDTO testProfileDto = createProfileDto(testProfile, createMetricsDto(), createRelationshipDto());

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<Profile> followers = new PageImpl<>(List.of(testProfile), page, 0);
        Page<ProfileDTO> followersDto = new PageImpl<>(List.of(testProfileDto), page, 0);
        PageDTO<ProfileDTO> expected = PageMapper.toDTO(followersDto, uri);

        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        mockMetricsService(testProfile);
        mockRelationshipService(testProfile);
        when(profileRepository.findAllFollowersById(testProfile.getProfileId(), page)).thenReturn(followers);
        when(httpRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<ProfileDTO> actual = profileService.getFollowers(username, page);

        // assert
        assertFalse(actual.items().isEmpty());
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findAllFollowersById(testProfile.getProfileId(), page);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowers(String)} correctly
     * returns an empty {@link PageDTO} of {@link ProfileDTO} when the
     * {@link Profile} associated to the {@code username} has no followers.
     */
    @Test
    void ProfileService_GetFollowers_ReturnPageOfEmpty() throws Exception {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<Profile> emptyFollowers = new PageImpl<>(new ArrayList<>(), page, 0);
        Page<ProfileDTO> emptyFollowersDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<ProfileDTO> expected = PageMapper.toDTO(emptyFollowersDto, uri);

        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        when(profileRepository.findAllFollowersById(testProfile.getProfileId(), page)).thenReturn(emptyFollowers);
        when(httpRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<ProfileDTO> actual = profileService.getFollowers(username, page);

        // assert
        assertTrue(actual.items().isEmpty());
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findAllFollowersById(testProfile.getProfileId(), page);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowers(String)} throws
     * {@link UsernameNotFoundException} when no such profile with the supplied
     * username exists.
     */
    @Test
    void ProfileService_GetFollowers_ThrowsUsernameNotFound() {
        // arrange
        String username = "non-existing-user";

        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);

        mockAuthenticatedUser();
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.getFollowers(username, page));
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowing(String)} correctly
     * returns a non-empty {@link PageDTO} of {@link ProfileDTO} when the
     * {@link Profile} associated to the {@code username} has at least 1 following.
     */
    @Test
    void ProfileService_GetFollowing_ReturnPageOfProfileDto() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");
        ProfileDTO testProfileDto = createProfileDto(testProfile, createMetricsDto(), createRelationshipDto());

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<Profile> followers = new PageImpl<>(List.of(testProfile), page, 0);
        Page<ProfileDTO> followersDto = new PageImpl<>(List.of(testProfileDto), page, 0);
        PageDTO<ProfileDTO> expected = PageMapper.toDTO(followersDto, uri);

        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        mockMetricsService(testProfile);
        mockRelationshipService(testProfile);
        when(profileRepository.findAllFollowingById(testProfile.getProfileId(), page)).thenReturn(followers);
        when(httpRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<ProfileDTO> actual = profileService.getFollowing(username, page);

        // assert
        assertFalse(actual.items().isEmpty());
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findAllFollowingById(testProfile.getProfileId(), page);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowing(String)} correctly
     * returns an empty {@link PageDTO} of {@link ProfileDTO} when the
     * {@link Profile} associated to the {@code username} has no following.
     */
    @Test
    void ProfileService_GetFollowing_ReturnPageOfEmpty() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);

        Page<Profile> emptyFollowers = new PageImpl<>(new ArrayList<>(), page, 0);
        Page<ProfileDTO> emptyFollowersDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<ProfileDTO> expected = PageMapper.toDTO(emptyFollowersDto, uri);

        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        when(profileRepository.findAllFollowingById(testProfile.getProfileId(), page)).thenReturn(emptyFollowers);
        when(httpRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<ProfileDTO> actual = profileService.getFollowing(username, page);

        // assert
        assertTrue(actual.items().isEmpty());
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findAllFollowingById(testProfile.getProfileId(), page);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#getFollowing(String)} throws
     * {@link UsernameNotFoundException} when no such profile with the supplied
     * username exists.
     */
    @Test
    void ProfileService_GetFollowing_ThrowsUsernameNotFound() {
        // arrange
        String username = "non-existing-user";

        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);

        mockAuthenticatedUser();
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.getFollowing(username, page));
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#follow(String)} does not throw
     * any exceptions when called.
     */
    @Test
    void ProfileService_Follow_ReturnVoid() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doNothing().when(relationshipService).follow(authenticatedProfile, testProfile);

        // act & assert
        assertDoesNotThrow(() -> profileService.follow(username));
        verify(relationshipService, times(1)).follow(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#follow(String)} throws
     * {@link UsernameNotFoundException} when the supplied username does not exist.
     */
    @Test
    void ProfileService_Follow_Throw400UsernameNotFoundException() {
        // arrange
        String username = "non-existent-user";

        // mock
        mockAuthenticatedUser();
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.follow(username));
        verify(profileRepository, times(1)).findByUsername(username);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#follow(String)} throws
     * {@link SelfActionException} when the supplied username is equal to the
     * current authenticated user.
     */
    @Test
    void ProfileService_Follow_ThrowSelfActionException() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_FOLLOW)).when(relationshipService)
            .follow(authenticatedProfile, testProfile);

        // act & assert
        assertThrows(SelfActionException.class, () -> profileService.follow(username));
        verify(relationshipService, times(1)).follow(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#follow(String)} throws
     * {@link AlreadyFollowingException} when the supplied username is already
     * followed by the authenticated user.
     */
    @Test
    void ProfileService_Follow_ThrowAlreadyFollowingException() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doThrow(new AlreadyFollowingException()).when(relationshipService).follow(authenticatedProfile, testProfile);

        // act & assert
        assertThrows(AlreadyFollowingException.class, () -> profileService.follow(username));
        verify(relationshipService, times(1)).follow(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#follow(String)} throws
     * {@link BlockedException} when the supplied username is blocking the
     * authenticated user.
     */
    @Test
    void ProfileService_Follow_ThrowBlockedException() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doThrow(new BlockedException()).when(relationshipService).follow(authenticatedProfile, testProfile);

        // act & assert
        assertThrows(BlockedException.class, () -> profileService.follow(username));
        verify(relationshipService, times(1)).follow(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unfollow(String)} does not throw
     * any exceptions when called.
     */
    @Test
    void ProfileService_Unfollow_ReturnVoid() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doNothing().when(relationshipService).unfollow(authenticatedProfile, testProfile);

        // act & assert
        assertDoesNotThrow(() -> profileService.unfollow(username));
        verify(relationshipService, times(1)).unfollow(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unfollow(String)} throws
     * {@link UsernameNotFoundException} when the supplied username does not exist.
     */
    @Test
    void ProfileService_Unfollow_Throw400UsernameNotFound() {
        // arrange
        String username = "non-existent-user";

        // mock
        mockAuthenticatedUser();
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.unfollow(username));
        verify(profileRepository, times(1)).findByUsername(username);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unfollow(String)} throws
     * {@link SelfActionException} when the supplied username is equal to the
     * current authenticated user.
     */
    @Test
    void ProfileService_Unfollow_ThrowSelfActionException() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_FOLLOW)).when(relationshipService)
            .unfollow(authenticatedProfile, testProfile);

        // act & assert
        assertThrows(SelfActionException.class, () -> profileService.unfollow(username));
        verify(relationshipService, times(1)).unfollow(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unfollow(String)} throws
     * {@link NotFollowingException} when the supplied username is not already
     * followed by the authenticated user.
     */
    @Test
    void ProfileService_Unfollow_ThrowNotFollowingException() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doThrow(new NotFollowingException()).when(relationshipService).unfollow(authenticatedProfile, testProfile);

        // act & assert
        assertThrows(NotFollowingException.class, () -> profileService.unfollow(username));
        verify(relationshipService, times(1)).unfollow(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#block(String)} does not throw any
     * exceptions when called.
     */
    @Test
    void ProfileService_Block_ReturnVoid() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doNothing().when(relationshipService).block(authenticatedProfile, testProfile);

        // act & assert
        assertDoesNotThrow(() -> profileService.block(username));
        verify(relationshipService, times(1)).block(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#block(String)} throws
     * {@link UsernameNotFoundException} when the supplied username does not exist.
     */
    @Test
    void ProfileService_Block_Throw400UsernameNotFound() {
        // arrange
        String username = "non-existent-user";

        // mock
        mockAuthenticatedUser();
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.block(username));
        verify(profileRepository, times(1)).findByUsername(username);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#block(String)} throws
     * {@link SelfActionException} when the supplied username is equal to the
     * current authenticated user.
     */
    @Test
    void ProfileService_Block_ThrowSelfActionException() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_FOLLOW)).when(relationshipService)
            .block(authenticatedProfile, testProfile);

        // act & assert
        assertThrows(SelfActionException.class, () -> profileService.block(username));
        verify(relationshipService, times(1)).block(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#block(String)} throws
     * {@link AlreadyBlockingException} when the supplied username is already
     * blocked by the authenticated user.
     */
    @Test
    void ProfileService_Block_ThrowAlreadyBlockingException() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doThrow(new AlreadyBlockingException()).when(relationshipService).block(authenticatedProfile, testProfile);

        // act & assert
        assertThrows(AlreadyBlockingException.class, () -> profileService.block(username));
        verify(relationshipService, times(1)).block(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unblock(String)} does not throw
     * any exceptions when called.
     */
    @Test
    void ProfileService_Unblock_ReturnVoid() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doNothing().when(relationshipService).unblock(authenticatedProfile, testProfile);

        // act & assert
        assertDoesNotThrow(() -> profileService.unblock(username));
        verify(relationshipService, times(1)).unblock(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unblock(String)} throws
     * {@link UsernameNotFoundException} when the supplied username does not exist.
     */
    @Test
    void ProfileService_Unblock_Throw400UsernameNotFound() {
        // arrange
        String username = "non-existent-user";

        // mock
        mockAuthenticatedUser();
        when(profileRepository.findByUsername(username)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileService.unblock(username));
        verify(profileRepository, times(1)).findByUsername(username);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unblock(String)} throws
     * {@link SelfActionException} when the supplied username is equal to the
     * current authenticated user.
     */
    @Test
    void ProfileService_Unblock_ThrowSelfActionException() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doThrow(new SelfActionException(ErrorMessageConfig.SELF_FOLLOW)).when(relationshipService)
            .unblock(authenticatedProfile, testProfile);

        // act & assert
        assertThrows(SelfActionException.class, () -> profileService.unblock(username));
        verify(relationshipService, times(1)).unblock(authenticatedProfile, testProfile);
    }

    /**
     * Test ensures that {@link ProfileServiceImpl#unblock(String)} throws
     * {@link NotBlockingException} when the supplied username is already not
     * already blocked by the authenticated user.
     */
    @Test
    void ProfileService_Unblock_ThrowNotBlockingException() {
        // arrange
        String username = "existing-user";
        Profile testProfile = createTestProfile(username, "test");

        // mock
        mockAuthenticatedUser();
        mockProfileRepository(testProfile);
        doThrow(new NotBlockingException()).when(relationshipService).unblock(authenticatedProfile, testProfile);

        // act & assert
        assertThrows(NotBlockingException.class, () -> profileService.unblock(username));
        verify(relationshipService, times(1)).unblock(authenticatedProfile, testProfile);
    }

}
