package com.example.echo_api.security;

import java.io.IOException;
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
 * synchronise databases, an onboarding flow is used to guarantee a local
 * reference to the Clerk user before allowing access to the application.
 * 
 * <p>
 * The filter validates two JWT claims before allowing access to protected
 * resources:
 * <ol>
 * <li>Onboarding completion status
 * <li>Valid Echo ID (UUID format)
 * </ol>
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
        if (shouldNotFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = validateAuthenticationPrincipal(authentication);
        validateOnboardingStatus(jwt);
        validateEchoId(jwt);

        filterChain.doFilter(request, response);
    } // @formatter:on

    /**
     * Determine if the current request should bypass onboarding validation.
     * 
     * @param request The current HTTP request
     * @return True if the request is to a public or onboarding endpoint, false
     *         otherwise
     */
    @Override
    public boolean shouldNotFilter(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean isPublicEndpoint = authentication instanceof AnonymousAuthenticationToken;
        boolean isOnboardingEndpoint = request.getRequestURI().equals(ApiConfig.Clerk.ONBOARDING);

        return isPublicEndpoint || isOnboardingEndpoint;
    }

    /**
     * Validate that the authentication principal is a JWT.
     * 
     * @param authentication The Spring Security authentication object
     * @return The validated JWT
     * @throws AccessDeniedException If the principal is not a JWT
     */
    private Jwt validateAuthenticationPrincipal(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof Jwt)) {
            throw new AccessDeniedException(ErrorMessageConfig.Forbidden.INVALID_AUTH_PRINCIPAL);
        }

        return (Jwt) authentication.getPrincipal();
    }

    /**
     * Validate that the {@code onboarded} claim on the JWT indicates the onboarding
     * process has been completed.
     * 
     * @param jwt The authenticated JWT
     * @throws AccessDeniedException If the {@code onboarded} claim is missing,
     *                               malformed, or false
     */
    private void validateOnboardingStatus(Jwt jwt) {
        Object onboarded = jwt.getClaim(ClerkConfig.JWT_ONBOARDED_CLAIM);
        if (!(onboarded instanceof Boolean)) {
            throw new AccessDeniedException(ErrorMessageConfig.Forbidden.ONBOARDED_CLAIM_MISSING_OR_MALFORMED);
        }
        if (!Boolean.TRUE.equals(onboarded)) {
            throw new AccessDeniedException(ErrorMessageConfig.Forbidden.ONBOARDING_NOT_COMPLETED);
        }
    }

    /**
     * Validate that the {@code echo_id} claim on the JWT is a valid UUID.
     * 
     * @param jwt The authenticated JWT
     * @throws AccessDeniedException If the {@code echo_id} claim is missing or
     *                               malformed
     */
    private void validateEchoId(Jwt jwt) {
        String echoId = jwt.getClaimAsString(ClerkConfig.JWT_ECHO_ID_CLAIM);
        if (echoId == null || !isValidUUID(echoId)) {
            throw new AccessDeniedException(ErrorMessageConfig.Forbidden.ECHO_ID_CLAIM_MISSING_OR_MALFORMED);
        }
    }

    /**
     * Check if a string is a valid UUID.
     * 
     * @param id The string to validate
     * @return True if the string is a valid UUID, false otherwise
     */
    private boolean isValidUUID(String id) {
        try {
            UUID.fromString(id);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
