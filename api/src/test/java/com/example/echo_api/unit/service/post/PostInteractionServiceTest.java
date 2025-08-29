package com.example.echo_api.unit.service.post;

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

import com.example.echo_api.exception.custom.conflict.AlreadyLikedException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.repository.PostLikeRepository;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.interaction.PostInteractionService;
import com.example.echo_api.service.post.interaction.PostInteractionServiceImpl;
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
        post = new Post(UUID.randomUUID(), "Test post.");
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
    void like_ThrowsResourceNotFound_WhenPostByIdDoesNotExist() {
        // arrange
        UUID id = post.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postInteractionService.like(id));
        verify(postRepository).findById(id);
        verify(likeRepository, never()).existsByPostIdAndAuthorId(id, authenticatedUserId);
    }

    @Test
    void like_ThrowsAlreadyLiked_WhenPostByIdAlreadyLikedByYou() {
        // arrange
        UUID id = post.getId();

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(likeRepository.existsByPostIdAndAuthorId(id, authenticatedUserId)).thenReturn(true);

        // act & assert
        assertThrows(AlreadyLikedException.class, () -> postInteractionService.like(id));
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
