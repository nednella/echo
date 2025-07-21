package com.example.echo_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;
    private final FilterChainExceptionHandler filterChainExceptionHandler;

    // @formatter:off
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(fl -> fl.disable())
            .logout(lo -> lo
                .logoutUrl("/api/v1/auth/logout")
                .logoutSuccessHandler(customLogoutSuccessHandler)
                .deleteCookies("ECHO_SESSION")
            )
            .sessionManagement(sm -> sm
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation(sf -> sf.migrateSession())
            )
            .securityContext(sc -> sc.requireExplicitSave(true))
            .exceptionHandling(ex -> ex.disable())
            .addFilterBefore(filterChainExceptionHandler, LogoutFilter.class);

        return http.build();
    }
    // @formatter:on  

    /**
     * Configure the SecurityContextRepository to store security context in HTTP
     * sessions managed by Redis for persistence.
     * 
     * <p>
     * This declaration is required due to the implementation of a custom login
     * mechanism.
     * 
     * <p>
     * For more information, refer to:
     * {@link https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html#store-authentication-manually}
     *
     * @return HttpSessionSecurityContextRepository
     */
    @Bean
    SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

}