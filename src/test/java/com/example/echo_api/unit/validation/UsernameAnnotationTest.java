package com.example.echo_api.unit.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.echo_api.unit.config.RegexConfigTest;
import com.example.echo_api.validation.account.annotations.Username;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Unit test class for {@link Username} annotation.
 */
class UsernameAnnotationTest {

    private static Validator validator;
    private static List<String> validUsernames;
    private static List<String> invalidUsernames;

    // Dummy class for validation
    static class TestUser {

        @Username
        private String username;

        public TestUser(String username) {
            this.username = username;
        }

    }

    @BeforeAll
    static void setup() throws Exception {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        validUsernames = readFileFromResources("data/valid_usernames.txt");
        invalidUsernames = readFileFromResources("data/invalid_usernames.txt");
    }

    /**
     * Test ensures that the {@link Username} annotation does not return a
     * constraint violation for all 100 items present in {@code validUsernames}.
     */
    @Test
    void validUsernamesShouldPass() {
        for (String username : validUsernames) {
            TestUser user = new TestUser(username);
            Set<ConstraintViolation<TestUser>> violations = validator.validate(user);

            assertTrue(violations.isEmpty(), "Valid username failed validation: " + username);
        }
    }

    /**
     * Test ensures that the {@link Username} annotation returns a constraint
     * violation for all 100 items present in {@code invalidUsernames}.
     */
    @Test
    void invalidUsernamesShouldFail() {
        for (String username : invalidUsernames) {
            TestUser user = new TestUser(username);
            Set<ConstraintViolation<TestUser>> violations = validator.validate(user);

            assertFalse(violations.isEmpty(), "Invalid username passed validation: " + username);
        }
    }

    /**
     * Reads a file from the resources directory and returns its content as a list
     * of strings.
     *
     * @param fileName name of the file to read
     * @return a list of strings representing the contents
     * @throws Exception if the file is not found or an error occurs while reading
     */
    private static List<String> readFileFromResources(String fileName) throws Exception {
        InputStream inputStream = RegexConfigTest.class.getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new FileNotFoundException("File not found in resources: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().toList();
        }
    }

}
