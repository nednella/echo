package com.example.echo_api.modules.profile.service;

import org.springframework.stereotype.Service;

import com.example.echo_api.modules.profile.dto.UpdateProfileDTO;
import com.example.echo_api.modules.profile.entity.Profile;
import com.example.echo_api.modules.profile.repository.ProfileRepository;
import com.example.echo_api.shared.service.SessionService;

/**
 * Service implementation for managing mutation operations for {@link Profile}
 * information related to the authenticated user.
 */
@Service
class ProfileManagementServiceImpl extends BaseProfileService implements ProfileManagementService {

    // @formatter:off
    public ProfileManagementServiceImpl(
        SessionService sessionService,
        ProfileRepository profileRepository
    ) {
        super(sessionService, profileRepository);
    }
    // @formatter:on

    @Override
    public void updateProfile(UpdateProfileDTO request) {
        Profile me = getAuthenticatedUserProfile();
        me.setName(request.name());
        me.setBio(request.bio());
        me.setLocation(request.location());
        profileRepository.save(me);
    }

}
