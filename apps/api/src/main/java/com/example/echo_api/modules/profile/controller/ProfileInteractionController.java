package com.example.echo_api.modules.profile.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.profile.api.ProfileInteractionAPI;
import com.example.echo_api.modules.profile.service.ProfileInteractionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class ProfileInteractionController implements ProfileInteractionAPI {

    private final ProfileInteractionService profileInteractionService;

    @Override
    public ResponseEntity<Void> followById(UUID id) {
        profileInteractionService.follow(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> unfollowById(UUID id) {
        profileInteractionService.unfollow(id);
        return ResponseEntity.noContent().build();
    }

}
