package com.example.echo_api.controller.profile;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.service.profile.interaction.ProfileInteractionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfileInteractionController {

    private final ProfileInteractionService profileInteractionService;

    @PostMapping(ApiConfig.Profile.FOLLOW_BY_ID)
    public ResponseEntity<Void> followById(@PathVariable("id") UUID id) {
        profileInteractionService.follow(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.FOLLOW_BY_ID)
    public ResponseEntity<Void> unfollowById(@PathVariable("id") UUID id) {
        profileInteractionService.unfollow(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ApiConfig.Profile.BLOCK_BY_ID)
    public ResponseEntity<Void> blockById(@PathVariable("id") UUID id) {
        profileInteractionService.block(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.BLOCK_BY_ID)
    public ResponseEntity<Void> unblockById(@PathVariable("id") UUID id) {
        profileInteractionService.unblock(id);
        return ResponseEntity.noContent().build();
    }

}
