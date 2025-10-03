
package com.example.echo_api.modules.clerk.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.example.echo_api.modules.user.entity.User;
import com.example.echo_api.shared.constant.ApiRoutes;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Clerk API")
public interface ClerkAPI {

    @Operation(description = "Sync the authenticated Clerk user to the local application")
    @PostMapping(ApiRoutes.CLERK.ONBOARDING)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> clerkOnboarding();

    @Hidden
    @PostMapping(ApiRoutes.CLERK.WEBHOOK)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> clerkEvent(@RequestHeader HttpHeaders headers, @RequestBody String payload);

}
