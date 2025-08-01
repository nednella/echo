package com.example.echo_api.security;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.echo_api.config.ClerkConfig;
import com.example.echo_api.config.ErrorMessageConfig;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom {@link OncePerRequestFilter} class to be consumed as part of the
 * Spring Security filter chain.
 * 
 * <p>
 * The filter is responsible for checking the onboarding status of an
 * authenticated user based on Clerk's JWT metadata found on their authenticated
 * token.
 * 
 * <p>
 * Since developers cannot directly interact with Clerk's user table, the
 * onboarding status is used as a flag to indicate whether the Clerk user has
 * successfully been synced to a local user in the APIs database.
 * 
 * <p>
 * For more information, refer to:
 * <ul>
 * <li>https://clerk.com/docs/users/metadata
 * <li>https://clerk.com/docs/references/nextjs/add-onboarding-flow
 * </ul>
 */
public class OnboardingFilter extends OncePerRequestFilter {

    @Override // @formatter:off
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
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
        String id = jwt.getClaimAsString(ClerkConfig.ECHO_ID);

        if (!isValidUUID(id)) {
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
