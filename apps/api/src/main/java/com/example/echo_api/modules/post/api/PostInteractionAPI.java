package com.example.echo_api.modules.post.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.shared.constant.ApiRoutes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Post API")
public interface PostInteractionAPI {

    @Operation(description = "Like a post by ID")
    @ApiResponse(responseCode = "404", description = "ID not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Already liked post with ID", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(ApiRoutes.POST.LIKE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> like(@PathVariable("id") UUID id);

    @Operation(description = "Unlike a post by ID")
    @DeleteMapping(ApiRoutes.POST.LIKE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> unlike(@PathVariable("id") UUID id);

}
