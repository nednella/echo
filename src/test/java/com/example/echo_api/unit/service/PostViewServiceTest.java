package com.example.echo_api.unit.service;

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
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.post.PostDTO;
import com.example.echo_api.persistence.dto.response.post.PostEntitiesDTO;
import com.example.echo_api.persistence.dto.response.post.PostMetricsDTO;
import com.example.echo_api.persistence.dto.response.post.PostRelationshipDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.post.view.PostViewService;
import com.example.echo_api.service.post.view.PostViewServiceImpl;
import com.example.echo_api.service.session.SessionService;
import com.example.echo_api.util.pagination.OffsetLimitRequest;

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

    private static Account authenticatedUser;

    @BeforeAll
    static void setup() {
        authenticatedUser = new Account(UUID.randomUUID(), "username", "password");
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
        return new Profile(id, username);
    }

    @Test
    void PostViewService_GetPostById_ReturnPostDto() {
        // arrange
        UUID id = UUID.randomUUID();
        PostDTO expected = createPostDto(id, "Test post.");

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findPostDtoById(id, authenticatedUser.getId())).thenReturn(Optional.of(expected));

        // act
        PostDTO actual = postViewService.getPostById(id);

        // assert
        assertEquals(expected, actual);
        verify(postRepository, times(1)).findPostDtoById(id, authenticatedUser.getId());
    }

    @Test
    void PostViewService_GetPostById_ThrowResourceNotFoundException() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findPostDtoById(id, authenticatedUser.getId())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getPostById(id));
        verify(postRepository, times(1)).findPostDtoById(id, authenticatedUser.getId());
    }

    @Test
    void PostViewService_GetPostRepliesById_ReturnPageDtoOfPostDto() {
        // arrange
        UUID id = UUID.randomUUID();
        Post post = new Post(UUID.randomUUID(), "Test post.");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);
        Page<PostDTO> repliesDto = new PageImpl<>(List.of(createPostDto(id, "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(repliesDto, uri);

        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findReplyPostsById(post.getId(), authenticatedUser.getId(), page)).thenReturn(repliesDto);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<PostDTO> actual = postViewService.getPostRepliesById(id, page);

        // assert
        assertEquals(expected, actual);
        verify(postRepository, times(1)).findById(id);
        verify(postRepository, times(1)).findReplyPostsById(post.getId(), authenticatedUser.getId(), page);
    }

    @Test
    void PostViewService_GetPostRepliesById_ThrowResourceNotFoundException() {
        // arrange
        UUID id = UUID.randomUUID();

        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);

        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getPostRepliesById(id, page));
        verify(postRepository, times(1)).findById(id);
        verify(postRepository, times(0)).findReplyPostsById(any(UUID.class), eq(authenticatedUser.getId()), eq(page));
    }

    @Test
    void PostViewService_GetHomepagePosts_ReturnPageDtoOfPostDto() {
        // arrange
        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findHomepagePosts(authenticatedUser.getId(), page)).thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getHomepagePosts(page);

        // assert
        assertEquals(expected, actual);
        verify(postRepository, times(1)).findHomepagePosts(authenticatedUser.getId(), page);
    }

    @Test
    void PostViewService_GetDiscoverPosts_ReturnPageDtoOfPostDto() {
        // arrange
        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findDiscoverPosts(authenticatedUser.getId(), page)).thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getDiscoverPosts(page);

        // assert
        assertEquals(expected, actual);
        verify(postRepository, times(1)).findDiscoverPosts(authenticatedUser.getId(), page);
    }

    @Test
    void PostViewService_GetProfilePostsById_ReturnPageDtoOfPostDto() {
        // arrange
        UUID id = UUID.randomUUID();
        Profile profile = createProfileEntity(id, "random_string");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.findById(id)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findPostsByProfileId(profile.getId(), authenticatedUser.getId(), page)).thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getProfilePostsById(id, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findById(id);
        verify(postRepository, times(1)).findPostsByProfileId(profile.getId(), authenticatedUser.getId(), page);
    }

    @Test
    void PostViewService_GetProfilePostsById_ThrowResourceNotFoundException() {
        // arrange
        UUID id = UUID.randomUUID();

        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);

        when(profileRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getProfilePostsById(id, page));
        verify(profileRepository, times(1)).findById(id);
        verify(postRepository, times(0)).findPostsByProfileId(any(UUID.class), eq(authenticatedUser.getId()), eq(page));
    }

    @Test
    void PostViewService_GetProfileRepliesById_ReturnPageDtoOfPostDto() {
        // arrange
        UUID profileId = UUID.randomUUID();
        Profile profile = createProfileEntity(profileId, "random-string");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findReplyPostsByProfileId(profileId, authenticatedUser.getId(), page)).thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getProfileRepliesById(profileId, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findById(profileId);
        verify(postRepository, times(1)).findReplyPostsByProfileId(profile.getId(), authenticatedUser.getId(), page);
    }

    @Test
    void PostViewService_GetProfileRepliesById_ThrowResourceNotFoundException() {
        // arrange
        UUID profileId = UUID.randomUUID();

        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getProfileRepliesById(profileId, page));
        verify(profileRepository, times(1)).findById(profileId);
        verify(postRepository, times(0)).findReplyPostsByProfileId(any(UUID.class), eq(authenticatedUser.getId()),
            eq(page));
    }

    @Test
    void PostViewService_GetProfileLikesById_ReturnPageDtoOfPostDto() {
        // arrange
        UUID profileId = UUID.randomUUID();
        Profile profile = createProfileEntity(profileId, "random-string");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findLikedPostsByProfileId(profileId, authenticatedUser.getId(), page)).thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getProfileLikesById(profileId, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findById(profileId);
        verify(postRepository, times(1)).findLikedPostsByProfileId(profile.getId(), authenticatedUser.getId(), page);
    }

    @Test
    void PostViewService_GetProfileLikesById_ThrowResourceNotFoundException() {
        // arrange
        UUID profileId = UUID.randomUUID();

        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getProfileLikesById(profileId, page));
        verify(profileRepository, times(1)).findById(profileId);
        verify(postRepository, times(0)).findReplyPostsById(any(UUID.class), eq(authenticatedUser.getId()), eq(page));
    }

    @Test
    void PostViewService_GetProfileMentionsById_ReturnPageDtoOfPostDto() {
        // arrange
        UUID profileId = UUID.randomUUID();
        Profile profile = createProfileEntity(profileId, "random-string");

        String uri = "/some/api/uri";
        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);
        Page<PostDTO> posts = new PageImpl<>(List.of(createPostDto(UUID.randomUUID(), "Test post.")), page, 1);
        PageDTO<PostDTO> expected = PageMapper.toDTO(posts, uri);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findMentionedPostsByProfileId(profileId, authenticatedUser.getId(), page))
            .thenReturn(posts);

        // act
        PageDTO<PostDTO> actual = postViewService.getProfileMentionsById(profileId, page);

        // assert
        assertEquals(expected, actual);
        verify(profileRepository, times(1)).findById(profileId);
        verify(postRepository, times(1)).findMentionedPostsByProfileId(profile.getId(), authenticatedUser.getId(),
            page);
    }

    @Test
    void PostViewService_GetProfileMentionsById_ThrowResourceNotFoundException() {
        // arrange
        UUID profileId = UUID.randomUUID();

        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);

        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getProfileMentionsById(profileId, page));
        verify(profileRepository, times(1)).findById(profileId);
        verify(postRepository, times(0)).findMentionedPostsByProfileId(any(UUID.class), eq(authenticatedUser.getId()),
            eq(page));
    }

}
