package com.example.echo_api.persistence.mapper;

import static lombok.AccessLevel.PRIVATE;

import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.MetricsDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.RelationshipDTO;
import com.example.echo_api.persistence.model.profile.Profile;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public class ProfileMapper {

    public static ProfileDTO toDTO(Profile profile, MetricsDTO metrics, RelationshipDTO relationship) {
        return new ProfileDTO(
            profile.getUsername(),
            profile.getName(),
            profile.getBio(),
            profile.getLocation(),
            profile.getAvatarUrl(),
            profile.getBannerUrl(),
            profile.getCreatedAt(),
            metrics,
            relationship);
    }

    public static Profile updateProfile(UpdateProfileDTO request, Profile profile) {
        if (request.name() != null) {
            profile.setName(request.name());
        }
        if (request.bio() != null) {
            profile.setBio(request.bio());
        }
        if (request.location() != null) {
            profile.setLocation(request.location());
        }

        return profile;
    }

}
