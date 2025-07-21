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
import com.example.echo_api.validation.account.annotations.Password;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Unit test class for {@link Password} annotation.
 */
class PasswordAnnotationTest {

    private static Validator validator;
    private static List<String> validPasswords;
    private static List<String> invalidPasswords;

    // Dummy class for validation
    static class TestUser {

        @Password
        private String password;

        public TestUser(String password) {
            this.password = password;
        }

    }

    @BeforeAll
    static void setup() throws Exception {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        validPasswords = readFileFromResources("data/valid_passwords.txt");
        invalidPasswords = readFileFromResources("data/invalid_passwords.txt");
    }

    /**
     * Test ensures that the {@link Password} annotation does not return a
     * constraint violation for all 100 items present in {@code validPasswords}.
     */
    @Test
    void validPasswordsShouldPass() {
        for (String password : validPasswords) {
            TestUser user = new TestUser(password);
            Set<ConstraintViolation<TestUser>> violations = validator.validate(user);

            assertTrue(violations.isEmpty(), "Valid password failed validation: " + password);
        }
    }

    /**
     * Test ensures that the {@link Password} annotation returns a constraint
     * violation for all 100 items present in {@code invalidPasswords}.
     */
    @Test
    void invalidPasswordsShouldFail() {
        for (String password : invalidPasswords) {
            TestUser user = new TestUser(password);
            Set<ConstraintViolation<TestUser>> violations = validator.validate(user);

            assertFalse(violations.isEmpty(), "Invalid password passed validation: " + password);
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
