package com.example.echo_api.modules.profile.api;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.echo_api.modules.profile.dto.request.UpdateProfileDTO;
import com.example.echo_api.shared.constant.ApiRoutes;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Profile API")
@Validated
public interface ProfileManagementAPI {

    @PutMapping(ApiRoutes.PROFILE.ME)
    public ResponseEntity<Void> updateProfile(@RequestBody @Valid UpdateProfileDTO request);

}
