package com.example.echo_api.unit.service;

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
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.repository.PostLikeRepository;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.interaction.PostInteractionService;
import com.example.echo_api.service.post.interaction.PostInteractionServiceImpl;
import com.example.echo_api.service.session.SessionService;

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

    private static Account authenticatedUser;
    private static Post post;

    @BeforeAll
    static void setup() {
        authenticatedUser = new Account("user", "password");
        post = new Post(UUID.randomUUID(), "Test post.");
    }

    @Test
    void PostInteractionService_Like_ReturnVoid() {
        // arrange
        UUID id = post.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(likeRepository.existsByPostIdAndAuthorId(id, authenticatedUser.getId())).thenReturn(false);

        // act & assert
        assertDoesNotThrow(() -> postInteractionService.like(id));
        verify(postRepository, times(1)).findById(id);
        verify(likeRepository, times(1)).existsByPostIdAndAuthorId(id, authenticatedUser.getId());
    }

    @Test
    void PostInteractionService_Like_ThrowResourceNotFoundException() {
        // arrange
        UUID id = post.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postInteractionService.like(id));
        verify(postRepository, times(1)).findById(id);
        verify(likeRepository, times(0)).existsByPostIdAndAuthorId(id, authenticatedUser.getId());
    }

    @Test
    void PostInteractionService_Like_ThrowAlreadyLikedException() {
        // arrange
        UUID id = post.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findById(id)).thenReturn(Optional.of(post));
        when(likeRepository.existsByPostIdAndAuthorId(id, authenticatedUser.getId())).thenReturn(true);

        // act & assert
        assertThrows(AlreadyLikedException.class, () -> postInteractionService.like(id));
        verify(postRepository, times(1)).findById(id);
        verify(likeRepository, times(1)).existsByPostIdAndAuthorId(id, authenticatedUser.getId());
    }

    @Test
    void PostInteractionService_Unlike_ReturnVoid() {
        // arrange
        UUID id = post.getId();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);

        // act & assert
        assertDoesNotThrow(() -> postInteractionService.unlike(id));
    }

}
