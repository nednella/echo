package com.example.echo_api.controller.post;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.persistence.dto.request.post.CreatePostDTO;
import com.example.echo_api.service.post.management.PostManagementService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
public class PostManagementController {

    private final PostManagementService postManagementService;

    @PostMapping(ApiConfig.Post.CREATE)
    public ResponseEntity<Void> create(@RequestBody @Valid CreatePostDTO request) {
        postManagementService.create(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(ApiConfig.Post.GET_BY_ID)
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        postManagementService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
