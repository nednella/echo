package com.example.echo_api.modules.profile.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.service.profile.management.ProfileManagementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
public class ProfileManagementController {

    private final ProfileManagementService profileManagementService;

    @PutMapping(ApiRoutes.PROFILE.ME)
    public ResponseEntity<Void> updateProfile(@RequestBody @Valid UpdateProfileDTO request) {
        profileManagementService.updateProfile(request);
        return ResponseEntity.noContent().build();
    }

}
