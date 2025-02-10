package com.example.echo_api.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.echo_api.exception.custom.password.IncorrectCurrentPasswordException;
import com.example.echo_api.exception.custom.username.UsernameAlreadyExistsException;
import com.example.echo_api.persistence.dto.request.account.UpdatePasswordDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.repository.AccountRepository;
import com.example.echo_api.persistence.repository.MetricsRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.account.AccountService;
import com.example.echo_api.service.account.AccountServiceImpl;
import com.example.echo_api.service.session.SessionService;

/**
 * Unit test class for {@link AccountService}.
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private MetricsRepository metricsRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    /**
     * Sets up a {@link Account} object before each test.
     */
    @BeforeEach
    public void initUser() {
        account = new Account("testUsername", "testPassword");
    }

    /**
     * Test ensures that the {@link AccountServiceImpl#register(String, String)}
     * method correctly creates a new account when the username does not already
     * exist.
     */
    @Test
    void AccountService_Register_ReturnAccount() {
        // arrange
        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.existsByUsername(account.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // act
        Account registered = accountService.register(account.getUsername(), account.getPassword());

        // assert
        assertEquals(account, registered);
        verify(accountRepository, times(1)).save(account);
        verify(passwordEncoder, times(1)).encode(account.getPassword());
    }

    /**
     * Test ensures that the {@link AccountServiceImpl#register(String, String)}
     * method throws a {@link UsernameAlreadyExistsException} when the username
     * already exists.
     */
    @Test
    void AccountService_Register_ThrowUsernameAlreadyExists() {
        // arrange
        when(accountRepository.existsByUsername(account.getUsername())).thenReturn(true);

        // act & assert
        assertThrows(UsernameAlreadyExistsException.class,
            () -> accountService.register(account.getUsername(), account.getPassword()));
        verify(accountRepository, times(1)).existsByUsername(account.getUsername());
    }

    /**
     * Test ensures that the {@link AccountServiceImpl#isUsernameAvailable(String)}
     * method returns true when the username exists in the repository.
     */
    @Test
    void AccountService_IsUsernameAvailable_ReturnTrue() {
        // arrange
        when(accountRepository.existsByUsername(account.getUsername())).thenReturn(true);

        // act
        boolean taken = accountService.isUsernameAvailable(account.getUsername());

        // assert
        assertFalse(taken);
        verify(accountRepository, times(1)).existsByUsername(account.getUsername());
    }

    /**
     * Test ensures that the {@link AccountServiceImpl#existsByUsername(String)}
     * method returns false when the username does not exist in the repository.
     */
    @Test
    void AccountService_IsUsernameAvailable_ReturnFalse() {
        // arrange
        when(accountRepository.existsByUsername(account.getUsername())).thenReturn(false);

        // act
        boolean taken = accountService.isUsernameAvailable(account.getUsername());

        // assert
        assertTrue(taken);
        verify(accountRepository, times(1)).existsByUsername(account.getUsername());
    }

    /**
     * Test ensures that the {@link AccountServiceImpl#updateUsername(String)}
     * method succeeds when the username does not already exist.
     */
    @Test
    void AccountService_UpdateUsername_Success() {
        // arrange
        when(accountRepository.existsByUsername("new_username")).thenReturn(false);
        when(sessionService.getAuthenticatedUser()).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(account);

        // act
        accountService.updateUsername("new_username");

        // assert
        assertEquals("new_username", account.getUsername());
        verify(accountRepository, times(1)).existsByUsername("new_username");
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(accountRepository, times(1)).save(account);
    }

    /**
     * Test ensures that the {@link AccountServiceImpl#updateUsername(String)}
     * methodthrows a {@link UsernameAlreadyExistsException} when the username
     * already exists.
     */
    @Test
    void AccountService_UpdateUsername_ThrowUsernameAlreadyExists() {
        // arrange
        when(accountRepository.existsByUsername("new_username")).thenReturn(true);

        // act & assert
        assertThrows(UsernameAlreadyExistsException.class,
            () -> accountService.updateUsername("new_username"));
        verify(accountRepository, times(1)).existsByUsername("new_username");
    }

    /**
     * Test ensures that the
     * {@link AccountServiceImpl#updatePassword(String, String)} method succeeds
     * when the request is valid.
     */
    @Test
    void AccountService_UpdatePassword_Success() {
        // arrange
        UpdatePasswordDTO request = new UpdatePasswordDTO(
            "current",
            "new",
            "new");

        String oldPassword = account.getPassword();

        when(sessionService.getAuthenticatedUser()).thenReturn(account);
        when(passwordEncoder.matches("current", oldPassword)).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("encodedPassword");
        when(accountRepository.save(account)).thenReturn(account);

        // act
        accountService.updatePassword(request.currentPassword(), request.newPassword());

        // assert
        assertEquals("encodedPassword", account.getPassword());
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(passwordEncoder, times(1)).matches("current", oldPassword);
        verify(passwordEncoder, times(1)).encode("new");
        verify(accountRepository, times(1)).save(account);
    }

    /**
     * Test ensures that the
     * {@link AccountServiceImpl#updatePassword(String, String)} method throws
     * {@link IncorrectCurrentPasswordException} when the supplied current and the
     * stored existing passwords do not match.
     */
    @Test
    void AccountService_UpdatePassword_ThrowIncorrectCurrentPassword() {
        // arrange
        UpdatePasswordDTO request = new UpdatePasswordDTO(
            "wrong_password",
            "new",
            "new");

        when(sessionService.getAuthenticatedUser()).thenReturn(account);
        when(passwordEncoder.matches("wrong_password", account.getPassword())).thenReturn(false);

        // act & assert
        assertThrows(IncorrectCurrentPasswordException.class,
            () -> accountService.updatePassword(request.currentPassword(), request.newPassword()));
        verify(sessionService, times(1)).getAuthenticatedUser();
        verify(passwordEncoder, times(1)).matches("wrong_password", account.getPassword());
    }

}
