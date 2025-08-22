package com.example.echo_api.integration.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.echo_api.config.SvixTestConfig;
import com.example.echo_api.persistence.dto.adapter.ClerkUserDTO;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.service.dev.DevService;

@ActiveProfiles(value = "test")
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { SvixTestConfig.class }) // Ensure default SvixConfig is NOT loaded
public abstract class IntegrationTest {

    protected static final String AUTH_USER_USERNAME = "auth_user";
    protected static final String MOCK_USER_USERNAME = "mock_user";

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected AuthorizationHeaderInterceptor authorizationHeaderInterceptor;

    @Autowired
    private ClerkTestUtils clerkTestUtils;

    @Autowired
    private DevService devService;

    protected User authUser;
    protected User mockUser;

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
     * Initialise the integration test environment:
     * 
     * <ul>
     * <li>Append Authorization header interceptor to the rest template
     * <li>Create Clerk users & sync to the local database
     * <li>Mark a Clerk user as having completed the onboarding process
     * <li>Obtain a bearer token for that user to send authenticated requests
     * </ul>
     */
    @BeforeAll
    void integrationTestSetup() {
        restTemplate
            .getRestTemplate()
            .getInterceptors()
            .add(authorizationHeaderInterceptor);

        authUser = createTestUser("test1@echo.app", AUTH_USER_USERNAME);
        mockUser = createTestUser("test2@echo.app", MOCK_USER_USERNAME);

        clerkTestUtils.completeOnboarding(authUser.getExternalId(), authUser.getId().toString());

        String token = clerkTestUtils.getSessionTokenForUser(authUser.getExternalId());
        authorizationHeaderInterceptor.setToken(token);
    }

    /**
     * Clean up the integration test environment:
     * 
     * <ul>
     * <li>Remove any Clerk users that were crated as part of the test suite
     * </ul>
     */
    @AfterAll
    void integrationTestCleanup() {
        clerkTestUtils.deleteUser(authUser.getExternalId());
        clerkTestUtils.deleteUser(mockUser.getExternalId());
    }

    /**
     * Create a test user.
     * 
     * <p>
     * Create a user in the Clerk db with the provided {@code email} and
     * {@code username} and sync that user to the local db by mapping to a
     * {@link User} entity.
     * 
     * @param email    the email for the Clerk user
     * @param username the username for the Clerk user
     * @return the persisted {@link User} entity
     */
    private User createTestUser(String email, String username) {
        ClerkUserDTO clerkUser = clerkTestUtils.createUser(email, username);
        return devService.persistClerkUser(clerkUser);
    }

}
