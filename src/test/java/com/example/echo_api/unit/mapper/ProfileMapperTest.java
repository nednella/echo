package com.example.echo_api.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.mapper.MetricsMapper;
import com.example.echo_api.persistence.mapper.ProfileMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Metrics;
import com.example.echo_api.persistence.model.profile.Profile;

class ProfileMapperTest {

    @Test
    void ProfileMapper_toDTO() {
        Account account = new Account("test", "test");
        Profile profile = new Profile(account);
        Metrics metrics = new Metrics(profile);
        MetricsDTO metricsDto = MetricsMapper.toDTO(metrics);
        RelationshipDTO relationshipDto = new RelationshipDTO(false, false, false, false);
        ProfileDTO response = ProfileMapper.toDTO(profile, metricsDto, relationshipDto);

        assertNotNull(response);
        assertEquals(account.getUsername(), response.username());
        assertEquals(profile.getName(), response.name());
        assertEquals(profile.getBio(), response.bio());
        assertEquals(profile.getLocation(), response.location());
        assertEquals(profile.getAvatarUrl(), response.avatarUrl());
        assertEquals(profile.getBannerUrl(), response.bannerUrl());
        assertEquals(metricsDto.followingCount(), response.metrics().followingCount());
        assertEquals(metricsDto.followerCount(), response.metrics().followerCount());
        assertEquals(metricsDto.postCount(), response.metrics().postCount());
        assertEquals(metricsDto.mediaCount(), response.metrics().mediaCount());
        assertEquals(relationshipDto.following(), response.relationship().following());
        assertEquals(relationshipDto.followedBy(), response.relationship().followedBy());
        assertEquals(relationshipDto.blocking(), response.relationship().blocking());
        assertEquals(relationshipDto.blockedBy(), response.relationship().blockedBy());
    }

    @Test
    void ProfileMapper_updateProfile() {
        Account account = new Account("test", "test");
        Profile profile = new Profile(account);
        UpdateProfileDTO request = new UpdateProfileDTO("name", "bio", "location");

        Profile updated = ProfileMapper.updateProfile(request, profile);

        assertNotNull(updated);
        assertEquals(request.name(), updated.getName());
        assertEquals(request.location(), updated.getLocation());
        assertEquals(request.bio(), updated.getBio());
    }

}
