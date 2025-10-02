package com.example.echo_api.modules.profile.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.echo_api.modules.profile.dto.response.ProfileDTO;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.Paged;
import com.example.echo_api.shared.validation.annotations.Limit;
import com.example.echo_api.shared.validation.annotations.Offset;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Profile API")
@Validated
public interface ProfileViewAPI {

    @GetMapping(ApiRoutes.PROFILE.ME)
    public ResponseEntity<ProfileDTO> getMe();

    @GetMapping(ApiRoutes.PROFILE.BY_USERNAME)
    public ResponseEntity<ProfileDTO> getByUsername(@PathVariable("username") String username);

    @GetMapping(ApiRoutes.PROFILE.FOLLOWERS)
    public ResponseEntity<Paged<SimplifiedProfileDTO>> getFollowers(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit);

    @GetMapping(ApiRoutes.PROFILE.FOLLOWING)
    public ResponseEntity<Paged<SimplifiedProfileDTO>> getFollowing(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit);

}
