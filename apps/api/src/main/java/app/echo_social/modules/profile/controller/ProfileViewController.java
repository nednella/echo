package app.echo_social.modules.profile.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import app.echo_social.modules.profile.api.ProfileViewAPI;
import app.echo_social.modules.profile.dto.response.ProfileDTO;
import app.echo_social.modules.profile.dto.response.SimplifiedProfileDTO;
import app.echo_social.modules.profile.service.ProfileViewService;
import app.echo_social.shared.pagination.OffsetLimitRequest;
import app.echo_social.shared.pagination.PageParameters;
import app.echo_social.shared.pagination.Paged;

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
    public ResponseEntity<Paged<SimplifiedProfileDTO>> getFollowers(UUID id, PageParameters pageParams) {
        Pageable page = OffsetLimitRequest.of(pageParams.getOffset(), pageParams.getLimit());
        return ResponseEntity.ok(profileViewService.getFollowers(id, page));
    }

    @Override
    public ResponseEntity<Paged<SimplifiedProfileDTO>> getFollowing(UUID id, PageParameters pageParams) {
        Pageable page = OffsetLimitRequest.of(pageParams.getOffset(), pageParams.getLimit());
        return ResponseEntity.ok(profileViewService.getFollowing(id, page));
    }

}
