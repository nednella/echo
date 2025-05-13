package com.example.echo_api.controller.profile;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.service.profile.interaction.ProfileInteractionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
public class ProfileInteractionController {

    private final ProfileInteractionService profileInteractionService;

    @PostMapping(ApiConfig.Profile.FOLLOW_BY_USERNAME)
    public ResponseEntity<Void> followByUsername(@PathVariable("username") String username) {
        profileInteractionService.follow(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.FOLLOW_BY_USERNAME)
    public ResponseEntity<Void> unfollowByUsername(@PathVariable("username") String username) {
        profileInteractionService.unfollow(username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ApiConfig.Profile.BLOCK_BY_USERNAME)
    public ResponseEntity<Void> blockByUsername(@PathVariable("username") String username) {
        profileInteractionService.block(username);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Profile.BLOCK_BY_USERNAME)
    public ResponseEntity<Void> unblockByUsername(@PathVariable("username") String username) {
        profileInteractionService.unblock(username);
        return ResponseEntity.noContent().build();
    }

}
