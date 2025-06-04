package com.example.echo_api.integration.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.echo_api.config.ApiConfig;
import com.example.echo_api.persistence.dto.request.auth.LoginDTO;
import com.example.echo_api.persistence.dto.request.auth.SignupDTO;
import com.example.echo_api.persistence.model.account.Account;
import com.example.echo_api.persistence.repository.AccountRepository;
import com.redis.testcontainers.RedisContainer;

@ActiveProfiles(value = "test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class IntegrationTest {

    private static final String AUTH_USER_USERNAME = "test1";
    private static final String OTHER_USER_USERNAME = "test2";
    private static final String TEST_ENV_PASSWORD = "password1";

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Container
    @ServiceConnection
    protected static RedisContainer redis = new RedisContainer("redis:latest");

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected SessionCookieInterceptor sessionCookieInterceptor;

    @Autowired
    private AccountRepository accountRepository;

    protected Account authenticatedUser;
    protected Account otherUser;

    /**
     * Initialise the integration test environment:
     * <ul>
     * <li>Configure {@link TestRestTemplate} with
     * {@link SessionCookieInterceptor}.</li>
     * <li>Configure a test {@link Account} for integration testing.</li>
     * <li>Obtaining an authenticated session for the test {@link Account}.</li>
     * </ul>
     */
    @BeforeAll
    void integrationTestSetup() {
        // Configure rest template
        restTemplate
            .getRestTemplate()
            .getInterceptors()
            .add(sessionCookieInterceptor);

        // create test environment accounts
        createUser(AUTH_USER_USERNAME, TEST_ENV_PASSWORD);
        createUser(OTHER_USER_USERNAME, TEST_ENV_PASSWORD);

        // authenticate a test account
        authenticateUser(AUTH_USER_USERNAME, TEST_ENV_PASSWORD);

        // fetch test account entities
        authenticatedUser = fetchUser(AUTH_USER_USERNAME);
        otherUser = fetchUser(OTHER_USER_USERNAME);
    }

    /**
     * Test ensures that the {@code postgres} container is initialised and running
     * correctly.
     */
    @Test
    void postgresConnectionEstablished() {
        assertTrue(postgres.isCreated());
        assertTrue(postgres.isRunning());
    }

    /**
     * Test ensures that the {@code redis} container is initialised and running
     * correctly.
     */
    @Test
    void redisConnectionEstablished() {
        assertTrue(redis.isCreated());
        assertTrue(redis.isRunning());
    }

    /**
     * Registers an account under the supplied {@code username} and {@code password}
     * by sending a POST request to the signup endpoint.
     * 
     * <p>
     * Authentication from the signup request is ignored by disabling
     * {@link SessionCookieInterceptor}.
     * 
     * @param username The username of the account to register.
     * @param password The password of the account to register.
     */
    private void createUser(String username, String password) {
        // api: POST /api/v1/auth/signup ==> 204 : No Content
        String path = ApiConfig.Auth.SIGNUP;
        SignupDTO body = new SignupDTO(username, password);
        HttpEntity<SignupDTO> request = TestUtils.createJsonRequestEntity(body);

        sessionCookieInterceptor.disable();
        ResponseEntity<Void> response = restTemplate.postForEntity(path, request, Void.class);
        sessionCookieInterceptor.enable();

        System.out.println("creating user: " + username);
        assertEquals(NO_CONTENT, response.getStatusCode());
    }

    /**
     * Authenticates an account with the supplied {@code username} and
     * {@code password} by sending a POST request to the login endpoint.
     * 
     * <p>
     * Authentication is stored using {@link SessionCookieInterceptor} and
     * subsequently attached to any HTTP request headers as part of the testing
     * environment.
     * 
     * @param username The username of the account to authenticate.
     * @param password The password of the account to authenticate.
     */
    private void authenticateUser(String username, String password) {
        // api: POST /api/v1/auth/login ==> 204 : No Content
        String path = ApiConfig.Auth.LOGIN;
        LoginDTO body = new LoginDTO(username, password);
        HttpEntity<LoginDTO> request = TestUtils.createJsonRequestEntity(body);

        ResponseEntity<Void> response = restTemplate.postForEntity(path, request, Void.class);

        assertEquals(NO_CONTENT, response.getStatusCode());
        TestUtils.assertSetCookieStartsWith(response, "ECHO_SESSION");
    }

    /**
     * Retrieves the {@link Account} associated to the supplied {@code username} to
     * enable allow within test environment methods.
     * 
     * @param username The username of the account to fetch.
     * @return The corresponding {@link Account} entity.
     */
    private Account fetchUser(String username) {
        return accountRepository.findByUsername(username)
            .orElseThrow(() -> new IllegalStateException("User: " + username + " could not be found."));
    }

}
