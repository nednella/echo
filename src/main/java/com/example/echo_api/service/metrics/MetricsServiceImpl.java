package com.example.echo_api.service.metrics;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.echo_api.exception.custom.account.IdNotFoundException;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.mapper.MetricsMapper;
import com.example.echo_api.persistence.model.profile.Metrics;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.MetricsRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing RU operations of profile {@link Metrics}.
 * 
 * @see MetricsRepository
 */
@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final MetricsRepository metricsRepository;

    @Override
    public MetricsDTO getMetrics(Profile profile) throws IdNotFoundException {
        Metrics metrics = findById(profile.getProfileId());
        return MetricsMapper.toDTO(metrics);
    }

    @Override
    public void incrementFollowing(Profile profile) throws IdNotFoundException {
        Metrics metrics = findById(profile.getProfileId());
        metrics.incrementFollowingCount();
        metricsRepository.save(metrics);
    }

    @Override
    public void incrementFollowers(Profile profile) throws IdNotFoundException {
        Metrics metrics = findById(profile.getProfileId());
        metrics.incrementFollowerCount();
        metricsRepository.save(metrics);
    }

    @Override
    public void incrementPosts(Profile profile) throws IdNotFoundException {
        Metrics metrics = findById(profile.getProfileId());
        metrics.incrementPostCount();
        metricsRepository.save(metrics);
    }

    @Override
    public void incrementMedia(Profile profile) throws IdNotFoundException {
        Metrics metrics = findById(profile.getProfileId());
        metrics.incrementMediaCount();
        metricsRepository.save(metrics);
    }

    @Override
    public void decrementFollowing(Profile profile) throws IdNotFoundException {
        Metrics metrics = findById(profile.getProfileId());
        metrics.decrementFollowingCount();
        metricsRepository.save(metrics);
    }

    @Override
    public void decrementFollowers(Profile profile) throws IdNotFoundException {
        Metrics metrics = findById(profile.getProfileId());
        metrics.decrementFollowerCount();
        metricsRepository.save(metrics);
    }

    @Override
    public void decrementPosts(Profile profile) throws IdNotFoundException {
        Metrics metrics = findById(profile.getProfileId());
        metrics.decrementPostCount();
        metricsRepository.save(metrics);
    }

    @Override
    public void decrementMedia(Profile profile) throws IdNotFoundException {
        Metrics metrics = findById(profile.getProfileId());
        metrics.decrementMediaCount();
        metricsRepository.save(metrics);
    }

    /**
     * Internal method for obtaining {@link Metrics} via {@code profile_id} from
     * {@link MetricsRepository}.
     * 
     * @param id The {@code profile_id} to search within the repository.
     * @return The found {@link Metrics}.
     */
    private Metrics findById(UUID id) throws IdNotFoundException {
        return metricsRepository.findById(id)
            .orElseThrow(IdNotFoundException::new);
    }

}
