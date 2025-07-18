package com.example.echo_api.controller.profile;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.persistence.dto.response.pagination.PageDTO;
import com.example.echo_api.persistence.dto.response.profile.ProfileDTO;
import com.example.echo_api.persistence.dto.response.profile.SimplifiedProfileDTO;
import com.example.echo_api.service.profile.view.ProfileViewService;
import com.example.echo_api.util.pagination.OffsetLimitRequest;
import com.example.echo_api.validation.pagination.annotations.Limit;
import com.example.echo_api.validation.pagination.annotations.Offset;

import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
public class ProfileViewController {

    private final ProfileViewService profileViewService;

    @GetMapping(ApiConfig.Profile.ME)
    public ResponseEntity<ProfileDTO> getMe() {
        return ResponseEntity.ok(profileViewService.getMe());
    }

    @GetMapping(ApiConfig.Profile.GET_BY_USERNAME)
    public ResponseEntity<ProfileDTO> getByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(profileViewService.getByUsername(username));
    }

    // @formatter:off
    @GetMapping(ApiConfig.Profile.GET_FOLLOWERS_BY_ID)
    public ResponseEntity<PageDTO<SimplifiedProfileDTO>> getFollowers(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(profileViewService.getFollowers(id, page));
    }

    @GetMapping(ApiConfig.Profile.GET_FOLLOWING_BY_ID)
    public ResponseEntity<PageDTO<SimplifiedProfileDTO>> getFollowing(
       @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit
    ) {
        Pageable page = new OffsetLimitRequest(offset, limit);
        return ResponseEntity.ok(profileViewService.getFollowing(id, page));
    }
    // @formatter:on

}
