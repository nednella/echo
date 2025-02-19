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

import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.FollowRepository;
import com.example.echo_api.service.metrics.profile.ProfileMetricsService;
import com.example.echo_api.service.metrics.profile.ProfileMetricsServiceImpl;

/**
 * Unit test class for {@link ProfileMetricsService}.
 */

@ExtendWith(MockitoExtension.class)
class ProfileMetricsServiceTest {

    @Mock
    private FollowRepository followRepository;

    @InjectMocks
    private ProfileMetricsServiceImpl profileMetricsService;

    static Profile profile;

    @BeforeAll
    static void setup() throws Exception {
        Account account = new Account("test", "test");
        profile = new Profile(account);

        // set id with reflection
        Field idField = Profile.class.getDeclaredField("profileId");
        idField.setAccessible(true);
        idField.set(profile, UUID.randomUUID());
    }

    @Test
    void ProfileMetricsService_GetMetrics_ReturnMetricsDTO() {
        // arrange
        MetricsDTO expected = new MetricsDTO(0, 0, 0, 0);

        when(followRepository.countFollowers(profile.getId())).thenReturn(0);
        when(followRepository.countFollowing(profile.getId())).thenReturn(0);

        // act
        MetricsDTO actual = profileMetricsService.getMetrics(profile);

        // assert
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

}
