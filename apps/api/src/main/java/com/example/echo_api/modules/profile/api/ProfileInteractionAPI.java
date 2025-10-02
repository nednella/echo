package com.example.echo_api.modules.profile.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.echo_api.shared.constant.ApiRoutes;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Profile API")
public interface ProfileInteractionAPI {

    @PostMapping(ApiRoutes.PROFILE.FOLLOW)
    public ResponseEntity<Void> followById(@PathVariable("id") UUID id);

    @DeleteMapping(ApiRoutes.PROFILE.FOLLOW)
    public ResponseEntity<Void> unfollowById(@PathVariable("id") UUID id);

}
