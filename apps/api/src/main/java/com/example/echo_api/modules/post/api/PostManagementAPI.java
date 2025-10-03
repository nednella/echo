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

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.post.dto.request.CreatePostDTO;
import com.example.echo_api.shared.constant.ApiRoutes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Post API")
@Validated
public interface PostManagementAPI {

    @Operation(description = "Create a post")
    @ApiResponse(responseCode = "400", description = "Invalid field(s)", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Parent ID not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping(ApiRoutes.POST.CREATE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> create(@RequestBody @Valid CreatePostDTO request);

    @Operation(description = "Delete a post by ID")
    @ApiResponse(responseCode = "403", description = "Cannot delete another user's post", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping(ApiRoutes.POST.BY_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id);

}
