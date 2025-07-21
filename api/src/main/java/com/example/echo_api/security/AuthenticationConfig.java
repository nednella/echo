package com.example.echo_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.echo_api.persistence.model.account.SecurityAccount;
import com.example.echo_api.persistence.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

/**
 * Configuration class for Spring Security.
 * 
 * <p>
 * The class configures the core security components for the application,
 * integrating Spring Security with JPA for account authentication and password
 * management.
 * 
 * <p>
 * The class defines beans to handle account authentication, password encoding,
 * and the creation of an {@link AuthenticationManager} to handle the
 * authentication process.
 */
@Configuration
@RequiredArgsConstructor
public class AuthenticationConfig {

    private final AccountRepository accountRepository;

    /**
     * Configures a {@link UserDetailsService} that integrates Spring Security
     * authentication with the {@link AccountRepository}.
     * 
     * <p>
     * The service is responsible for querying the database via JPA to fetch account
     * details for authentication purposes.
     * 
     * @return A {@link UserDetailsService} implementation that retrieves account
     *         information from the database.
     * @throws UserNotFoundException If no account is found with the provided
     *                               username.
     */
    UserDetailsService jpaUserDetailsService() {
        return username -> accountRepository
            .findByUsername(username)
            .map(SecurityAccount::new)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    /**
     * Configures a {@link PasswordEncoder} that uses the BCrypt hashing algorithm
     * to encode and validate passwords.
     * 
     * @return A {@link BCryptPasswordEncoder} instance.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures an {@link AuthenticationManager} that handles the authentication
     * process by using {@link DaoAuthenticationProvider}.
     * 
     * <p>
     * The {@link DaoAuthenticationProvider} is configured to use the custom
     * {@link UserDetailsService} and {@link PasswordEncoder} defined in this class.
     * 
     * @return An {@link AuthenticationManager} that is automatically picked up by
     *         Spring Security to manage authentication requests.
     */
    @Bean
    AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(jpaUserDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

}
