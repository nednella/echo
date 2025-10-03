package com.example.echo_api.modules.feed.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.post.dto.response.PostDTO;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.PageParameters;
import com.example.echo_api.shared.pagination.Paged;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Feed API")
@Validated
public interface FeedAPI {

    @Operation(description = "Get your homepage posts")
    @GetMapping(ApiRoutes.FEED.HOMEPAGE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getHomeFeed(@Valid PageParameters pageParams);

    @Operation(description = "Get discover page posts")
    @GetMapping(ApiRoutes.FEED.DISCOVER)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getDiscoverFeed(@Valid PageParameters pageParams);

    @Operation(description = "Get posts by profile ID")
    @ApiResponse(responseCode = "404", description = "ID not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping(ApiRoutes.FEED.POSTS)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getProfilePosts(@PathVariable("id") UUID id, @Valid PageParameters pageParams);

    @Operation(description = "Get replies by profile ID")
    @ApiResponse(responseCode = "404", description = "ID not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping(ApiRoutes.FEED.REPLIES)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getProfileReplies(@PathVariable("id") UUID id, @Valid PageParameters pageParams);

    @Operation(description = "Get likes by profile ID")
    @ApiResponse(responseCode = "404", description = "ID not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping(ApiRoutes.FEED.LIKES)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getProfileLikes(@PathVariable("id") UUID id, @Valid PageParameters pageParams);

    @Operation(description = "Get mentions of profile ID")
    @ApiResponse(responseCode = "404", description = "ID not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping(ApiRoutes.FEED.MENTIONS)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Paged<PostDTO>> getProfileMentions(@PathVariable("id") UUID id, @Valid PageParameters pageParams);

}
