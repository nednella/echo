package com.example.echo_api.modules.profile.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.profile.dto.response.ProfileDTO;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.modules.profile.service.ProfileViewService;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.OffsetLimitRequest;
import com.example.echo_api.shared.pagination.Paged;
import com.example.echo_api.shared.validation.annotations.Limit;
import com.example.echo_api.shared.validation.annotations.Offset;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Profile")
@Validated
@RestController
@RequiredArgsConstructor
public class ProfileViewController {

    private final ProfileViewService profileViewService;

    @GetMapping(ApiRoutes.PROFILE.ME)
    public ResponseEntity<ProfileDTO> getMe() {
        return ResponseEntity.ok(profileViewService.getMe());
    }

    @GetMapping(ApiRoutes.PROFILE.BY_USERNAME)
    public ResponseEntity<ProfileDTO> getByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(profileViewService.getByUsername(username));
    }

    // @formatter:off
    @GetMapping(ApiRoutes.PROFILE.FOLLOWERS)
    public ResponseEntity<Paged<SimplifiedProfileDTO>> getFollowers(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(profileViewService.getFollowers(id, page));
    }

    @GetMapping(ApiRoutes.PROFILE.FOLLOWING)
    public ResponseEntity<Paged<SimplifiedProfileDTO>> getFollowing(
       @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(profileViewService.getFollowing(id, page));
    }
    // @formatter:on

}
