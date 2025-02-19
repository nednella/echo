package com.example.echo_api.service.metrics.profile;

import org.springframework.stereotype.Service;

import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.FollowRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing RU operations of profile {@link Metrics}.
 * 
 * @see MetricsRepository
 */
@Service
@RequiredArgsConstructor
public class ProfileMetricsServiceImpl implements ProfileMetricsService {

    private final FollowRepository followRepository;

    @Override
    public MetricsDTO getMetrics(Profile profile) {
        int followers = followRepository.countFollowers(profile.getId());
        int following = followRepository.countFollowing(profile.getId());
        int posts = 0; // no posts implementation available
        int media = 0; // no media implementation available
        return new MetricsDTO(followers, following, posts, media);
    }

}
