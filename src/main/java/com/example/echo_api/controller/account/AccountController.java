package com.example.echo_api.controller.account;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.persistence.dto.request.account.UpdatePasswordDTO;
import com.example.echo_api.persistence.dto.request.account.UpdateUsernameDTO;
import com.example.echo_api.service.account.AccountService;
import com.example.echo_api.validation.sequence.ValidationOrder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequiredArgsConstructor
@Validated(ValidationOrder.class)
public class AccountController {

    private final AccountService accountService;

    @GetMapping(ApiConfig.Account.USERNAME_AVAILABLE)
    public ResponseEntity<Boolean> isUsernameAvailable(@RequestParam("username") String username) {
        return ResponseEntity.ok(!accountService.existsByUsername(username));
    }

    @PutMapping(ApiConfig.Account.UPDATE_USERNAME)
    public ResponseEntity<Void> updateUsername(@RequestParam("username") @Valid UpdateUsernameDTO request) {
        accountService.updateUsername(request.username());
        return ResponseEntity.noContent().build();
    }

    @PutMapping(ApiConfig.Account.UPDATE_PASSWORD)
    public ResponseEntity<Void> updatePassword(@RequestBody @Valid UpdatePasswordDTO request) {
        accountService.updatePassword(request.currentPassword(), request.newPassword());
        return ResponseEntity.noContent().build();
    }

}
