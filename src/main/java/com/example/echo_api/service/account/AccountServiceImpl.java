package com.example.echo_api.service.account;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.echo_api.exception.custom.password.IncorrectCurrentPasswordException;
import com.example.echo_api.exception.custom.username.UsernameAlreadyExistsException;
import com.example.echo_api.persistence.model.account.Role;
import com.example.echo_api.persistence.model.profile.Profile;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.repository.AccountRepository;
import com.example.echo_api.persistence.repository.ProfileRepository;
import com.example.echo_api.service.session.SessionService;

import lombok.RequiredArgsConstructor;

/**
 * Service implementation for managing CRUD operations of a {@link Account}.
 * 
 * @see ProfileService
 * @see SessionService
 * @see AccountRepository
 * @see PasswordEncoder
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final SessionService sessionService;

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean existsById(UUID id) {
        return accountRepository.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public Account register(String username, String password) throws UsernameAlreadyExistsException {
        return registerWithRole(username, password, Role.USER);
    }

    @Override
    public Account registerWithRole(String username, String password, Role role) throws UsernameAlreadyExistsException {
        if (existsByUsername(username)) {
            throw new UsernameAlreadyExistsException();
        }

        Account account = new Account(username, passwordEncoder.encode(password), role);
        accountRepository.save(account);

        Profile profile = new Profile(account.getId(), account.getUsername());
        profileRepository.save(profile);

        return account;
    }

    @Override
    public void updateUsername(String username) throws UsernameAlreadyExistsException {
        if (existsByUsername(username)) {
            throw new UsernameAlreadyExistsException();
        }

        Account me = getMe();
        me.setUsername(username);

        accountRepository.save(me);
        sessionService.reauthenticate(me);
    }

    @Override
    public void updatePassword(String currentPassword, String newPassword) throws IncorrectCurrentPasswordException {
        Account me = getMe();

        if (!passwordEncoder.matches(currentPassword, me.getPassword())) {
            throw new IncorrectCurrentPasswordException();
        }

        me.setPassword(passwordEncoder.encode(newPassword));

        accountRepository.save(me);
        sessionService.reauthenticate(me);
    }

    /**
     * Internal method for obtaining an {@link Account} associated to the
     * authenticated user.
     * 
     * @return The associated {@link Account} entity.
     */
    private Account getMe() {
        return sessionService.getAuthenticatedUser();
    }

}
