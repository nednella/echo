package com.example.echo_api.modules.profile.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.modules.profile.dto.response.ProfileDTO;
import com.example.echo_api.modules.profile.dto.response.ProfileMetricsDTO;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.shared.pagination.OffsetLimitRequest;
import com.example.echo_api.shared.pagination.PageDTO;
import com.example.echo_api.shared.pagination.PageMapper;
import com.example.echo_api.shared.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Unit test class for {@link ProfileViewService}.
 */
@ExtendWith(MockitoExtension.class)
class ProfileViewServiceTest {

    @InjectMocks
    private ProfileViewServiceImpl profileViewService;

    @Mock
    private SessionService sessionService;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    private static UUID authenticatedUserId;

    @BeforeAll
    static void setup() {
        authenticatedUserId = UUID.randomUUID();
    }

    private Profile createProfile(UUID id, String username) {
        return Profile.forTest(id, username);
    }

    private ProfileDTO createProfileDto(UUID id, String username) {
        return new ProfileDTO(
            id.toString(),
            username,
            null,
            null,
            null,
            null,
            null,
            new ProfileMetricsDTO(0, 0, 0),
            null);
    }

    @Test
    void getMe_ReturnsProfileDto_WhenIExist() {
        // arrange
        ProfileDTO expected = createProfileDto(UUID.randomUUID(), "username");

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findProfileDtoById(authenticatedUserId, authenticatedUserId))
            .thenReturn(Optional.of(expected));

        // act
        ProfileDTO actual = profileViewService.getMe();

        // assert
        assertEquals(expected, actual);
        verify(sessionService).getAuthenticatedUserId();
        verify(profileRepository).findProfileDtoById(authenticatedUserId, authenticatedUserId);
    }

    @Test
    void getMe_ThrowsResourceNotFound_WhenIDoNotExist() {
        // arrange
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findProfileDtoById(authenticatedUserId, authenticatedUserId))
            .thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileViewService.getMe());
        verify(sessionService).getAuthenticatedUserId();
        verify(profileRepository).findProfileDtoById(authenticatedUserId, authenticatedUserId);
    }

    @Test
    void getById_ReturnsProfileDto_WhenProfileByIdExists() {
        // arrange
        UUID id = UUID.randomUUID();
        ProfileDTO expected = createProfileDto(id, "test");

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findProfileDtoById(id, authenticatedUserId))
            .thenReturn(Optional.of(expected));

        // act
        ProfileDTO actual = profileViewService.getById(id);

        // assert
        assertEquals(expected, actual);
        verify(sessionService).getAuthenticatedUserId();
        verify(profileRepository).findProfileDtoById(id, authenticatedUserId);
    }

    @Test
    void getById_ThrowsResourceNotFound_WhenProfileByIdDoesNotExist() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findProfileDtoById(id, authenticatedUserId))
            .thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileViewService.getById(id));
        verify(sessionService).getAuthenticatedUserId();
        verify(profileRepository).findProfileDtoById(id, authenticatedUserId);

    }

    @Test
    void getByUsername_ReturnsProfileDto_WhenProfileByUsernameExists() {
        // arrange
        String username = "test";
        ProfileDTO expected = createProfileDto(UUID.randomUUID(), username);

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findProfileDtoByUsername(username, authenticatedUserId))
            .thenReturn(Optional.of(expected));

        // act
        ProfileDTO actual = profileViewService.getByUsername(username);

        // assert
        assertEquals(expected, actual);
        verify(sessionService).getAuthenticatedUserId();
        verify(profileRepository).findProfileDtoByUsername(username, authenticatedUserId);
    }

    @Test
    void getByUsername_ThrowsResourceNotFound_WhenProfileByUsernameDoesNotExist() {
        // arrange
        String username = "test";

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findProfileDtoByUsername(username, authenticatedUserId))
            .thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileViewService.getByUsername(username));
        verify(sessionService).getAuthenticatedUserId();
        verify(profileRepository).findProfileDtoByUsername(username, authenticatedUserId);
    }

    @Test
    void getFollowers_ReturnsPageDtoOfProfileDto_WhenProfileByIdExists() {
        // arrange
        UUID id = UUID.randomUUID();
        Profile profile = createProfile(id, "username");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 1;
        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<SimplifiedProfileDTO> followersDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(followersDto, uri);

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findFollowerDtosById(profile.getId(), authenticatedUserId, page))
            .thenReturn(followersDto);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<SimplifiedProfileDTO> actual = profileViewService.getFollowers(id, page);

        // assert
        assertTrue(actual.items().isEmpty());
        assertEquals(expected, actual);
        verify(profileRepository).findFollowerDtosById(profile.getId(), authenticatedUserId, page);
    }

    @Test
    void getFollowers_ThrowsResourceNotFound_WhenProfileByIdDoesNotExist() {
        // arrange
        UUID id = UUID.randomUUID();

        int offset = 0;
        int limit = 1;
        Pageable page = OffsetLimitRequest.of(offset, limit);

        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileViewService.getFollowers(id, page));
    }

    @Test
    void getFollowing_ReturnsPageDtoOfProfileDto_WhenProfileByIdExists() {
        // arrange
        UUID id = UUID.randomUUID();
        Profile profile = createProfile(id, "username");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 1;
        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<SimplifiedProfileDTO> followingDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(followingDto, uri);

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(profileRepository.findFollowingDtosById(profile.getId(), authenticatedUserId, page))
            .thenReturn(followingDto);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<SimplifiedProfileDTO> actual = profileViewService.getFollowing(id, page);

        // assert
        assertTrue(actual.items().isEmpty());
        assertEquals(expected, actual);
        verify(profileRepository).findFollowingDtosById(profile.getId(), authenticatedUserId, page);
    }

    @Test
    void getFollowing_ThrowsResourceNotFound_WhenProfileByIdDoesNotExist() {
        // arrange
        UUID id = UUID.randomUUID();

        int offset = 0;
        int limit = 1;
        Pageable page = OffsetLimitRequest.of(offset, limit);

        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileViewService.getFollowing(id, page));
    }

}
