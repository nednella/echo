package com.example.echo_api.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.echo_api.service.session.SessionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final SessionService sessionService;

    @GetMapping("/public")
    public ResponseEntity<String> publicMthd() {
        return ResponseEntity.ok("This is a public endpoint.");
    }

    @PostMapping("/public")
    public ResponseEntity<String> publicMthdPost() {
        return ResponseEntity.ok("This is a public endpoint.");
    }

    @GetMapping("/private")
    public ResponseEntity<String> privateMthd() {
        UUID id = sessionService.getAuthenticatedUserId();
        return ResponseEntity.ok("This is a secure endpoint, hello " + id.toString());
    }

}
