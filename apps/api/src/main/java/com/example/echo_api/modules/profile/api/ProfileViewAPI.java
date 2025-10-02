package com.example.echo_api.modules.profile.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.echo_api.modules.profile.dto.response.ProfileDTO;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.PageParameters;
import com.example.echo_api.shared.pagination.Paged;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

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
        @Valid PageParameters pageParams);

    @GetMapping(ApiRoutes.PROFILE.FOLLOWING)
    public ResponseEntity<Paged<SimplifiedProfileDTO>> getFollowing(
        @PathVariable("id") UUID id,
        @Valid PageParameters pageParams);

}
