package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.echo_api.exception.custom.account.IdNotFoundException;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.mapper.MetricsMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Metrics;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.MetricsRepository;
import com.example.echo_api.service.metrics.MetricsServiceImpl;

/**
 * Unit test class for {@link MetricsService}.
 */
@ExtendWith(MockitoExtension.class)
class MetricsServiceTest {

    @Mock
    private MetricsRepository metricsRepository;

    @InjectMocks
    private MetricsServiceImpl metricsService;

    private static Profile profile;
    private static Metrics metrics;

    @BeforeAll
    static void setup() {
        Account account = new Account("test", "test");
        profile = new Profile(account);
        metrics = new Metrics(profile);
    }

    @Test
    void MetricsService_GetMetrics_ReturnMetrics() {
        // arrange
        Metrics uniqueMetrics = new Metrics(profile);
        MetricsDTO expected = MetricsMapper.toDTO(uniqueMetrics);

        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.of(uniqueMetrics));

        // act
        MetricsDTO actual = metricsService.getMetrics(profile);

        // assert
        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_GetMetrics_ThrowIdNotFound() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> metricsService.getMetrics(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());

    }

    // following

    @Test
    void MetricsService_IncrementFollowing_ReturnVoid() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.of(metrics));

        // act & assert
        assertDoesNotThrow(() -> metricsService.incrementFollowing(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_IncrementFollowing_ThrowIdNotFound() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> metricsService.incrementFollowing(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_DecrementFollowing_ReturnVoid() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.of(metrics));

        // act & assert
        assertDoesNotThrow(() -> metricsService.decrementFollowing(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_DecrementFollowing_ThrowIdNotFound() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> metricsService.decrementFollowing(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    // followers

    @Test
    void MetricsService_IncrementFollowers_ReturnVoid() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.of(metrics));

        // act & assert
        assertDoesNotThrow(() -> metricsService.incrementFollowers(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_IncrementFollowers_ThrowIdNotFound() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> metricsService.incrementFollowers(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_DecrementFollowers_ReturnVoid() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.of(metrics));

        // act & assert
        assertDoesNotThrow(() -> metricsService.decrementFollowers(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_DecrementFollowers_ThrowIdNotFound() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> metricsService.decrementFollowers(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    // posts

    @Test
    void MetricsService_IncrementPosts_ReturnVoid() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.of(metrics));

        // act & assert
        assertDoesNotThrow(() -> metricsService.incrementPosts(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_IncrementPosts_ThrowIdNotFound() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> metricsService.incrementPosts(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_DecrementPosts_ReturnVoid() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.of(metrics));

        // act & assert
        assertDoesNotThrow(() -> metricsService.decrementPosts(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_DecrementPosts_ThrowIdNotFound() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> metricsService.decrementPosts(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    // media

    @Test
    void MetricsService_IncrementMedia_ReturnVoid() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.of(metrics));

        // act & assert
        assertDoesNotThrow(() -> metricsService.incrementMedia(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_IncrementMedia_ThrowIdNotFound() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> metricsService.incrementMedia(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_DecrementMedia_ReturnVoid() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.of(metrics));

        // act & assert
        assertDoesNotThrow(() -> metricsService.decrementMedia(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

    @Test
    void MetricsService_DecrementMedia_ThrowIdNotFound() {
        // arrange
        when(metricsRepository.findById(profile.getProfileId())).thenReturn(Optional.empty());

        // act & assert
        assertThrows(IdNotFoundException.class, () -> metricsService.decrementMedia(profile));
        verify(metricsRepository, times(1)).findById(profile.getProfileId());
    }

}
