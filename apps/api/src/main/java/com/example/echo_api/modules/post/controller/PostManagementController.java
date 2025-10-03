package com.example.echo_api.modules.post.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.post.api.PostManagementAPI;
import com.example.echo_api.modules.post.dto.request.CreatePostDTO;
import com.example.echo_api.modules.post.service.PostManagementService;

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
