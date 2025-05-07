package com.example.echo_api.controller.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.service.profile.management.ProfileManagementService;
import com.example.echo_api.util.validator.ImageValidator;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class ProfileManagementController {

    private final ProfileManagementService profileManagementService;

    @PutMapping(ApiConfig.Profile.ME_INFO)
    public ResponseEntity<Void> updateInformation(@RequestBody @Valid UpdateProfileDTO request) {
        profileManagementService.updateInformation(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ApiConfig.Profile.ME_AVATAR)
    public ResponseEntity<Void> updateAvatar(@RequestPart("file") MultipartFile file) {
        ImageValidator.validate(file);
        profileManagementService.updateAvatar(file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.ME_AVATAR)
    public ResponseEntity<Void> deleteAvatar() {
        profileManagementService.deleteAvatar();
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ApiConfig.Profile.ME_BANNER)
    public ResponseEntity<Void> updateBanner(@RequestPart("file") MultipartFile file) {
        ImageValidator.validate(file);
        profileManagementService.updateBanner(file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.ME_BANNER)
    public ResponseEntity<Void> deleteBanner() {
        profileManagementService.deleteBanner();
        return ResponseEntity.noContent().build();
    }

}
