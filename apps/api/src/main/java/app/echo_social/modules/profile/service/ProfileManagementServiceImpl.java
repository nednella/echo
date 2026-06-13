package app.echo_social.modules.profile.service;

import org.springframework.stereotype.Service;

import app.echo_social.modules.profile.dto.request.UpdateProfileDTO;
import app.echo_social.modules.profile.entity.Profile;
import app.echo_social.modules.profile.repository.ProfileRepository;
import app.echo_social.shared.service.SessionService;

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
        Profile me = getProfile(getAuthenticatedUserId());
        me.setName(request.name());
        me.setBio(request.bio());
        me.setLocation(request.location());

        profileRepository.save(me);
    }

}
