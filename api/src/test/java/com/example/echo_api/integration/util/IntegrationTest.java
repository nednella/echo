package com.example.echo_api.integration.util;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.echo_api.integration.util.ClerkTestUtils.Template;
import com.example.echo_api.modules.clerk.dto.sdk.ClerkUserDTO;
import com.example.echo_api.persistence.model.user.User;
import com.example.echo_api.service.dev.DevService;

/**
 * Base class for full-stack integration tests.
 * 
 * <ul>
 * <li>runs with the "test" profile and Testcontainers
 * <li>boots the application with a random port
 * <li>uses {@link WebTestClient} for fluent assertions
 * <li>import test-only beans (DatabaseCleaner, SvixTestConfig)
 * </ul>
 */
@ActiveProfiles(value = "test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Import(DatabaseCleaner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class IntegrationTest {

    protected static final String AUTH_USER_USERNAME = "auth_user";
    protected static final String MOCK_USER_USERNAME = "mock_user";

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    protected DatabaseCleaner cleaner;

    @Autowired
    protected WebTestClient authenticatedClient;

    @Autowired
    protected WebTestClient unauthenticatedClient;

    @Autowired
    protected ClerkTestUtils clerkTestUtils;

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
     * <li>Create Clerk users & sync to the local database
     * <li>Mark a Clerk user as having completed the onboarding process
     * <li>Obtain a bearer token for that user to send authenticated requests
     * <li>Build an authenticated testing client with a default AUTHORIZATION header
     * </ul>
     */
    @BeforeAll
    void integrationTestSetup() {
        authUser = createTestUser("test1@echo.app", AUTH_USER_USERNAME);
        mockUser = createTestUser("test2@echo.app", MOCK_USER_USERNAME);

        clerkTestUtils.completeOnboarding(authUser.getExternalId(), authUser.getId().toString());
        String token = clerkTestUtils.getSessionTokenFromTemplate(authUser.getExternalId(), Template.VALID_TOKEN);

        authenticatedClient = authenticatedClient.mutate()
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .build();
    }

    /**
     * Clean up the integration test environment:
     * 
     * <ul>
     * <li>Remove any Clerk users that were created as part of the test suite
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
