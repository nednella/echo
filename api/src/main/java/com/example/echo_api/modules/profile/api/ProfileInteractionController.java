package com.example.echo_api.modules.profile.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.constants.ApiRoutes;
import com.example.echo_api.service.profile.interaction.ProfileInteractionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfileInteractionController {

    private final ProfileInteractionService profileInteractionService;

    @PostMapping(ApiRoutes.PROFILE.FOLLOW)
    public ResponseEntity<Void> followById(@PathVariable("id") UUID id) {
        profileInteractionService.follow(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiRoutes.PROFILE.FOLLOW)
    public ResponseEntity<Void> unfollowById(@PathVariable("id") UUID id) {
        profileInteractionService.unfollow(id);
        return ResponseEntity.noContent().build();
    }

}
