package com.example.echo_api.modules.profile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.profile.api.ProfileManagementAPI;
import com.example.echo_api.modules.profile.dto.request.UpdateProfileDTO;
import com.example.echo_api.modules.profile.service.ProfileManagementService;

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
