package com.example.echo_api.persistence.model.account;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

/**
 * The {@code SecurityUser} class extends the default Spring Security
 * authentication model by implementing both {@link UserDetails}.
 * 
 * <p>
 * Instances of this class are created with an {@link Account} object.
 * 
 * <p>
 * This class acts as a wrapper for the {@link Account} in the Spring Security
 * authentication flow, representing an authenticated user retrieved from the
 * database.
 * 
 */
@RequiredArgsConstructor
public class SecurityAccount implements UserDetails {

    private final Account account;

    public Account getAccount() {
        return account;
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(account.getRole().toString()));
    }

    @Override
    public boolean isEnabled() {
        return account.isEnabled();
    }

}
