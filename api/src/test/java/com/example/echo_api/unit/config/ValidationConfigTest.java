package com.example.echo_api.unit.config;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.hibernate.validator.HibernateValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.echo_api.validation.account.annotations.Password;
import com.example.echo_api.validation.account.annotations.Username;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class ValidationConfigTest {

    private static Validator defaultValidator;
    private static Validator failFastValidator;
    private static TestUser testUser;

    // dummy class for validation
    static class TestUser {

        @Username
        private String username;

        @Password
        private String password;

        public TestUser(String username, String password) {
            this.username = username;
            this.password = password;
        }

    }

    @BeforeAll
    static void setup() {
        // configure a default validator
        defaultValidator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();

        // configure a fail-fast validator
        failFastValidator = Validation
            .byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory()
            .getValidator();

        // configure an invalid object with multiple validation failures
        testUser = new TestUser(
            "invalid-username!",
            "invalid@password");
    }

    @Test
    void ValidationConfig_Default_ReturnMultipleViolations() {
        Set<ConstraintViolation<TestUser>> violations = defaultValidator.validate(testUser);
        assertEquals(2, violations.size());
    }

    @Test
    void ValidationConfig_FailFast_ReturnOneViolation() {
        Set<ConstraintViolation<TestUser>> violations = failFastValidator.validate(testUser);
        assertEquals(1, violations.size());
    }

}
