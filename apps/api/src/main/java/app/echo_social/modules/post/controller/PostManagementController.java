package app.echo_social.modules.post.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import app.echo_social.modules.post.api.PostManagementAPI;
import app.echo_social.modules.post.dto.request.CreatePostDTO;
import app.echo_social.modules.post.service.PostManagementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class PostManagementController implements PostManagementAPI {

    private final PostManagementService postManagementService;

    @Override
    public ResponseEntity<Void> create(@Valid CreatePostDTO request) {
        postManagementService.create(request);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> delete(UUID id) {
        postManagementService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
