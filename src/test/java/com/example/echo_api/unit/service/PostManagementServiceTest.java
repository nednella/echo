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

import com.example.echo_api.exception.custom.badrequest.InvalidParentIdException;
import com.example.echo_api.exception.custom.forbidden.ResourceOwnershipException;
import com.example.echo_api.exception.custom.notfound.ResourceNotFoundException;
import com.example.echo_api.persistence.dto.request.post.CreatePostDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.management.PostManagementService;
import com.example.echo_api.service.post.management.PostManagementServiceImpl;
import com.example.echo_api.service.session.SessionService;

/**
 * Unit test class for {@link PostManagementService}.
 */
@ExtendWith(MockitoExtension.class)
class PostManagementServiceTest {

    @InjectMocks
    private PostManagementServiceImpl postManagementService;

    @Mock
    private SessionService sessionService;

    @Mock
    private PostRepository postRepository;

    private static Account authenticatedUser;
    private static CreatePostDTO request;
    private static Post post1;
    private static Post post2;

    @BeforeAll
    static void setup() {
        authenticatedUser = new Account(UUID.randomUUID(), "username", "password");
        request = new CreatePostDTO(UUID.randomUUID(), "Test post.");
        post1 = new Post(authenticatedUser.getId(), "Test post."); // post belonging to authenticatedUser
        post2 = new Post(UUID.randomUUID(), "Test post."); // post belonging to another user
    }

    @Test
    void PostManagementService_Create_ReturnVoid() {
        // arrange
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.existsById(request.parentId())).thenReturn(true);

        // act & assert
        assertDoesNotThrow(() -> postManagementService.create(request));
        verify(postRepository, times(1)).existsById(request.parentId());
    }

    @Test
    void PostManagementService_Create_ThrowInvalidParentIdException() {
        // arrange
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.existsById(request.parentId())).thenReturn(false);

        // act & assert
        assertThrows(InvalidParentIdException.class, () -> postManagementService.create(request));
        verify(postRepository, times(1)).existsById(request.parentId());
    }

    @Test
    void PostManagementService_Delete_ReturnVoid() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findById(id)).thenReturn(Optional.of(post1));

        // act & assert
        assertDoesNotThrow(() -> postManagementService.delete(id));
        verify(postRepository, times(1)).findById(id);
    }

    @Test
    void PostManagementService_Delete_ThrowResourceNotFoundException() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // act & assert
        assertThrows(ResourceNotFoundException.class, () -> postManagementService.delete(id));
        verify(postRepository, times(1)).findById(id);
    }

    @Test
    void PostManagementService_Delete_ThrowResourceOwnershipException() {
        // arrange
        UUID id = UUID.randomUUID();

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findById(id)).thenReturn(Optional.of(post2));

        // act & assert
        assertThrows(ResourceOwnershipException.class, () -> postManagementService.delete(id));
        verify(postRepository, times(1)).findById(id);
    }

}
