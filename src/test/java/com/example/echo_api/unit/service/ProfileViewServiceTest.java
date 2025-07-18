package com.example.echo_api.unit.service;

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
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileMetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.profile.view.ProfileViewService;
import com.example.echo_api.service.profile.view.ProfileViewServiceImpl;
import com.example.echo_api.service.session.SessionService;
import com.example.echo_api.util.pagination.OffsetLimitRequest;

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

    private static Account authenticatedUser;

    @BeforeAll
    static void setup() {
        authenticatedUser = new Account("user", "password");
    }

    private Profile createProfile(UUID id, String username) {
        return new Profile(id, username);
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
            null,
            new ProfileMetricsDTO(0, 0, 0),
            null);
    }

    @Test
    void ProfileViewService_GetMe_ReturnProfileDto() {
        // arrange
        ProfileDTO expected = createProfileDto(UUID.randomUUID(), authenticatedUser.getUsername());

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoById(authenticatedUser.getId(), authenticatedUser.getId()))
            .thenReturn(Optional.of(expected));

        // act
        ProfileDTO actual = profileViewService.getMe();

        // assert
        assertEquals(expected, actual);
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoById(authenticatedUser.getId(), authenticatedUser.getId());
    }

    @Test
    void ProfileViewService_GetMe_ThrowResourceNotFound() {
        // arrange
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoById(authenticatedUser.getId(), authenticatedUser.getId()))
            .thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileViewService.getMe());
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoById(authenticatedUser.getId(), authenticatedUser.getId());
    }

    @Test
    void ProfileViewService_GetById_ReturnProfileDto() {
        // arrange
        UUID id = UUID.randomUUID();
        ProfileDTO expected = createProfileDto(id, "test");

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoById(id, authenticatedUser.getId()))
            .thenReturn(Optional.of(expected));

        // act
        ProfileDTO actual = profileViewService.getById(id);

        // assert
        assertEquals(expected, actual);
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoById(id, authenticatedUser.getId());
    }

    @Test
    void ProfileViewService_GetById_ThrowResourceNotFound() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoById(id, authenticatedUser.getId()))
            .thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileViewService.getById(id));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoById(id, authenticatedUser.getId());

    }

    @Test
    void ProfileViewService_GetByUsername_ReturnProfileDto() {
        // arrange
        String username = "test";
        ProfileDTO expected = createProfileDto(UUID.randomUUID(), username);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoByUsername(username, authenticatedUser.getId()))
            .thenReturn(Optional.of(expected));

        // act
        ProfileDTO actual = profileViewService.getByUsername(username);

        // assert
        assertEquals(expected, actual);
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoByUsername(username, authenticatedUser.getId());
    }

    @Test
    void ProfileViewService_GetByUsername_ThrowResourceNotFound() {
        // arrange
        String username = "test";

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoByUsername(username, authenticatedUser.getId()))
            .thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileViewService.getByUsername(username));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoByUsername(username, authenticatedUser.getId());

    }

    @Test
    void ProfileViewService_GetFollowers_ReturnPageDtoOfProfileDto() {
        // arrange
        UUID id = UUID.randomUUID();
        Profile profile = createProfile(id, "username");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);
        Page<SimplifiedProfileDTO> followersDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(followersDto, uri);

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findFollowerDtosById(profile.getId(), authenticatedUser.getId(), page))
            .thenReturn(followersDto);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<SimplifiedProfileDTO> actual = profileViewService.getFollowers(id, page);

        // assert
        assertTrue(actual.items().isEmpty());
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findFollowerDtosById(profile.getId(), authenticatedUser.getId(), page);
    }

    @Test
    void ProfileViewService_GetFollowers_ThrowResourceNotFound() {
        // arrange
        UUID id = UUID.randomUUID();

        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);

        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileViewService.getFollowers(id, page));
    }

    @Test
    void ProfileViewService_GetFollowing_ReturnPageDtoOfProfileDto() {
        // arrange
        UUID id = UUID.randomUUID();
        Profile profile = createProfile(id, "username");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);
        Page<SimplifiedProfileDTO> followingDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(followingDto, uri);

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findFollowingDtosById(profile.getId(), authenticatedUser.getId(), page))
            .thenReturn(followingDto);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<SimplifiedProfileDTO> actual = profileViewService.getFollowing(id, page);

        // assert
        assertTrue(actual.items().isEmpty());
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findFollowingDtosById(profile.getId(), authenticatedUser.getId(), page);
    }

    @Test
    void ProfileViewService_GetFollowing_ThrowResourceNotFound() {
        // arrange
        UUID id = UUID.randomUUID();

        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);

        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> profileViewService.getFollowing(id, page));
    }

}
