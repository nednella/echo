package com.example.echo_api.unit.service.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
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
import com.example.echo_api.modules.profile.dto.SimplifiedProfileDTO;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.persistence.dto.response.post.PostEntitiesDTO;
import com.example.echo_api.persistence.dto.response.post.PostMetricsDTO;
import com.example.echo_api.persistence.dto.response.post.PostRelationshipDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.view.PostViewService;
import com.example.echo_api.service.post.view.PostViewServiceImpl;
import com.example.echo_api.shared.service.SessionService;
import com.example.echo_api.util.OffsetLimitRequest;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Unit test class for {@link PostViewService}.
 */
@ExtendWith(MockitoExtension.class)
class PostViewServiceTest {

    @InjectMocks
    private PostViewServiceImpl postViewService;

    @Mock
    private SessionService sessionService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    private static UUID authenticatedUserId;

    @BeforeAll
    static void setup() {
        authenticatedUserId = UUID.randomUUID();
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

    private Profile createProfileEntity(UUID id, String username) {
        return Profile.forTest(id, username);
    }

    @Test
    void getPostById_ReturnsPostDto_WhenPostByIdExists() {
        // arrange
        UUID id = UUID.randomUUID();
        PostDTO expected = createPostDto(id, "Test post.");

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findPostDtoById(id, authenticatedUserId)).thenReturn(Optional.of(expected));

        // act
        PostDTO actual = postViewService.getPostById(id);

        // assert
        assertEquals(expected, actual);
        verify(postRepository).findPostDtoById(id, authenticatedUserId);
    }

    @Test
    void getPostById_ThrowsResourceNotFound_WhenPostByIdDoesNotExist() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findPostDtoById(id, authenticatedUserId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getPostById(id));
        verify(postRepository).findPostDtoById(id, authenticatedUserId);
    }

    @Test
    void getRepliesById_ReturnPageDtoOfPostDto_WhenPostByIdExists() {
        // arrange
        UUID id = UUID.randomUUID();
        Post post = new Post(UUID.randomUUID(), "Test post.");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> repliesDto = new PageImpl<>(List.of(createPostDto(id, "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(repliesDto, uri);

        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findRepliesById(post.getId(), authenticatedUserId, page)).thenReturn(repliesDto);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<PostDTO> actual = postViewService.getRepliesById(id, page);

        // assert
        assertEquals(expected, actual);
        verify(postRepository).findById(id);
        verify(postRepository).findRepliesById(post.getId(), authenticatedUserId, page);
    }

    @Test
    void getRepliesById_ThrowsResourceNotFound_WhenPostByIdDoesNotExist() {
        // arrange
        UUID id = UUID.randomUUID();

        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);

        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getRepliesById(id, page));
        verify(postRepository).findById(id);
        verify(postRepository, never()).findRepliesById(any(UUID.class), eq(authenticatedUserId), eq(page));
    }

    @Test
    void getHomepagePosts_ReturnPageDtoOfPostDto() {
        // arrange
        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findHomepagePosts(authenticatedUserId, page)).thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getHomepagePosts(page);

        // assert
        assertEquals(expected, actual);
        verify(postRepository).findHomepagePosts(authenticatedUserId, page);
    }

    @Test
    void getDiscoverPosts_ReturnPageDtoOfPostDto() {
        // arrange
        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findDiscoverPosts(authenticatedUserId, page)).thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getDiscoverPosts(page);

        // assert
        assertEquals(expected, actual);
        verify(postRepository).findDiscoverPosts(authenticatedUserId, page);
    }

    @Test
    void getPostsByAuthorId_ReturnPageDtoOfPostDto_WhenProfileByIdExists() {
        // arrange
        UUID id = UUID.randomUUID();
        Profile profile = createProfileEntity(id, "random_string");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findPostsByProfileId(profile.getId(), authenticatedUserId, page)).thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getPostsByAuthorId(id, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository).findById(id);
        verify(postRepository).findPostsByProfileId(profile.getId(), authenticatedUserId, page);
    }

    @Test
    void getPostsByAuthorId_ThrowsResourceNotFound_WhenProfileByIdDoesNotExist() {
        // arrange
        UUID id = UUID.randomUUID();

        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);

        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getPostsByAuthorId(id, page));
        verify(profileRepository).findById(id);
        verify(postRepository, never()).findPostsByProfileId(any(UUID.class), eq(authenticatedUserId), eq(page));
    }

    @Test
    void getRepliesByAuthorId_ReturnPageDtoOfPostDto_WhenProfileByIdExists() {
        // arrange
        UUID profileId = UUID.randomUUID();
        Profile profile = createProfileEntity(profileId, "random-string");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findRepliesByProfileId(profileId, authenticatedUserId, page)).thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getRepliesByAuthorId(profileId, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository).findById(profileId);
        verify(postRepository).findRepliesByProfileId(profile.getId(), authenticatedUserId, page);
    }

    @Test
    void getRepliesByAuthorId_ThrowsResourceNotFound_WhenProfileByIdDoesNotExist() {
        // arrange
        UUID profileId = UUID.randomUUID();

        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getRepliesByAuthorId(profileId, page));
        verify(profileRepository).findById(profileId);
        verify(postRepository, never()).findRepliesByProfileId(any(UUID.class), eq(authenticatedUserId),
            eq(page));
    }

    @Test
    void getLikesByAuthorId_ReturnPageDtoOfPostDto_WhenProfileByIdExists() {
        // arrange
        UUID profileId = UUID.randomUUID();
        Profile profile = createProfileEntity(profileId, "random-string");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findPostsLikedByProfileId(profileId, authenticatedUserId, page)).thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getLikesByAuthorId(profileId, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository).findById(profileId);
        verify(postRepository).findPostsLikedByProfileId(profile.getId(), authenticatedUserId, page);
    }

    @Test
    void getLikesByAuthorId_ThrowsResourceNotFound_WhenProfileByIdDoesNotExist() {
        // arrange
        UUID profileId = UUID.randomUUID();

        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getLikesByAuthorId(profileId, page));
        verify(profileRepository).findById(profileId);
        verify(postRepository, never()).findPostsLikedByProfileId(any(UUID.class), eq(authenticatedUserId),
            eq(page));
    }

    @Test
    void getMentionsOfAuthorId_ReturnPageDtoOfPostDto_WhenProfileByIdExists() {
        // arrange
        UUID profileId = UUID.randomUUID();
        Profile profile = createProfileEntity(profileId, "random-string");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findPostsMentioningProfileId(profileId, authenticatedUserId, page)).thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getMentionsOfAuthorId(profileId, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository).findById(profileId);
        verify(postRepository).findPostsMentioningProfileId(profile.getId(), authenticatedUserId, page);
    }

    @Test
    void getMentionsOfAuthorId_ThrowsResourceNotFound_WhenProfileByIdDoesNotExist() {
        // arrange
        UUID profileId = UUID.randomUUID();

        int offset = 0;
        int limit = 20;
        Pageable page = OffsetLimitRequest.of(offset, limit);

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getMentionsOfAuthorId(profileId, page));
        verify(profileRepository).findById(profileId);
        verify(postRepository, never()).findPostsMentioningProfileId(any(UUID.class), eq(authenticatedUserId),
            eq(page));
    }

}
