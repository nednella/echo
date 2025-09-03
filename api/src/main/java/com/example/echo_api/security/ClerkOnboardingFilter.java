package com.example.echo_api.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.echo_api.config.ClerkConfig;
import com.example.echo_api.shared.constant.ApiRoutes;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom {@link OncePerRequestFilter} for Spring Security that enforces
 * onboarding status for authenticated users.
 * 
 * <p>
 * All <b>authenticated</b> requests must include the following two JWT claims:
 * <ul>
 * <li><b>onboarded</b> - boolean flag indicating the onboarding status
 * <li><b>echo_id</b> - UUID referencing the synchronised local user entity
 * </ul>
 * 
 * <p>
 * Validation differs depending on the request:
 * <ul>
 * <li><b>Onboarding route</b>: both claims must be present, but the claim
 * values do not matter.
 * <li><b>Any other route</b>: both claims must be present and well-formed, and
 * {@code onboarded} must be {@code true}.
 * </ul>
 * 
 * <p>
 * The filter ensures that a local reference to the authenticated Clerk user is
 * created and synced before granting access to the API. The database sync is
 * guaranteed with the use of an onboarding flow since Clerk does not allow
 * developers to directly interact with the user table.
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
public class ClerkOnboardingFilter extends OncePerRequestFilter {

    @Override // @formatter:off
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException
    {
        if (isPublicEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = validateAuthenticationPrincipal(authentication);
        validateExpectedClaimsArePresent(jwt);

        if (isOnboardingEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        validateOnboardingStatus(jwt);
        validateEchoId(jwt);

        filterChain.doFilter(request, response);
    } // @formatter:on

    /**
     * Determine if the current request should bypass the filter entirely.
     * 
     * @param request the current HTTP request
     * @return true if the request is to a public endpoint, false otherwise
     */
    public boolean isPublicEndpoint(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication instanceof AnonymousAuthenticationToken;
    }

    /**
     * Validate that the authentication principal is a JWT.
     * 
     * @param authentication the Spring Security authentication object
     * @return the validated JWT
     * @throws AccessDeniedException if the principal is not a JWT
     */
    private Jwt validateAuthenticationPrincipal(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof Jwt)) {
            throw new AccessDeniedException("Authentication principal is missing or invalid");
        }

        return (Jwt) authentication.getPrincipal();
    }

    /**
     * Validate that the {@code echo_id} and {@code onboarded} token claims are
     * present.
     * 
     * @param jwt the authenticated JWT
     * @throws AccessDeniedException if the {@code onboarded} claim or
     *                               {@code echo_id} claim is missing
     */
    private void validateExpectedClaimsArePresent(Jwt jwt) {
        if (!jwt.hasClaim(ClerkConfig.JWT_ONBOARDED_CLAIM)) {
            throw new AccessDeniedException("Required token claim 'onboarded' is missing");
        }
        if (!jwt.hasClaim(ClerkConfig.JWT_ECHO_ID_CLAIM)) {
            throw new AccessDeniedException("Required token claim 'echo_id' is missing");
        }
    }

    /**
     * Determine if the current request should bypass the onboarding status
     * validation.
     * 
     * @param request the current HTTP request
     * @return true if the request is to the onboarding endpoint, false otherwise
     */
    private boolean isOnboardingEndpoint(HttpServletRequest request) {
        return request.getRequestURI().equals(ApiRoutes.CLERK.ONBOARDING);
    }

    /**
     * Validate that the {@code onboarded} claim on the JWT indicates the onboarding
     * process has been completed.
     * 
     * @param jwt the authenticated JWT
     * @throws AccessDeniedException if the {@code onboarded} claim is malformed, or
     *                               equal to false
     */
    private void validateOnboardingStatus(Jwt jwt) {
        Object onboarded = jwt.getClaim(ClerkConfig.JWT_ONBOARDED_CLAIM);
        if (!(onboarded instanceof Boolean)) {
            throw new AccessDeniedException("Token claim 'onboarded' contains an unexpected value");
        }
        if (!Boolean.TRUE.equals(onboarded)) {
            throw new AccessDeniedException("User has not completed the onboarding process");
        }
    }

    /**
     * Validate that the {@code echo_id} claim on the JWT is a valid UUID.
     * 
     * @param jwt the authenticated JWT
     * @throws AccessDeniedException if the {@code echo_id} claim is malformed
     */
    private void validateEchoId(Jwt jwt) {
        String echoId = jwt.getClaimAsString(ClerkConfig.JWT_ECHO_ID_CLAIM);
        if (echoId == null || !isValidUUID(echoId)) {
            throw new AccessDeniedException("Token claim 'echo_id' contains an unexpected value");
        }
    }

    /**
     * Check if a string is a valid UUID.
     * 
     * @param id the string to validate
     * @return true if the string is a valid UUID, false otherwise
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
