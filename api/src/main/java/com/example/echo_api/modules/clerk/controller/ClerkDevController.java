package com.example.echo_api.modules.clerk.controller;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.modules.clerk.service.ClerkDevService;
import com.example.echo_api.modules.user.entity.User;
import com.example.echo_api.shared.constants.ApiRoutes;

import lombok.RequiredArgsConstructor;

@Profile("dev")
@RestController
@RequiredArgsConstructor
public class ClerkDevController {

    private final ClerkDevService clerkDevService;

    @PostMapping(ApiRoutes.CLERK.PERSIST_ALL)
    public ResponseEntity<List<User>> persistAllClerkUsers() {
        List<User> users = clerkDevService.persistAllClerkUsers();
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    @DeleteMapping(ApiRoutes.CLERK.SYNC_ALL)
    public ResponseEntity<Void> unsyncAllClerkUsers() {
        clerkDevService.unsyncAllClerkUsers();
        return ResponseEntity.ok().build();
    }

}
