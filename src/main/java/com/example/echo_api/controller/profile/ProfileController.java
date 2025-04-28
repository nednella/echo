package com.example.echo_api.controller.profile;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.persistence.dto.request.profile.UpdateProfileDTO;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.example.echo_api.service.profile.interaction.ProfileInteractionService;
import com.example.echo_api.service.profile.management.ProfileManagementService;
import com.example.echo_api.service.profile.view.ProfileViewService;
import com.example.echo_api.util.pagination.OffsetLimitRequest;
import com.example.echo_api.util.validator.ImageValidator;
import com.example.echo_api.validation.pagination.annotations.Limit;
import com.example.echo_api.validation.pagination.annotations.Offset;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequiredArgsConstructor
@Validated
public class ProfileController {

    private final ProfileViewService profileViewService;
    private final ProfileManagementService profileManagementService;
    private final ProfileInteractionService profileInteractionService;

    // ---- get profile ----

    @GetMapping(ApiConfig.Profile.ME)
    public ResponseEntity<ProfileDTO> getMe() {
        return ResponseEntity.ok(profileViewService.getSelf());
    }

    @GetMapping(ApiConfig.Profile.GET_BY_USERNAME)
    public ResponseEntity<ProfileDTO> getByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(profileViewService.getByUsername(username));
    }

    // ---- update self profile ----

    @PutMapping(ApiConfig.Profile.ME_INFO)
    public ResponseEntity<Void> updateMeProfile(@RequestBody @Valid UpdateProfileDTO request) {
        profileManagementService.updateInformation(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ApiConfig.Profile.ME_AVATAR)
    public ResponseEntity<Void> updateMeAvatar(@RequestPart("file") MultipartFile file) {
        ImageValidator.validate(file);
        profileManagementService.updateAvatar(file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.ME_AVATAR)
    public ResponseEntity<Void> deleteMeAvatar() {
        profileManagementService.deleteAvatar();
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ApiConfig.Profile.ME_BANNER)
    public ResponseEntity<Void> updateMeBanner(@RequestPart("file") MultipartFile file) {
        ImageValidator.validate(file);
        profileManagementService.updateBanner(file);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.ME_BANNER)
    public ResponseEntity<Void> deleteMeBanner() {
        profileManagementService.deleteBanner();
        return ResponseEntity.noContent().build();
    }

    // --- following/follower list ----

    // @formatter:off
    @GetMapping(ApiConfig.Profile.GET_FOLLOWERS_BY_USERNAME)
    public ResponseEntity<PageDTO<SimplifiedProfileDTO>> getFollowers(
        @PathVariable("username") String username,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(profileViewService.getFollowers(username, page));
    }

    @GetMapping(ApiConfig.Profile.GET_FOLLOWING_BY_USERNAME)
    public ResponseEntity<PageDTO<SimplifiedProfileDTO>> getFollowing(
        @PathVariable("username") String username,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(profileViewService.getFollowing(username, page));
    }
    // @formatter:on

    // --- follow ----

    @PostMapping(ApiConfig.Profile.FOLLOW_BY_USERNAME)
    public ResponseEntity<Void> followProfile(@PathVariable("username") String username) {
        profileInteractionService.follow(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.UNFOLLOW_BY_USERNAME)
    public ResponseEntity<Void> unfollowProfile(@PathVariable("username") String username) {
        profileInteractionService.unfollow(username);
        return ResponseEntity.noContent().build();
    }

    // ---- block ----

    @PostMapping(ApiConfig.Profile.BLOCK_BY_USERNAME)
    public ResponseEntity<Void> blockProfile(@PathVariable("username") String username) {
        profileInteractionService.block(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.UNBLOCK_BY_USERNAME)
    public ResponseEntity<Void> unblockProfile(@PathVariable("username") String username) {
        profileInteractionService.unblock(username);
        return ResponseEntity.noContent().build();
    }

}
