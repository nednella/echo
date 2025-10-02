package com.example.echo_api.modules.post.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.Paged;
import com.example.echo_api.shared.validation.annotations.Limit;
import com.example.echo_api.shared.validation.annotations.Offset;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Post API")
@Validated
public interface PostViewAPI {

    @GetMapping(ApiRoutes.POST.BY_ID)
    public ResponseEntity<PostDTO> getPostById(@PathVariable("id") UUID id);

    @GetMapping(ApiRoutes.POST.REPLIES)
    public ResponseEntity<Paged<PostDTO>> getRepliesByPostId(
        @PathVariable("id") UUID id,
        @RequestParam(name = "offset", defaultValue = "0") @Offset int offset,
        @RequestParam(name = "limit", defaultValue = "20") @Limit int limit);

}
