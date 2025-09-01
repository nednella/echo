package com.example.echo_api.modules.profile.dto;

/**
 * Represents a standardised response format for profile metrics.
 *
 * @param followers the number of followers this profile has
 * @param following the number of profiles this profile is following
 * @param posts     the number of posts this profile has made
 * @param media     the number of media items this profile has uploaded
 */
public record ProfileMetricsDTO(
    int followers,
    int following,
    int posts
// int media // TODO: implement post media
) {}