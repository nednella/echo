package com.example.echo_api.unit.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.config.ClerkConfig;
import com.example.echo_api.config.ErrorMessageConfig;
import com.example.echo_api.security.ClerkOnboardingFilter;

import jakarta.servlet.FilterChain;

@ExtendWith(MockitoExtension.class)
class OnboardingFilterTest {

    @Mock
    private FilterChain filterChain;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ClerkOnboardingFilter onboardingFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    private Jwt mockJwt() {
        Jwt jwt = mock(Jwt.class);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);

        return jwt;
    }

    private void mockAnonymousAuthentication() {
        String key = "key";
        String principal = "anonymousUser";
        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        AnonymousAuthenticationToken anonAuthentication = new AnonymousAuthenticationToken(key, principal, authorities);

        SecurityContextHolder.getContext().setAuthentication(anonAuthentication);
    }

    private void mockUserDetails() {
        UserDetails userDetails = mock(UserDetails.class);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
    }

    @Test
    void DoFilterInternal_AllowsOnboardedUser() throws Exception {
        // arrange
        Object onboarded = true;
        String echoId = UUID.randomUUID().toString();

        Jwt jwt = mockJwt();
        when(jwt.getClaim(ClerkConfig.JWT_ONBOARDED_CLAIM)).thenReturn(onboarded);
        when(jwt.getClaimAsString(ClerkConfig.JWT_ECHO_ID_CLAIM)).thenReturn(echoId);

        // act
        onboardingFilter.doFilter(request, response, filterChain);

        // assert
        verify(jwt).getClaim(ClerkConfig.JWT_ONBOARDED_CLAIM);
        verify(jwt).getClaimAsString(ClerkConfig.JWT_ECHO_ID_CLAIM);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void DoFilterInternal_BypassesFilterForPublicEndpoint() throws Exception {
        // arrange
        mockAnonymousAuthentication();

        // act
        onboardingFilter.doFilter(request, response, filterChain);

        // assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void DoFilterInternal_BypassesFilterForOnboardingEndpoint() throws Exception {
        // arrange
        request.setRequestURI(ApiConfig.Clerk.ONBOARDING);

        // act
        onboardingFilter.doFilter(request, response, filterChain);

        // assert
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void DoFilterInternal_ThrowsWhenAuthenticationPrincipalNotJwt() throws Exception {
        // arrange
        mockUserDetails();

        // act & assert
        Exception ex = assertThrows(AccessDeniedException.class,
            () -> onboardingFilter.doFilter(request, response, filterChain));

        assertEquals(ErrorMessageConfig.Forbidden.INVALID_AUTH_PRINCIPAL, ex.getMessage());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void DoFilterInternal_ThrowsWhenOnboardedClaimIsMissing() throws Exception {
        // arrange
        Boolean onboarded = null;
        Jwt jwt = mockJwt();
        when(jwt.getClaim(ClerkConfig.JWT_ONBOARDED_CLAIM)).thenReturn(onboarded);

        // act & assert
        Exception ex = assertThrows(AccessDeniedException.class,
            () -> onboardingFilter.doFilter(request, response, filterChain));

        assertEquals(ErrorMessageConfig.Forbidden.ONBOARDED_CLAIM_MISSING_OR_MALFORMED, ex.getMessage());
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void DoFilterInternal_ThrowsWhenUserNotOnboarded() throws Exception {
        // arrange
        Boolean onboarded = false;
        Jwt jwt = mockJwt();
        when(jwt.getClaim(ClerkConfig.JWT_ONBOARDED_CLAIM)).thenReturn(onboarded);

        // act & assert
        Exception ex = assertThrows(AccessDeniedException.class,
            () -> onboardingFilter.doFilter(request, response, filterChain));

        assertEquals(ErrorMessageConfig.Forbidden.ONBOARDING_NOT_COMPLETED, ex.getMessage());
        verify(filterChain, never()).doFilter(request, response);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { "invalid_UUID_string" })
    void DoFilterInternal_ThrowsWhenEchoIdClaimIsMissingOrInvalidUUID(String value) throws Exception {
        // arrange
        Boolean onboarded = true;
        Jwt jwt = mockJwt();
        when(jwt.getClaim(ClerkConfig.JWT_ONBOARDED_CLAIM)).thenReturn(onboarded);
        when(jwt.getClaimAsString(ClerkConfig.JWT_ECHO_ID_CLAIM)).thenReturn(value);

        // act & assert
        Exception ex = assertThrows(AccessDeniedException.class,
            () -> onboardingFilter.doFilter(request, response, filterChain));

        assertEquals(ErrorMessageConfig.Forbidden.ECHO_ID_CLAIM_MISSING_OR_MALFORMED, ex.getMessage());
        verify(filterChain, never()).doFilter(request, response);
    }

}
