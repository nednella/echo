package com.example.echo_api.modules.post.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.modules.post.service.PostViewService;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.OffsetLimitRequest;
import com.example.echo_api.shared.pagination.PageDTO;
import com.example.echo_api.shared.validation.annotations.Limit;
import com.example.echo_api.shared.validation.annotations.Offset;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Validated
@RestController
@RequiredArgsConstructor
public class PostViewController {

    private final PostViewService postViewService;

    @GetMapping(ApiRoutes.POST.BY_ID)
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(postViewService.getPostById(id));
    }

    @GetMapping(ApiRoutes.POST.REPLIES)
    public ResponseEntity<PageDTO<PostDTO>> getRepliesByPostId(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit) {
        Pageable page = OffsetLimitRequest.of(offset, limit);
        return ResponseEntity.ok(postViewService.getRepliesByPostId(id, page));
    }

}
