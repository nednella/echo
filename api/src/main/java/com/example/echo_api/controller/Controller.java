package com.example.echo_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.oauth2.jwt.Jwt;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    @GetMapping("/public")
    public ResponseEntity<String> publicMthd() {
        return ResponseEntity.ok("This is a public endpoint.");
    }

    @GetMapping("/private")
    public ResponseEntity<String> privateMthd() {
        Authentication authentication = securityContextHolderStrategy.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("No auth!");
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();

        return ResponseEntity.ok("This is a secure endpoint, hello " + jwt.getSubject());
    }

}
