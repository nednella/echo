
package com.example.echo_api.modules.clerk.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.echo_api.modules.user.entity.User;
import com.example.echo_api.shared.constant.ApiRoutes;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Clerk API")
public interface ClerkAPI {

    @PostMapping(ApiRoutes.CLERK.ONBOARDING)
    public ResponseEntity<User> clerkOnboarding();

    @Hidden
    @PostMapping(ApiRoutes.CLERK.WEBHOOK)
    public ResponseEntity<Void> clerkEvent(@RequestHeader HttpHeaders headers, @RequestBody String payload);

}
