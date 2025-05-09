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

import com.example.echo_api.exception.custom.account.IdNotFoundException;
import com.example.echo_api.exception.custom.username.UsernameNotFoundException;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
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
            new MetricsDTO(0, 0, 0, 0),
            null);
    }

    @Test
    void ProfileViewService_GetSelf_ReturnProfileDto() {
        // arrange
        ProfileDTO expected = createProfileDto(UUID.randomUUID(), authenticatedUser.getUsername());

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoById(authenticatedUser.getId(), authenticatedUser.getId()))
            .thenReturn(Optional.of(expected));

        // act
        ProfileDTO actual = profileViewService.getSelf();

        // assert
        assertEquals(expected, actual);
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoById(authenticatedUser.getId(), authenticatedUser.getId());
    }

    @Test
    void ProfileViewService_GetSelf_ThrowIdNotFound() {
        // arrange
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoById(authenticatedUser.getId(), authenticatedUser.getId()))
            .thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> profileViewService.getSelf());
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoById(authenticatedUser.getId(), authenticatedUser.getId());
    }

    @Test
    void ProfileViewService_GetById_ReturnProfileDto() {
        // arrange
        UUID targetId = UUID.randomUUID();
        ProfileDTO expected = createProfileDto(targetId, "test");

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoById(targetId, authenticatedUser.getId()))
            .thenReturn(Optional.of(expected));

        // act
        ProfileDTO actual = profileViewService.getById(targetId);

        // assert
        assertEquals(expected, actual);
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoById(targetId, authenticatedUser.getId());
    }

    @Test
    void ProfileViewService_GetById_ThrowIdNotFound() {
        // arrange
        UUID targetId = UUID.randomUUID();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoById(targetId, authenticatedUser.getId()))
            .thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> profileViewService.getById(targetId));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoById(targetId, authenticatedUser.getId());

    }

    @Test
    void ProfileViewService_GetByUsername_ReturnProfileDto() {
        // arrange
        String targetUsername = "test";
        ProfileDTO expected = createProfileDto(UUID.randomUUID(), targetUsername);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoByUsername(targetUsername, authenticatedUser.getId()))
            .thenReturn(Optional.of(expected));

        // act
        ProfileDTO actual = profileViewService.getByUsername(targetUsername);

        // assert
        assertEquals(expected, actual);
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoByUsername(targetUsername, authenticatedUser.getId());
    }

    @Test
    void ProfileViewService_GetByUsername_ThrowUsernameNotFound() {
        // arrange
        String targetUsername = "test";

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findProfileDtoByUsername(targetUsername, authenticatedUser.getId()))
            .thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileViewService.getByUsername(targetUsername));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(profileRepository, times(1)).findProfileDtoByUsername(targetUsername, authenticatedUser.getId());

    }

    @Test
    void ProfileViewService_GetFollowers_ReturnPageDtoOfProfileDto() {
        // arrange
        String targetUsername = "non-existing-user";
        Profile targetProfile = createProfile(UUID.randomUUID(), targetUsername);

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);
        Page<SimplifiedProfileDTO> followersDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(followersDto, uri);

        when(profileRepository.findByUsername(targetUsername)).thenReturn(Optional.of(targetProfile));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findFollowerDtosById(targetProfile.getId(), authenticatedUser.getId(), page))
            .thenReturn(followersDto);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<SimplifiedProfileDTO> actual = profileViewService.getFollowers(targetUsername, page);

        // assert
        assertTrue(actual.items().isEmpty());
        assertEquals(expected, actual);
        verify(profileRepository, times(1))
            .findFollowerDtosById(targetProfile.getId(), authenticatedUser.getId(), page);
    }

    @Test
    void ProfileViewService_GetFollowers_ThrowUsernameNotFound() {
        // arrange
        String targetUsername = "non-existing-user";

        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);

        when(profileRepository.findByUsername(targetUsername)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileViewService.getFollowers(targetUsername, page));
    }

    @Test
    void ProfileViewService_GetFollowing_ReturnPageDtoOfProfileDto() {
        // arrange
        String targetUsername = "non-existing-user";
        Profile targetProfile = createProfile(UUID.randomUUID(), targetUsername);

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);
        Page<SimplifiedProfileDTO> followingDto = new PageImpl<>(new ArrayList<>(), page, 0);
        PageDTO<SimplifiedProfileDTO> expected = PageMapper.toDTO(followingDto, uri);

        when(profileRepository.findByUsername(targetUsername)).thenReturn(Optional.of(targetProfile));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(profileRepository.findFollowingDtosById(targetProfile.getId(), authenticatedUser.getId(), page))
            .thenReturn(followingDto);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<SimplifiedProfileDTO> actual = profileViewService.getFollowing(targetUsername, page);

        // assert
        assertTrue(actual.items().isEmpty());
        assertEquals(expected, actual);
        verify(profileRepository, times(1))
            .findFollowingDtosById(targetProfile.getId(), authenticatedUser.getId(), page);
    }

    @Test
    void ProfileViewService_GetFollowing_ThrowUsernameNotFound() {
        // arrange
        String targetUsername = "non-existing-user";

        int offset = 0;
        int limit = 1;
        Pageable page = new OffsetLimitRequest(offset, limit);

        when(profileRepository.findByUsername(targetUsername)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(UsernameNotFoundException.class, () -> profileViewService.getFollowing(targetUsername, page));
    }

}
