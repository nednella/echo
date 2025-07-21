package com.example.echo_api.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.persistence.dto.request.auth.LoginDTO;
import com.example.echo_api.persistence.dto.request.auth.SignupDTO;
import com.example.echo_api.service.auth.AuthService;
import com.example.echo_api.validation.sequence.ValidationSequence;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Validated(ValidationSequence.class)
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(ApiConfig.Auth.LOGIN)
    public ResponseEntity<Void> login(@RequestBody @Valid LoginDTO request) {
        authService.login(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ApiConfig.Auth.SIGNUP)
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupDTO request) {
        authService.signup(request);
        return ResponseEntity.noContent().build();
    }

}
