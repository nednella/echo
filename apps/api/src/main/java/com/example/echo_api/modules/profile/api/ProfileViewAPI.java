package com.example.echo_api.modules.profile.api;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.profile.dto.response.ProfileDTO;
import com.example.echo_api.modules.profile.dto.response.SimplifiedProfileDTO;
import com.example.echo_api.shared.constant.ApiRoutes;
import com.example.echo_api.shared.pagination.PageParameters;
import com.example.echo_api.shared.pagination.Paged;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Profile API")
@Validated
public interface ProfileViewAPI {

    @Operation(description = "Get your profile")
    @GetMapping(ApiRoutes.PROFILE.ME)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProfileDTO> getMe();

    @Operation(description = "Get a profile by username")
    @ApiResponse(responseCode = "404", description = "Username not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping(ApiRoutes.PROFILE.BY_USERNAME)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ProfileDTO> getByUsername(@PathVariable("username") String username);

    @Operation(description = "Get followers by ID")
    @ApiResponse(responseCode = "400", description = "Invalid pagination parameter(s)", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "ID not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping(ApiRoutes.PROFILE.FOLLOWERS)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Paged<SimplifiedProfileDTO>> getFollowers(
        @PathVariable("id") UUID id,
        @Valid PageParameters pageParams);

    @Operation(description = "Get following by ID")
    @ApiResponse(responseCode = "400", description = "Invalid pagination parameter(s)", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "ID not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping(ApiRoutes.PROFILE.FOLLOWING)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Paged<SimplifiedProfileDTO>> getFollowing(
        @PathVariable("id") UUID id,
        @Valid PageParameters pageParams);

}
