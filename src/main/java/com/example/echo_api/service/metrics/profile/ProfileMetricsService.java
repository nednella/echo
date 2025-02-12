package com.example.echo_api.service.metrics.profile;

import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.model.profile.Profile;

public interface ProfileMetricsService {

    public MetricsDTO getMetrics(Profile profile);

}
