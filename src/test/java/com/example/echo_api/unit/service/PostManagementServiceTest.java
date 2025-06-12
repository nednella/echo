package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
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
import com.example.echo_api.persistence.dto.request.post.CreatePostDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.post.Post;
import com.example.echo_api.persistence.model.post.entity.PostEntity;
import com.example.echo_api.persistence.model.post.entity.PostHashtag;
import com.example.echo_api.persistence.model.post.entity.PostMention;
import com.example.echo_api.persistence.repository.PostHashtagRepository;
import com.example.echo_api.persistence.repository.PostMentionRepository;
import com.example.echo_api.persistence.repository.PostRepository;
import com.example.echo_api.service.post.management.PostManagementService;
import com.example.echo_api.service.post.management.PostManagementServiceImpl;
import com.example.echo_api.service.post.util.PostParsingService;
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

    @Mock
    private PostParsingService postParsingService;

    @Mock
    private PostHashtagRepository postHashtagRepository;

    @Mock
    private PostMentionRepository postMentionRepository;

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
        when(postParsingService.parse(post.getId(), post.getText()))
            .thenReturn(Map.of("hashtags", List.of(), "mentions", List.of()));

        // act & assert
        assertDoesNotThrow(() -> postManagementService.create(request));
        verify(postRepository, times(1)).existsById(request.parentId());
        verify(postParsingService, times(1)).parse(post.getId(), post.getText());
        verify(postHashtagRepository, times(1)).saveAll(List.of()); // empty list of hashtags
        verify(postMentionRepository, times(1)).saveAll(List.of()); // empty list of mentions
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

        List<PostEntity> hashtags = List.of(new PostHashtag(post.getId(), 38, 51, "#ValidHashtag"));
        List<PostEntity> mentions = List.of(new PostMention(post.getId(), 17, 31, "@Valid_Mention"));

        when(sessionService.getAuthenticatedUser()).thenReturn(authenticatedUser);
        when(postRepository.existsById(request.parentId())).thenReturn(true);
        when(postRepository.save(any(Post.class))).thenReturn(post);
        when(postParsingService.parse(post.getId(), post.getText()))
            .thenReturn(Map.of("hashtags", hashtags, "mentions", mentions));

        // act & assert
        assertDoesNotThrow(() -> postManagementService.create(request));
        verify(postRepository, times(1)).existsById(request.parentId());
        verify(postParsingService, times(1)).parse(post.getId(), post.getText());
        verify(postHashtagRepository, times(1)).saveAll(hashtags); // non-empty list of hashtags
        verify(postMentionRepository, times(1)).saveAll(mentions); // non-empty list of mentions
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
        verify(postParsingService, never()).parse(any(UUID.class), any(String.class));
        verify(postHashtagRepository, never()).saveAll(anyList());
        verify(postMentionRepository, never()).saveAll(anyList());
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
        when(postParsingService.parse(post.getId(), post.getText())).thenThrow(new IllegalArgumentException());

        // act & assert
        assertThrows(IllegalArgumentException.class, () -> postManagementService.create(request));
        verify(postRepository, times(1)).existsById(request.parentId());
        verify(postParsingService, times(1)).parse(post.getId(), post.getText());
        verify(postHashtagRepository, never()).saveAll(anyList());
        verify(postMentionRepository, never()).saveAll(anyList());
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
