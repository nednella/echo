package com.example.echo_api.modules.post.api;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.echo_api.shared.constant.ApiRoutes;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Post API")
public interface PostInteractionAPI {

    @PostMapping(ApiRoutes.POST.LIKE)
    public ResponseEntity<Void> like(@PathVariable("id") UUID id);

    @DeleteMapping(ApiRoutes.POST.LIKE)
    public ResponseEntity<Void> unlike(@PathVariable("id") UUID id);

}
