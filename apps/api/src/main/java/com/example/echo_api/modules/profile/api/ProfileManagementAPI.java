package com.example.echo_api.modules.profile.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.echo_api.exception.ErrorResponse;
import com.example.echo_api.modules.profile.dto.request.UpdateProfileDTO;
import com.example.echo_api.shared.constant.ApiRoutes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Profile API")
@Validated
public interface ProfileManagementAPI {

    @Operation(description = "Update your profile")
    @ApiResponse(responseCode = "400", description = "Invalid field(s)", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping(ApiRoutes.PROFILE.ME)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> updateProfile(@RequestBody @Valid UpdateProfileDTO request);

}
