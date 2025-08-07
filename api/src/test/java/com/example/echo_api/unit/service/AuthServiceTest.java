package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;

import com.example.echo_api.exception.custom.badrequest.UsernameAlreadyExistsException;
import com.example.echo_api.persistence.dto.request.auth.LoginDTO;
import com.example.echo_api.persistence.dto.request.auth.SignupDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.service.account.AccountService;
import com.example.echo_api.service.auth.onboarding.OnboardingService;
import com.example.echo_api.service.auth.onboarding.OnboardingServiceImpl;
import com.example.echo_api.service.session.SessionService;

/**
 * Unit test class for {@link OnboardingService}.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private SessionService sessionSerivce;

    @InjectMocks
    private OnboardingServiceImpl authService;

    private static Account account;

    /**
     * Set up a {@link Account} object before each test.
     * 
     */
    @BeforeAll
    static void initVariables() {
        account = new Account("testUsername", "testPassword");
    }

    /**
     * This test ensures that the {@link OnboardingService#login(String, String)}
     * method does not throw any exceptions.
     */
    @Test
    void AuthService_Login_ReturnVoid() {
        // arrange
        LoginDTO login = new LoginDTO(account.getUsername(), account.getPassword());

        doNothing()
            .when(sessionSerivce)
            .authenticate(login.username(), login.password());

        // act & assert
        assertDoesNotThrow(() -> authService.login(login));
        verify(sessionSerivce, times(1)).authenticate(login.username(), login.password());
    }

    /**
     * This test ensures that {@link OnboardingService#login(String, String)} method
     * throws {@link DisabledException}.
     */
    @Test
    void AuthService_Login_ThrowDisabledException() {
        // arrange
        LoginDTO login = new LoginDTO(account.getUsername(), account.getPassword());

        doThrow(new DisabledException(""))
            .when(sessionSerivce)
            .authenticate(login.username(), login.password());

        // act & assert
        assertThrows(DisabledException.class, () -> authService.login(login));
        verify(sessionSerivce, times(1)).authenticate(login.username(), login.password());
    }

    /**
     * This test ensures that {@link OnboardingService#login(String, String)} method
     * throws {@link LockedException}.
     */
    @Test
    void AuthService_Login_ThrowLockedException() {
        // arrange
        LoginDTO login = new LoginDTO(account.getUsername(), account.getPassword());

        doThrow(new LockedException(""))
            .when(sessionSerivce)
            .authenticate(login.username(), login.password());

        // act & assert
        assertThrows(LockedException.class, () -> authService.login(login));
        verify(sessionSerivce, times(1)).authenticate(login.username(), login.password());
    }

    /**
     * This test ensures that {@link OnboardingService#login(String, String)} method
     * throws {@link BadCredentialsException}.
     */
    @Test
    void AuthService_Login_ThrowBadCredentialsException() {
        // arrange
        LoginDTO login = new LoginDTO(account.getUsername(), account.getPassword());

        doThrow(new BadCredentialsException(""))
            .when(sessionSerivce)
            .authenticate(login.username(), login.password());

        // act & assert
        assertThrows(BadCredentialsException.class, () -> authService.login(login));
        verify(sessionSerivce, times(1)).authenticate(login.username(), login.password());
    }

    /**
     * This test ensures that {@link OnboardingService#signup(String, String)}
     * method does not throw any exceptions.
     */
    @Test
    void AuthService_Signup_ReturnVoid() {
        // arrange
        SignupDTO signup = new SignupDTO(account.getUsername(), account.getPassword());

        when(accountService.register(account.getUsername(), account.getPassword())).thenReturn(account);
        doNothing()
            .when(sessionSerivce)
            .authenticate(signup.username(), signup.password());

        // act & assert
        assertDoesNotThrow(() -> authService.signup(signup));
        verify(accountService, times(1)).register(account.getUsername(), account.getPassword());
        verify(sessionSerivce, times(1)).authenticate(signup.username(), signup.password());
    }

    /**
     * This test ensures that {@link OnboardingService#signup(String, String)}
     * method throws {@link UsernameAlreadyExistsException}.
     */
    @Test
    void AuthService_Signup_ThrowUsernameAlreadyExists() {
        // arrange
        SignupDTO signup = new SignupDTO(account.getUsername(), account.getPassword());

        when(accountService.register(account.getUsername(), account.getPassword()))
            .thenThrow(new UsernameAlreadyExistsException());

        // act & assert
        assertThrows(UsernameAlreadyExistsException.class, () -> authService.signup(signup));
        verify(accountService, times(1)).register(account.getUsername(), account.getPassword());
        verify(sessionSerivce, never()).authenticate(signup.username(), signup.password());
    }

    /**
     * This test ensures that {@link OnboardingService#signup(String, String)}
     * method throws {@link DisabledException}.
     */
    @Test
    void AuthService_Signup_ThrowDisabledException() {
        // arrange
        SignupDTO signup = new SignupDTO(account.getUsername(), account.getPassword());

        when(accountService.register(account.getUsername(), account.getPassword())).thenReturn(account);
        doThrow(new DisabledException(""))
            .when(sessionSerivce)
            .authenticate(signup.username(), signup.password());

        // act & assert
        assertThrows(DisabledException.class, () -> authService.signup(signup));
        verify(accountService, times(1)).register(account.getUsername(), account.getPassword());
        verify(sessionSerivce, times(1)).authenticate(signup.username(), signup.password());
    }

    /**
     * This test ensures that {@link OnboardingService#signup(String, String)}
     * method throws {@link LockedException}.
     */
    @Test
    void AuthService_Signup_ThrowLockedException() {
        // arrange
        SignupDTO signup = new SignupDTO(account.getUsername(), account.getPassword());

        when(accountService.register(account.getUsername(), account.getPassword())).thenReturn(account);
        doThrow(new LockedException(""))
            .when(sessionSerivce)
            .authenticate(signup.username(), signup.password());

        // act & assert
        assertThrows(LockedException.class, () -> authService.signup(signup));
        verify(accountService, times(1)).register(account.getUsername(), account.getPassword());
        verify(sessionSerivce, times(1)).authenticate(signup.username(), signup.password());
    }

    /**
     * This test ensures that {@link OnboardingService#signup(String, String)}
     * method throws {@link BadCredentialsException}.
     */
    @Test
    void AuthService_Signup_ThrowBadCredentialsException() {
        // arrange
        SignupDTO signup = new SignupDTO(account.getUsername(), account.getPassword());

        when(accountService.register(account.getUsername(), account.getPassword())).thenReturn(account);
        doThrow(new BadCredentialsException(""))
            .when(sessionSerivce)
            .authenticate(signup.username(), signup.password());

        // act & assert
        assertThrows(BadCredentialsException.class, () -> authService.signup(signup));
        verify(accountService, times(1)).register(account.getUsername(), account.getPassword());
        verify(sessionSerivce, times(1)).authenticate(signup.username(), signup.password());
    }

}
