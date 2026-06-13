package app.echo_social.modules.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import app.echo_social.modules.profile.api.ProfileManagementAPI;
import app.echo_social.modules.profile.dto.request.UpdateProfileDTO;
import app.echo_social.modules.profile.service.ProfileManagementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class ProfileManagementController implements ProfileManagementAPI {

    private final ProfileManagementService profileManagementService;

    @Override
    public ResponseEntity<Void> updateProfile(@Valid UpdateProfileDTO request) {
        profileManagementService.updateProfile(request);
        return ResponseEntity.noContent().build();
    }

}
