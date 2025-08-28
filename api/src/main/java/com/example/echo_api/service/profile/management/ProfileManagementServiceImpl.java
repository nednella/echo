package com.example.echo_api.service.profile.management;

import org.springframework.stereotype.Service;

import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.profile.BaseProfileService;
import com.example.echo_api.service.session.SessionService;

/**
 * Service implementation for managing mutation operations for {@link Profile}
 * information related to the authenticated user.
 */
@Service
public class ProfileManagementServiceImpl extends BaseProfileService implements ProfileManagementService {

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
