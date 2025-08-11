package com.example.echo_api.unit.service.auth.session;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.echo_api.config.ClerkConfig;
import com.example.echo_api.service.auth.session.SessionServiceImpl;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @InjectMocks
    private SessionServiceImpl sessionService;

    private Jwt mockJwt;
    private UserDetails mockUserDetails;

    @AfterEach
    void cleanup() {
        SecurityContextHolder.clearContext();
    }

    private void mockJwt() {
        mockJwt = mock(Jwt.class);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockJwt);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void mockUserDetails() {
        mockUserDetails = mock(UserDetails.class);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void getAuthenticatedUserId_ReturnsUUID() {
        // arrange
        UUID expected = UUID.randomUUID();
        String expectedStr = expected.toString();

        mockJwt();
        when(mockJwt.getClaim(ClerkConfig.JWT_ECHO_ID_CLAIM)).thenReturn(expectedStr);

        // act
        UUID actual = sessionService.getAuthenticatedUserId();

        // assert
        assertEquals(expected, actual);
    }

    @Test
    void getAuthenticatedUserClerkId_ReturnsString() {
        // arrange
        String expected = "user_someRandomStringThatIsUniqueApparently";

        mockJwt();
        when(mockJwt.getSubject()).thenReturn(expected);

        // act
        String actual = sessionService.getAuthenticatedUserClerkId();

        // assert
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void isAuthenticatedUserOnboarded_ReturnsBoolean(boolean value) {
        // arrange
        boolean expected = true;

        mockJwt();
        when(mockJwt.getClaim(ClerkConfig.JWT_ONBOARDED_CLAIM)).thenReturn(expected);

        // act
        boolean actual = sessionService.isAuthenticatedUserOnboarded();

        // assert
        assertEquals(expected, actual);
    }

    @Test
    void allMethods_ThrowsWhenPrincipalNotJwt() {
        mockUserDetails();
        assertAll(
            () -> assertThrows(ClassCastException.class,
                () -> sessionService.getAuthenticatedUserId()),
            () -> assertThrows(ClassCastException.class,
                () -> sessionService.getAuthenticatedUserClerkId()),
            () -> assertThrows(ClassCastException.class,
                () -> sessionService.isAuthenticatedUserOnboarded()));
    }

    @Test
    void allMethods_ThrowsWhenNoAuthentication() {
        assertAll(
            () -> assertThrows(NullPointerException.class,
                () -> sessionService.getAuthenticatedUserId()),
            () -> assertThrows(NullPointerException.class,
                () -> sessionService.getAuthenticatedUserClerkId()),
            () -> assertThrows(NullPointerException.class,
                () -> sessionService.isAuthenticatedUserOnboarded()));
    }

}
