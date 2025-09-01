package com.example.echo_api.modules.profile.api;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.modules.profile.dto.ProfileDTO;
import com.example.echo_api.modules.profile.dto.SimplifiedProfileDTO;
import com.example.echo_api.modules.profile.service.ProfileViewService;
import com.example.echo_api.shared.dto.PageDTO;
import com.example.echo_api.util.OffsetLimitRequest;
import com.example.echo_api.validation.pagination.annotations.Limit;
import com.example.echo_api.validation.pagination.annotations.Offset;

import lombok.RequiredArgsConstructor;

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
    public ResponseEntity<PageDTO<SimplifiedProfileDTO>> getFollowers(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(profileViewService.getFollowers(id, page));
    }

    @GetMapping(ApiRoutes.PROFILE.FOLLOWING)
    public ResponseEntity<PageDTO<SimplifiedProfileDTO>> getFollowing(
       @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(profileViewService.getFollowing(id, page));
    }
    // @formatter:on

}
