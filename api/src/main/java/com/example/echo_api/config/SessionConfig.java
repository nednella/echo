package com.example.echo_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * Configuration class for Spring Session.
 * 
 * <p>
 * For more information, refer to:
 * {@link https://docs.spring.io/spring-session/reference/configuration/redis.html}.
 * 
 * <p>
 * If interested in implementing custom logout handling such as logout
 * everywhere, see:
 * {@link https://docs.spring.io/spring-session/reference/configuration/redis.html#finding-all-user-sessions}.
 * 
 */
@Configuration
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {

    /**
     * Customise the session cookie.
     *
     * @return CookieSerializer
     */
    @Bean
    CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("ECHO_SESSION");
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return serializer;
    }

}
