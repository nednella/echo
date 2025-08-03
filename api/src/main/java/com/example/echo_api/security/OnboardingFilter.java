package com.example.echo_api.security;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ClerkConfig;
import com.example.echo_api.config.ErrorMessageConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom {@link OncePerRequestFilter} for Spring Security to check the
 * onboarding status of an authenticated user when requested a protected
 * resource.
 * 
 * <p>
 * Since developers cannot directly interact with Clerk's user table to
 * synchronise databases, an onboarding status flag is appended to the JWT
 * metadata claim. The flag is used to indicate whether the Clerk authenticated
 * user has been synced to the local database.
 * 
 * <p>
 * For more information, refer to:
 * <ul>
 * <li>https://clerk.com/docs/webhooks/sync-data
 * <li>https://clerk.com/docs/users/metadata
 * <li>https://clerk.com/docs/references/nextjs/add-onboarding-flow
 * <li>https://docs.spring.io/spring-security/reference/servlet/authentication/anonymous.html
 * </ul>
 */
public class OnboardingFilter extends OncePerRequestFilter {

    @Override // @formatter:off
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Bypass filter when requesting a public endpoint or the onboarding endpoint
        boolean publicEndpoint = authentication instanceof AnonymousAuthenticationToken;
        boolean onboardingEndpoint = request.getRequestURI().equals(ApiConfig.Auth.ONBOARDING);

        if (publicEndpoint || onboardingEndpoint) {
            filterChain.doFilter(request, response);
            return;
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();

        // Validate onboarding metadata
        Map<String, Object> metadata = jwt.getClaimAsMap(ClerkConfig.METADATA);
        Object onboardingComplete = metadata.getOrDefault(ClerkConfig.ONBOARDING_COMPLETE_KEY, false);
        
        if (!(onboardingComplete instanceof Boolean)) {
            throw new AccessDeniedException(ErrorMessageConfig.Forbidden.ONBOARDING_STATUS_MALFORMED);
        }
        if (!(boolean) onboardingComplete) {
            throw new AccessDeniedException(ErrorMessageConfig.Forbidden.ONBOARDING_NOT_COMPLETED);
        }
        
        // Validate Echo ID
        String echoId = jwt.getClaimAsString(ClerkConfig.ECHO_ID);

        if (!isValidUUID(echoId)) {
            throw new AccessDeniedException(ErrorMessageConfig.Forbidden.ECHO_ID_MISSING_OR_MALFORMED);
        }

        // Success
        filterChain.doFilter(request, response);
    } // @formatter:on

    private boolean isValidUUID(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
