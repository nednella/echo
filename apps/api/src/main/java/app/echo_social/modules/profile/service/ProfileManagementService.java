package app.echo_social.modules.profile.service;

import app.echo_social.modules.profile.dto.request.UpdateProfileDTO;

public interface ProfileManagementService {

    /**
     * Update the profile information of the authenticated user.
     * 
     * <p>
     * Valid fields are {@code name}, {@code bio}, {@code location}.
     * 
     * @param request the request DTO containing the updated profile information
     */
    void updateProfile(UpdateProfileDTO request);

}
