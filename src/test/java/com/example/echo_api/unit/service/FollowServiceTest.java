package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.exception.custom.conflict.AlreadyFollowingException;
import com.example.echo_api.exception.custom.conflict.NotFollowingException;
import com.example.echo_api.exception.custom.conflict.SelfActionException;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.FollowRepository;
import com.example.echo_api.service.follow.FollowService;
import com.example.echo_api.service.follow.FollowServiceImpl;

/**
 * Unit test class for {@link FollowService}.
 */
@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private FollowServiceImpl followService;

    private static Profile source;
    private static Profile target;

    @BeforeAll
    static void setup() {
        source = new Profile(UUID.randomUUID(), "source");
        target = new Profile(UUID.randomUUID(), "target");
    }

    /**
     * Test ensures that {@link FollowServiceImpl#follow(UUID, UUID)} does not throw
     * any exceptions when called.
     */
    @Test
    void FollowService_Follow_ReturnVoid() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(source.getId(), target.getId())).thenReturn(false);

        // act & assert
        assertDoesNotThrow(() -> followService.follow(source.getId(), target.getId()));
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(source.getId(), target.getId());
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    /**
     * Test ensures that {@link FollowServiceImpl#follow(UUID, UUID)} throws
     * {@link SelfActionException} when called with two identical profile ids.
     */
    @Test
    void FollowService_Follow_ThrowSelfActionException() {
        // act & assert
        assertThrows(SelfActionException.class, () -> followService.follow(source.getId(), source.getId()));
        verify(followRepository, times(0)).existsByFollowerIdAndFollowingId(source.getId(), source.getId());
        verify(followRepository, times(0)).save(any(Follow.class));
    }

    /**
     * Test ensures that {@link FollowServiceImpl#follow(UUID, UUID)} throws
     * {@link AlreadyFollowingException} when called with a source profile id that
     * already follows the target profile id.
     */
    @Test
    void FollowService_Follow_ThrowAlreadyFollowingException() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(source.getId(), target.getId())).thenReturn(true);

        // act & assert
        assertThrows(AlreadyFollowingException.class, () -> followService.follow(source.getId(), target.getId()));
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(source.getId(), target.getId());
        verify(followRepository, times(0)).save(any(Follow.class));
    }

    /**
     * Test ensures that {@link FollowServiceImpl#unfollow(UUID, UUID)} does not
     * throw any exceptions when called.
     */
    @Test
    void FollowService_Unfollow_ReturnVoid() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(source.getId(), target.getId())).thenReturn(true);

        // act & assert
        assertDoesNotThrow(() -> followService.unfollow(source.getId(), target.getId()));
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(source.getId(), target.getId());
        verify(followRepository, times(1)).delete(any(Follow.class));
    }

    /**
     * Test ensures that {@link FollowServiceImpl#unfollow(UUID, UUID)} throws
     * {@link SelfActionException} when called with two identical profile ids.
     */
    @Test
    void FollowService_Unfollow_ThrowSelfActionException() {
        // act & assert
        assertThrows(SelfActionException.class, () -> followService.unfollow(source.getId(), source.getId()));
        verify(followRepository, times(0)).existsByFollowerIdAndFollowingId(source.getId(), target.getId());
        verify(followRepository, times(0)).delete(any(Follow.class));
    }

    /**
     * Test ensures that {@link FollowServiceImpl#follow(UUID, UUID)} throws
     * {@link NotFollowingException} when called with a source profile that does not
     * follow the target profile.
     */
    @Test
    void FollowService_Unfollow_ThrowNotFollowingException() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(source.getId(), target.getId())).thenReturn(false);

        // act & assert
        assertThrows(NotFollowingException.class, () -> followService.unfollow(source.getId(), target.getId()));
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(source.getId(), target.getId());
        verify(followRepository, times(0)).delete(any(Follow.class));
    }

    /**
     * Test ensures that
     * {@link FollowServiceImpl#mutualUnfollowIfExists(UUID, UUID)} does not throw
     * any exceptions when called.
     */
    @Test
    void FollowService_MutualUnfollowIfExists_ReturnVoid() {
        // arrange
        doNothing().when(followRepository).deleteAnyFollowIfExists(source.getId(), target.getId());

        // act & assert
        assertDoesNotThrow(() -> followRepository.deleteAnyFollowIfExists(source.getId(), target.getId()));
        verify(followRepository, times(1)).deleteAnyFollowIfExists(source.getId(), target.getId());
    }

}
