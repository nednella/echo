package com.example.echo_api.service.metrics;

import com.example.echo_api.exception.custom.account.IdNotFoundException;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.model.profile.Metrics;
import com.example.echo_api.persistence.model.profile.Profile;

public interface MetricsService {

    /**
     * Fetches profile {@link Metrics} for the supplied profile.
     * 
     * @param profile The {@link Profile} of the metrics to fetch.
     * @return profile {@link Metrics}.
     * @throws IdNotFoundException If the {@code profile_id} is not found.
     */
    public MetricsDTO getMetrics(Profile profile) throws IdNotFoundException;

    /**
     * Updates profile {@link Metrics} by incrementing {@code followingCount}.
     * 
     * @param profile The {@link Profile} of the metrics to update.
     * @throws IdNotFoundException If the {@code profile_id} is not found.
     */
    public void incrementFollowing(Profile profile) throws IdNotFoundException;

    /**
     * Updates profile {@link Metrics} by incrementing {@code followerCount}.
     * 
     * @param profile The {@link Profile} of the metrics to update.
     * @throws IdNotFoundException If the {@code profile_id} is not found.
     */
    public void incrementFollowers(Profile profile) throws IdNotFoundException;

    /**
     * Updates profile {@link Metrics} by incrementing {@code postCount}.
     * 
     * @param profile The {@link Profile} of the metrics to update.
     * @throws IdNotFoundException If the {@code profile_id} is not found.
     */
    public void incrementPosts(Profile profile) throws IdNotFoundException;

    /**
     * Updates profile {@link Metrics} by incrementing {@code mediaCount}.
     * 
     * @param profile The {@link Profile} of the metrics to update.
     * @throws IdNotFoundException If the {@code profile_id} is not found.
     */
    public void incrementMedia(Profile profile) throws IdNotFoundException;

    /**
     * Updates profile {@link Metrics} by decrementing {@code followingCount}.
     * 
     * @param profile The {@link Profile} of the metrics to update.
     * @throws IdNotFoundException If the {@code profile_id} is not found.
     */
    public void decrementFollowing(Profile profile) throws IdNotFoundException;

    /**
     * Updates profile {@link Metrics} by decrementing {@code followerCount}.
     * 
     * @param profile The {@link Profile} of the metrics to update.
     * @throws IdNotFoundException If the {@code profile_id} is not found.
     */
    public void decrementFollowers(Profile profile) throws IdNotFoundException;

    /**
     * Updates profile {@link Metrics} by decrementing {@code postCount}.
     * 
     * @param profile The {@link Profile} of the metrics to update.
     * @throws IdNotFoundException If the {@code profile_id} is not found.
     */
    public void decrementPosts(Profile profile) throws IdNotFoundException;

    /**
     * Updates profile {@link Metrics} by decrementing {@code mediaCount}.
     * 
     * @param profile The {@link Profile} of the metrics to update.
     * @throws IdNotFoundException If the {@code profile_id} is not found.
     */
    public void decrementMedia(Profile profile) throws IdNotFoundException;

}
