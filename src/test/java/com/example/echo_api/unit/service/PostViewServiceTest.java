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
import com.example.echo_api.persistence.dto.response.post.PostMetricsDTO;
import com.example.echo_api.persistence.dto.response.post.PostRelationshipDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.example.echo_api.persistence.mapper.PageMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.view.post.PostViewService;
import com.example.echo_api.service.post.view.post.PostViewServiceImpl;
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
            new PostMetricsDTO(0, 0, 0),
            new PostRelationshipDTO(false));
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

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(postRepository.findReplyDtosById(post.getId(), authenticatedUser.getId(), page)).thenReturn(repliesDto);
        when(httpServletRequest.getRequestURI()).thenReturn(uri);

        // act
        PageDTO<PostDTO> actual = postViewService.getPostRepliesById(id, page);

        // assert
        assertEquals(expected, actual);
        verify(postRepository, times(1)).findById(id);
        verify(postRepository, times(1)).findReplyDtosById(post.getId(), authenticatedUser.getId(), page);
    }

    @Test
    void PostViewService_GetPostRepliesById_ThrowResourceNotFoundException() {
        // arrange
        UUID id = UUID.randomUUID();

        int offset = 0;
        int limit = 20;
        Pageable page = new OffsetLimitRequest(offset, limit);

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postViewService.getPostRepliesById(id, page));
        verify(postRepository, times(1)).findById(id);
        verify(postRepository, times(0)).findReplyDtosById(any(UUID.class), eq(authenticatedUser.getId()), eq(page));
    }

}
