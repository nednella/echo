package app.echo_social.modules.profile.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import app.echo_social.modules.profile.api.ProfileInteractionAPI;
import app.echo_social.modules.profile.service.ProfileInteractionService;

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
