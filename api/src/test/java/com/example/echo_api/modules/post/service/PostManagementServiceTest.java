package com.example.echo_api.modules.post.service;

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
import com.example.echo_api.modules.post.dto.CreatePostDTO;
import com.example.echo_api.modules.post.entity.Post;
import com.example.echo_api.modules.post.entity.PostEntity;
import com.example.echo_api.modules.post.entity.PostEntityType;
import com.example.echo_api.modules.post.repository.PostEntityRepository;
import com.example.echo_api.modules.post.repository.PostRepository;
import com.example.echo_api.shared.service.SessionService;

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

    private static UUID authenticatedUserId;

    @BeforeAll
    static void setup() {
        authenticatedUserId = UUID.randomUUID();
    }

    @Test
    void create_SavesNoEntities_WhenPostTextContainsNoEntities() {
        // arrange
        var request = new CreatePostDTO(
            UUID.randomUUID(),
            "Test post.");

        var post = new Post(
            UUID.randomUUID(),
            request.parentId(),
            authenticatedUserId,
            request.text());

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.existsById(request.parentId())).thenReturn(true);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // act & assert
        assertDoesNotThrow(() -> postManagementService.create(request));
        verify(postRepository).existsById(request.parentId());
        verify(postEntityRepository).saveAll(List.of()); // empty list of entities
    }

    @Test
    void create_SavesMultipleEntities_WhenPostTextContainsMultipleEntities() {
        // arrange
        var request = new CreatePostDTO(
            UUID.randomUUID(),
            "Test post with a @Valid_Mention and a #ValidHashtag!");

        var post = new Post(
            UUID.randomUUID(),
            request.parentId(),
            authenticatedUserId,
            request.text());

        List<PostEntity> entities = List.of(
            new PostEntity(post.getId(), PostEntityType.MENTION, 17, 31, "Valid_Mention"),
            new PostEntity(post.getId(), PostEntityType.HASHTAG, 38, 51, "ValidHashtag"));

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.existsById(request.parentId())).thenReturn(true);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // act & assert
        assertDoesNotThrow(() -> postManagementService.create(request));
        verify(postRepository).existsById(request.parentId());
        verify(postEntityRepository).saveAll(postEntityCaptor.capture());

        // validate captured postEntityRepository.saveAll argument
        List<PostEntity> captured = postEntityCaptor.getValue();
        assertEquals(2, captured.size());
        assertEquals(entities, captured);
    }

    @Test
    void create_ThrowsInvalidParentId_WhenPostByParentIdDoesNotExist() {
        var request = new CreatePostDTO(
            UUID.randomUUID(),
            "Test post.");

        // arrange
        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.existsById(request.parentId())).thenReturn(false);

        // act & assert
        assertThrows(InvalidParentIdException.class, () -> postManagementService.create(request));
        verify(postRepository).existsById(request.parentId());
        verify(postEntityRepository, never()).saveAll(anyList());
    }

    @Test
    void create_ThrowIllegalArgument_WhenPostTextIsNull() {
        // arrange
        var request = new CreatePostDTO(
            UUID.randomUUID(),
            null);

        var post = new Post(
            UUID.randomUUID(),
            request.parentId(),
            authenticatedUserId,
            request.text());

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.existsById(request.parentId())).thenReturn(true);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // act & assert
        assertThrows(IllegalArgumentException.class, () -> postManagementService.create(request));
        verify(postRepository).existsById(request.parentId());
        verify(postEntityRepository, never()).saveAll(anyList());
    }

    @Test
    void delete_ReturnVoid_WhenPostSuccessfullyDeleted() {
        // arrange
        var id = UUID.randomUUID();
        var post = new Post(authenticatedUserId, "Test post."); // post belonging to authenticatedUser

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findById(id)).thenReturn(Optional.of(post));

        // act & assert
        assertDoesNotThrow(() -> postManagementService.delete(id));
        verify(postRepository).findById(id);
    }

    @Test
    void delete_ThrowResourceOwnership_WhenPostByIdNotOwnedByYou() {
        // arrange
        var id = UUID.randomUUID();
        var post = new Post(UUID.randomUUID(), "Test post."); // post belonging to another user

        when(sessionService.getAuthenticatedUserId()).thenReturn(authenticatedUserId);
        when(postRepository.findById(id)).thenReturn(Optional.of(post));

        // act & assert
        assertThrows(ResourceOwnershipException.class, () -> postManagementService.delete(id));
        verify(postRepository).findById(id);
    }

}
