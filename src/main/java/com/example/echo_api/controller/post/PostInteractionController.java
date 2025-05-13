package com.example.echo_api.controller.post;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.service.post.interaction.PostInteractionService;
import com.example.echo_api.validation.sequence.ValidationOrder;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated(ValidationOrder.class)
public class PostInteractionController {

    private final PostInteractionService postInteractionService;

    @PostMapping(ApiConfig.Post.LIKE)
    public ResponseEntity<Void> create(@PathVariable("id") UUID id) {
        postInteractionService.like(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Post.LIKE)
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        postInteractionService.unlike(id);
        return ResponseEntity.noContent().build();
    }
}
