package com.example.echo_api.modules.post.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.post.api.PostInteractionAPI;
import com.example.echo_api.modules.post.service.PostInteractionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class PostInteractionController implements PostInteractionAPI {

    private final PostInteractionService postInteractionService;

    @Override
    public ResponseEntity<Void> like(UUID id) {
        postInteractionService.like(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> unlike(UUID id) {
        postInteractionService.unlike(id);
        return ResponseEntity.noContent().build();
    }

}
