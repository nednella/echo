package com.example.echo_api.modules.post.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.echo_api.modules.post.dto.request.CreatePostDTO;
import com.example.echo_api.shared.constant.ApiRoutes;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Post API")
@Validated
public interface PostManagementAPI {

    @PostMapping(ApiRoutes.POST.CREATE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> create(@RequestBody @Valid CreatePostDTO request);

    @DeleteMapping(ApiRoutes.POST.BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id);

}
