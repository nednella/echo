package com.example.echo_api.modules.profile.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.profile.api.ProfileViewAPI;
import com.example.echo_api.modules.profile.dto.response.ProfileDTO;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.modules.profile.service.ProfileViewService;
import com.example.echo_api.shared.pagination.OffsetLimitRequest;
import com.example.echo_api.shared.pagination.Paged;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class ProfileViewController implements ProfileViewAPI {

    private final ProfileViewService profileViewService;

    @Override
    public ResponseEntity<ProfileDTO> getMe() {
        return ResponseEntity.ok(profileViewService.getMe());
    }

    @Override
    public ResponseEntity<ProfileDTO> getByUsername(String username) {
        return ResponseEntity.ok(profileViewService.getByUsername(username));
    }

    @Override
    public ResponseEntity<Paged<SimplifiedProfileDTO>> getFollowers(UUID id, int offset, int limit) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(profileViewService.getFollowers(id, page));
    }

    @Override
    public ResponseEntity<Paged<SimplifiedProfileDTO>> getFollowing(UUID id, int offset, int limit) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(profileViewService.getFollowing(id, page));
    }

}
