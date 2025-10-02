package com.example.echo_api.modules.post.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.post.api.PostViewAPI;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.modules.post.service.PostViewService;
import com.example.echo_api.shared.pagination.OffsetLimitRequest;
import com.example.echo_api.shared.pagination.PageParameters;
import com.example.echo_api.shared.pagination.Paged;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class PostViewController implements PostViewAPI {

    private final PostViewService postViewService;

    @Override
    public ResponseEntity<PostDTO> getPostById(UUID id) {
        return ResponseEntity.ok(postViewService.getPostById(id));
    }

    @Override
    public ResponseEntity<Paged<PostDTO>> getRepliesByPostId(UUID id, PageParameters pageParams) {
        Pageable page = OffsetLimitRequest.of(pageParams.getOffset(), pageParams.getLimit());
        return ResponseEntity.ok(postViewService.getRepliesByPostId(id, page));
    }

}
