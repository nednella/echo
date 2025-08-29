package com.example.echo_api.controller.dev;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.service.dev.DevService;

import lombok.RequiredArgsConstructor;

@Profile("dev")
@RestController
@RequiredArgsConstructor
public class DevController {

    private final DevService devService;

    // @PostMapping(ApiConfig.Dev.PERSIST_ALL)
    public ResponseEntity<List<User>> persistAllClerkUsers() {
        List<User> users = devService.persistAllClerkUsers();
        return ResponseEntity.status(HttpStatus.CREATED).body(users);
    }

    // @DeleteMapping(ApiConfig.Dev.SYNC_ALL)
    public ResponseEntity<Void> unsyncAllClerkUsers() {
        devService.unsyncAllClerkUsers();
        return ResponseEntity.ok().build();
    }

}
