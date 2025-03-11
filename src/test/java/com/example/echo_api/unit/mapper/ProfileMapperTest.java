package com.example.echo_api.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.mapper.ProfileMapper;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.model.profile.Profile;

class ProfileMapperTest {

    @Test
    void ProfileMapper_toDTO() {
        Account account = new Account("test", "test");
        Profile profile = new Profile(account);
        MetricsDTO metrics = new MetricsDTO(0, 0, 0, 0);
        RelationshipDTO relationship = new RelationshipDTO(false, false, false, false);
        ProfileDTO response = ProfileMapper.toDTO(profile, metrics, relationship);

        assertNotNull(response);
        assertEquals(account.getUsername(), response.username());
        assertEquals(profile.getName(), response.name());
        assertEquals(profile.getBio(), response.bio());
        assertEquals(profile.getLocation(), response.location());
        assertEquals(profile.getAvatar() != null ? profile.getAvatar().getTransformedUrl() : null,
            response.avatarUrl());
        assertEquals(profile.getBanner() != null ? profile.getBanner().getTransformedUrl() : null,
            response.bannerUrl());
        assertEquals(metrics.followingCount(), response.metrics().followingCount());
        assertEquals(metrics.followerCount(), response.metrics().followerCount());
        assertEquals(metrics.postCount(), response.metrics().postCount());
        assertEquals(metrics.mediaCount(), response.metrics().mediaCount());
        assertEquals(relationship.following(), response.relationship().following());
        assertEquals(relationship.followedBy(), response.relationship().followedBy());
        assertEquals(relationship.blocking(), response.relationship().blocking());
        assertEquals(relationship.blockedBy(), response.relationship().blockedBy());
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
