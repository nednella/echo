package com.example.echo_api.modules.post.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.post.service.PostInteractionService;
import com.example.echo_api.shared.constant.ApiRoutes;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class PostInteractionController {

    private final PostInteractionService postInteractionService;

    @PostMapping(ApiRoutes.POST.LIKE)
    public ResponseEntity<Void> like(@PathVariable("id") UUID id) {
        postInteractionService.like(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiRoutes.POST.LIKE)
    public ResponseEntity<Void> unlike(@PathVariable("id") UUID id) {
        postInteractionService.unlike(id);
        return ResponseEntity.noContent().build();
    }

}
