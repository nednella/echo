package com.example.echo_api.modules.profile.service;

import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;

public interface ProfileManagementService {

    /**
     * Update the profile information of the authenticated user.
     * 
     * <p>
     * Valid fields are {@code name}, {@code bio}, {@code location}.
     * 
     * @param request the request DTO containing the updated profile information
     */
    public void updateProfile(UpdateProfileDTO request);

}
