package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.exception.custom.relationship.AlreadyFollowingException;
import com.example.echo_api.exception.custom.relationship.NotFollowingException;
import com.example.echo_api.exception.custom.relationship.SelfActionException;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.follow.Follow;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.FollowRepository;
import com.example.echo_api.service.relationship.follow.FollowService;
import com.example.echo_api.service.relationship.follow.FollowServiceImpl;

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
    static void setup() throws Exception {
        Account sourceAcc = new Account("source", "test");
        source = new Profile(sourceAcc);

        Account targetAcc = new Account("target", "test");
        target = new Profile(targetAcc);

        // set ids with reflection
        Field idField = Profile.class.getDeclaredField("profileId");
        idField.setAccessible(true);
        idField.set(source, UUID.randomUUID());
        idField.set(target, UUID.randomUUID());
    }

    /**
     * Test ensures that {@link FollowServiceImpl#follow(Profile, Profile)} does not
     * throw any exceptions when called.
     */
    @Test
    void FollowService_Follow_ReturnVoid() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(source.getProfileId(), target.getProfileId()))
            .thenReturn(false);

        // act & assert
        assertDoesNotThrow(() -> followService.follow(source, target));
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(source.getProfileId(),
            target.getProfileId());
        verify(followRepository, times(1)).save(any(Follow.class));
    }

    /**
     * Test ensures that {@link FollowServiceImpl#follow(Profile, Profile)} throws
     * {@link SelfActionException} when called with two identical profiles.
     */
    @Test
    void FollowService_Follow_ThrowSelfActionException() {
        // act & assert
        assertThrows(SelfActionException.class, () -> followService.follow(source, source));
        verify(followRepository, times(0)).existsByFollowerIdAndFollowingId(source.getProfileId(),
            target.getProfileId());
        verify(followRepository, times(0)).save(any(Follow.class));
    }

    /**
     * Test ensures that {@link FollowServiceImpl#follow(Profile, Profile)} throws
     * {@link AlreadyFollowingException} when called with a source profile that
     * already follows the target profile.
     */
    @Test
    void FollowService_Follow_ThrowAlreadyFollowingException() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(source.getProfileId(), target.getProfileId()))
            .thenReturn(true);

        // act & assert
        assertThrows(AlreadyFollowingException.class, () -> followService.follow(source, target));
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(source.getProfileId(),
            target.getProfileId());
        verify(followRepository, times(0)).save(any(Follow.class));
    }

    /**
     * Test ensures that {@link FollowServiceImpl#unfollow(Profile, Profile)} does
     * not throw any exceptions when called.
     */
    @Test
    void FollowService_Unfollow_ReturnVoid() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(source.getProfileId(), target.getProfileId()))
            .thenReturn(true);

        // act & assert
        assertDoesNotThrow(() -> followService.unfollow(source, target));
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(source.getProfileId(),
            target.getProfileId());
        verify(followRepository, times(1)).delete(any(Follow.class));
    }

    /**
     * Test ensures that {@link FollowServiceImpl#unfollow(Profile, Profile)} throws
     * {@link SelfActionException} when called with two identical profiles.
     */
    @Test
    void FollowService_Unfollow_ThrowSelfActionException() {
        // act & assert
        assertThrows(SelfActionException.class, () -> followService.unfollow(source, source));
        verify(followRepository, times(0)).existsByFollowerIdAndFollowingId(source.getProfileId(),
            target.getProfileId());
        verify(followRepository, times(0)).delete(any(Follow.class));
    }

    /**
     * Test ensures that {@link FollowServiceImpl#follow(Profile, Profile)} throws
     * {@link NotFollowingException} when called with a source profile that does not
     * follow the target profile.
     */
    @Test
    void FollowService_Unfollow_ThrowNotFollowingException() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(source.getProfileId(), target.getProfileId()))
            .thenReturn(false);

        // act & assert
        assertThrows(NotFollowingException.class, () -> followService.unfollow(source, target));
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(source.getProfileId(),
            target.getProfileId());
        verify(followRepository, times(0)).delete(any(Follow.class));
    }

    /**
     * Test ensures that {@link FollowServiceImpl#isFollowing(Profile, Profile)}
     * returns true when the source profile is already following the target profile.
     */
    @Test
    void FollowService_IsFollowing_ReturnTrue() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(source.getProfileId(), target.getProfileId()))
            .thenReturn(true);

        // act
        boolean isFollowing = followService.isFollowing(source, target);

        // assert
        assertTrue(isFollowing);
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(source.getProfileId(),
            target.getProfileId());
    }

    /**
     * Test ensures that {@link FollowServiceImpl#isFollowing(Profile, Profile)}
     * returns false when the source profile is not following the target profile.
     */
    @Test
    void FollowService_IsFollowing_ReturnFalse() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(source.getProfileId(), target.getProfileId()))
            .thenReturn(false);

        // act
        boolean isFollowing = followService.isFollowing(source, target);

        // assert
        assertFalse(isFollowing);
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(source.getProfileId(),
            target.getProfileId());
    }

    /**
     * Test ensures that {@link FollowServiceImpl#isFollowedBy(Profile, Profile)}
     * returns true when the source profile is already followed by the target
     * profile.
     */
    @Test
    void FollowService_IsFollowedBy_ReturnTrue() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(target.getProfileId(), source.getProfileId()))
            .thenReturn(true);

        // act
        boolean isFollowedBy = followService.isFollowedBy(source, target);

        // assert
        assertTrue(isFollowedBy);
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(target.getProfileId(),
            source.getProfileId());
    }

    /**
     * Test ensures that {@link FollowServiceImpl#isFollowedBy(Profile, Profile)}
     * returns true when the source profile is not followed by the target profile.
     */
    @Test
    void FollowService_IsFollowedBy_ReturnFalse() {
        // arrange
        when(followRepository.existsByFollowerIdAndFollowingId(target.getProfileId(), source.getProfileId()))
            .thenReturn(false);

        // act
        boolean isFollowedBy = followService.isFollowedBy(source, target);

        // assert
        assertFalse(isFollowedBy);
        verify(followRepository, times(1)).existsByFollowerIdAndFollowingId(target.getProfileId(),
            source.getProfileId());
    }

}
