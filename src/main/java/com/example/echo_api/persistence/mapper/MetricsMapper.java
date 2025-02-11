package com.example.echo_api.persistence.mapper;

import static lombok.AccessLevel.PRIVATE;

import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.model.profile.Metrics;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class MetricsMapper {

    public static MetricsDTO toDTO(Metrics metrics) {
        return new MetricsDTO(
            metrics.getFollowingCount(),
            metrics.getFollowerCount(),
            metrics.getPostCount(),
            metrics.getMediaCount());
    }

}
