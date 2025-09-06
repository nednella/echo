package com.example.echo_api.modules.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.exception.ApplicationException;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.exception.PostErrorCode;
import com.example.echo_api.modules.post.repository.PostLikeRepository;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.shared.service.SessionService;

/**
 * Unit test class for {@link PostInteractionService}.
 */
@ExtendWith(MockitoExtension.class)
class PostInteractionServiceTest {

    @InjectMocks
    private PostInteractionServiceImpl postInteractionService;

    @Mock
    private SessionService sessionService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostLikeRepository likeRepository;

    private static UUID authenticatedUserId;
    private static Post post;

    @BeforeAll
    static void setup() {
        authenticatedUserId = UUID.randomUUID();
        post = Post.forTest(UUID.randomUUID(), null, UUID.randomUUID(), "Test post.");
    }

    @Test
    void like_ReturnsVoid_WhenLikeSuccessfullyCreated() {
        // arrange
        UUID id = post.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(likeRepository.existsByPostIdAndAuthorId(id, authenticatedUserId)).thenReturn(false);

        // act & assert
        assertDoesNotThrow(() -> postInteractionService.like(id));
        verify(postRepository).findById(id);
        verify(likeRepository).existsByPostIdAndAuthorId(id, authenticatedUserId);
    }

    @Test
    void like_ThrowsApplicationException_WhenPostByIdDoesNotExist() {
        // arrange
        PostErrorCode errorCode = PostErrorCode.ID_NOT_FOUND;
        UUID id = post.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> postInteractionService.like(id));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage(id));

        verify(postRepository).findById(id);
        verify(likeRepository, never()).existsByPostIdAndAuthorId(id, authenticatedUserId);
    }

    @Test
    void like_ThrowsApplicationException_WhenPostByIdAlreadyLikedByYou() {
        // arrange
        PostErrorCode errorCode = PostErrorCode.ALREADY_LIKED;
        UUID id = post.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(likeRepository.existsByPostIdAndAuthorId(id, authenticatedUserId)).thenReturn(true);

        // act & assert
        var ex = assertThrows(ApplicationException.class, () -> postInteractionService.like(id));
        assertThat(ex.getMessage()).isEqualTo(errorCode.formatMessage(id));

        verify(postRepository).findById(id);
        verify(likeRepository).existsByPostIdAndAuthorId(id, authenticatedUserId);
    }

    @Test
    void unlike_ReturnsVoid() {
        // arrange
        UUID id = post.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);

        // act & assert
        assertDoesNotThrow(() -> postInteractionService.unlike(id));
    }

}
