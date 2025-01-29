package com.example.echo_api.controller.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.service.profile.ProfileService;
import com.example.echo_api.validation.sequence.ValidationOrder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@Validated(ValidationOrder.class)
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping(ApiConfig.Profile.ME)
    public ResponseEntity<ProfileDTO> getMe() {
        ProfileDTO response = profileService.getMe();
        return ResponseEntity.ok(response);
    }

    @PutMapping(ApiConfig.Profile.ME)
    public ResponseEntity<Void> updateMeProfile(@RequestBody @Valid UpdateProfileDTO request) {
        profileService.updateMeProfile(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(ApiConfig.Profile.GET_BY_USERNAME)
    public ResponseEntity<ProfileDTO> getByUsername(@PathVariable("username") String username) {
        ProfileDTO response = profileService.getByUsername(username);
        return ResponseEntity.ok(response);
    }

    // --- follow ----
    @GetMapping(ApiConfig.Profile.GET_FOLLOWERS_BY_USERNAME)
    public ResponseEntity<Void> getFollowers(@PathVariable("username") String username) {
        return ResponseEntity.noContent().build();
    }

    @GetMapping(ApiConfig.Profile.GET_FOLLOWING_BY_USERNAME)
    public ResponseEntity<Void> getFollowing(@PathVariable("username") String username) {
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ApiConfig.Profile.FOLLOW_BY_USERNAME)
    public ResponseEntity<Void> followProfile(@PathVariable("username") String username) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.UNFOLLOW_BY_USERNAME)
    public ResponseEntity<Void> unfollowProfile(@PathVariable("username") String username) {
        return ResponseEntity.noContent().build();
    }

    // ---- block ----
    @PostMapping(ApiConfig.Profile.BLOCK_BY_USERNAME)
    public ResponseEntity<Void> blockProfile(@PathVariable("username") String username) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.UNBLOCK_BY_USERNAME)
    public ResponseEntity<Void> unblockProfile(@PathVariable("username") String username) {
        return ResponseEntity.noContent().build();
    }

}
