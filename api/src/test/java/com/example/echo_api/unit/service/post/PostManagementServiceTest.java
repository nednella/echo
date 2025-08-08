package com.example.echo_api.unit.service.post;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.exception.custom.badrequest.InvalidParentIdException;
import com.example.echo_api.exception.custom.forbidden.ResourceOwnershipException;
import com.example.echo_api.persistence.dto.request.post.CreatePostDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.model.post.entity.PostEntity;
import com.example.echo_api.persistence.model.post.entity.PostEntityType;
import com.example.echo_api.persistence.repository.PostEntityRepository;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.auth.session.SessionService;
import com.example.echo_api.service.post.management.PostManagementService;
import com.example.echo_api.service.post.management.PostManagementServiceImpl;

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

    @Mock
    private PostEntityRepository postEntityRepository;

    @Captor
    private ArgumentCaptor<List<PostEntity>> postEntityCaptor;

    private static Account authenticatedUser;

    @BeforeAll
    static void setup() {
        authenticatedUser = new Account(UUID.randomUUID(), "username", "password");
    }

    @Test
    void PostManagementService_Create_SavesNoEntities() {
        // arrange
        var request = new CreatePostDTO(
            UUID.randomUUID(),
            "Test post.");

        var post = new Post(
            UUID.randomUUID(),
            request.parentId(),
            authenticatedUser.getId(),
            request.text());

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.existsById(request.parentId())).thenReturn(true);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // act & assert
        assertDoesNotThrow(() -> postManagementService.create(request));
        verify(postRepository, times(1)).existsById(request.parentId());
        verify(postEntityRepository, times(1)).saveAll(List.of()); // empty list of entities
    }

    @Test
    void PostManagementService_Create_SavesMultipleEntities() {
        // arrange
        var request = new CreatePostDTO(
            UUID.randomUUID(),
            "Test post with a @Valid_Mention and a #ValidHashtag!");

        var post = new Post(
            UUID.randomUUID(),
            request.parentId(),
            authenticatedUser.getId(),
            request.text());

        List<PostEntity> entities = List.of(
            new PostEntity(post.getId(), PostEntityType.MENTION, 17, 31, "Valid_Mention"),
            new PostEntity(post.getId(), PostEntityType.HASHTAG, 38, 51, "ValidHashtag"));

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.existsById(request.parentId())).thenReturn(true);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // act & assert
        assertDoesNotThrow(() -> postManagementService.create(request));
        verify(postRepository, times(1)).existsById(request.parentId());
        verify(postEntityRepository, times(1)).saveAll(postEntityCaptor.capture());

        // validate captured postEntityRepository.saveAll argument
        List<PostEntity> captured = postEntityCaptor.getValue();
        assertEquals(2, captured.size());
        assertEquals(entities, captured);
    }

    @Test
    void PostManagementService_Create_ThrowInvalidParentIdException() {
        var request = new CreatePostDTO(
            UUID.randomUUID(),
            "Test post.");

        // arrange
        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.existsById(request.parentId())).thenReturn(false);

        // act & assert
        assertThrows(InvalidParentIdException.class, () -> postManagementService.create(request));
        verify(postRepository, times(1)).existsById(request.parentId());
        verify(postEntityRepository, never()).saveAll(anyList());
    }

    @Test
    void PostManagementService_Create_ThrowIllegalArgumentException() {
        // arrange
        var request = new CreatePostDTO(
            UUID.randomUUID(),
            null);

        var post = new Post(
            UUID.randomUUID(),
            request.parentId(),
            authenticatedUser.getId(),
            request.text());

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.existsById(request.parentId())).thenReturn(true);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // act & assert
        assertThrows(IllegalArgumentException.class, () -> postManagementService.create(request));
        verify(postRepository, times(1)).existsById(request.parentId());
        verify(postEntityRepository, never()).saveAll(anyList());
    }

    @Test
    void PostManagementService_Delete_ReturnVoid() {
        // arrange
        var id = UUID.randomUUID();
        var post = new Post(authenticatedUser.getId(), "Test post."); // post belonging to authenticatedUser

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findById(id)).thenReturn(Optional.of(post));

        // act & assert
        assertDoesNotThrow(() -> postManagementService.delete(id));
        verify(postRepository, times(1)).findById(id);
    }

    @Test
    void PostManagementService_Delete_ThrowResourceOwnershipException() {
        // arrange
        var id = UUID.randomUUID();
        var post = new Post(UUID.randomUUID(), "Test post."); // post belonging to another user

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.findById(id)).thenReturn(Optional.of(post));

        // act & assert
        assertThrows(ResourceOwnershipException.class, () -> postManagementService.delete(id));
        verify(postRepository, times(1)).findById(id);
    }

}
