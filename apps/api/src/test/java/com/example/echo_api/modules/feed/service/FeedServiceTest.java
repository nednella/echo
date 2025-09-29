package com.example.echo_api.modules.feed.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.modules.post.dto.response.PostEntitiesDTO;
import com.example.echo_api.modules.post.dto.response.PostMetricsDTO;
import com.example.echo_api.modules.post.dto.response.PostRelationshipDTO;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.modules.profile.exception.ProfileErrorCode;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.shared.pagination.OffsetLimitRequest;
import com.example.echo_api.shared.pagination.Paged;
import com.example.echo_api.shared.pagination.PageMapper;
import com.example.echo_api.shared.service.SessionService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Unit test class for {@link FeedService}.
 */
@ExtendWith(MockitoExtension.class)
class FeedServiceTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private FeedServiceImpl feedService;

    private static UUID authenticatedUserId;
    private static Pageable page;

    @BeforeAll
    static void setup() {
        authenticatedUserId = UUID.randomUUID();
        page = OffsetLimitRequest.of(0, 20);
    }

    private PostDTO createPostDto(UUID id, String text) {
        return new PostDTO(
            id.toString(),
            null,
            id.toString(),
            new SimplifiedProfileDTO(
                UUID.randomUUID().toString(),
                "username",
                "name",
                null,
                null),
            text,
            null,
            new PostMetricsDTO(0, 0),
            new PostRelationshipDTO(false),
            new PostEntitiesDTO(List.of(), List.of(), List.of()));
    }

    @Test
    void getHomeFeed_ReturnPageDtoOfPostDto() {
        // arrange
        String uri = "/some/api/uri";
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        Paged<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findHomepagePosts(authenticatedUserId, page)).thenReturn(posts);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        Paged<PostDTO> actual = feedService.getHomeFeed(page);

        // assert
        assertEquals(expected, actual);
        verify(postRepository).findHomepagePosts(authenticatedUserId, page);
    }

    @Test
    void getDiscoverFeed_ReturnPageDtoOfPostDto() {
        // arrange
        String uri = "/some/api/uri";
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        Paged<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findDiscoverPosts(authenticatedUserId, page)).thenReturn(posts);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        Paged<PostDTO> actual = feedService.getDiscoverFeed(page);

        // assert
        assertEquals(expected, actual);
        verify(postRepository).findDiscoverPosts(authenticatedUserId, page);
    }

    @Test
    void getProfilePosts_ReturnPageDtoOfPostDto_WhenProfileByIdExists() {
        // arrange
        UUID id = UUID.randomUUID();

        String uri = "/some/api/uri";
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        Paged<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.existsById(id)).thenReturn(true);
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findPostsByProfileId(id, authenticatedUserId, page)).thenReturn(posts);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        Paged<PostDTO> actual = feedService.getProfilePosts(id, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository).existsById(id);
        verify(postRepository).findPostsByProfileId(id, authenticatedUserId, page);
    }

    @Test
    void getProfilePosts_ThrowsApplicationException_WhenProfileByIdDoesNotExist() {
        // arrange
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID id = UUID.randomUUID();

        when(profileRepository.existsById(id)).thenReturn(false);

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> feedService.getProfilePosts(id, page));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage(id));

        verify(profileRepository).existsById(id);
        verify(postRepository, never()).findPostsByProfileId(any(UUID.class), eq(authenticatedUserId), eq(page));
    }

    @Test
    void getProfileReplies_ReturnPageDtoOfPostDto_WhenProfileByIdExists() {
        // arrange
        UUID id = UUID.randomUUID();

        String uri = "/some/api/uri";
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        Paged<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.existsById(id)).thenReturn(true);
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findRepliesByProfileId(id, authenticatedUserId, page)).thenReturn(posts);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        Paged<PostDTO> actual = feedService.getProfileReplies(id, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository).existsById(id);
        verify(postRepository).findRepliesByProfileId(id, authenticatedUserId, page);
    }

    @Test
    void getProfileReplies_ThrowsApplicationException_WhenProfileByIdDoesNotExist() {
        // arrange
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID id = UUID.randomUUID();

        when(profileRepository.existsById(id)).thenReturn(false);

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> feedService.getProfileReplies(id, page));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage(id));

        verify(profileRepository).existsById(id);
        verify(postRepository, never()).findRepliesByProfileId(any(UUID.class), eq(authenticatedUserId), eq(page));
    }

    @Test
    void getProfileLikes_ReturnPageDtoOfPostDto_WhenProfileByIdExists() {
        // arrange
        UUID id = UUID.randomUUID();

        String uri = "/some/api/uri";
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        Paged<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.existsById(id)).thenReturn(true);
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findPostsLikedByProfileId(id, authenticatedUserId, page)).thenReturn(posts);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        Paged<PostDTO> actual = feedService.getProfileLikes(id, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository).existsById(id);
        verify(postRepository).findPostsLikedByProfileId(id, authenticatedUserId, page);
    }

    @Test
    void getProfileLikes_ThrowsApplicationException_WhenProfileByIdDoesNotExist() {
        // arrange
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID id = UUID.randomUUID();

        when(profileRepository.existsById(id)).thenReturn(false);

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> feedService.getProfileLikes(id, page));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage(id));

        verify(profileRepository).existsById(id);
        verify(postRepository, never()).findPostsLikedByProfileId(any(UUID.class), eq(authenticatedUserId), eq(page));
    }

    @Test
    void getProfileMentions_ReturnPageDtoOfPostDto_WhenProfileByIdExists() {
        // arrange
        UUID id = UUID.randomUUID();

        String uri = "/some/api/uri";
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        Paged<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.existsById(id)).thenReturn(true);
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findPostsMentioningProfileId(id, authenticatedUserId, page)).thenReturn(posts);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        Paged<PostDTO> actual = feedService.getProfileMentions(id, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository).existsById(id);
        verify(postRepository).findPostsMentioningProfileId(id, authenticatedUserId, page);
    }

    @Test
    void getProfileMentions_ThrowsApplicationException_WhenProfileByIdDoesNotExist() {
        // arrange
        ProfileErrorCode errorCode = ProfileErrorCode.ID_NOT_FOUND;
        UUID id = UUID.randomUUID();

        when(profileRepository.existsById(id)).thenReturn(false);

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> feedService.getProfileMentions(id, page));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage(id));

        verify(profileRepository).existsById(id);
        verify(postRepository, never()).findPostsMentioningProfileId(id, authenticatedUserId, page);
    }

}
