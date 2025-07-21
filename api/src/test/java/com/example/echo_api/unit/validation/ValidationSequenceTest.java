package com.example.echo_api.unit.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.validation.account.annotations.Password;
import com.example.echo_api.validation.account.annotations.Username;
import com.example.echo_api.validation.sequence.Advanced;
import com.example.echo_api.validation.sequence.Basic;
import com.example.echo_api.validation.sequence.ValidationSequence;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;

class ValidationSequenceTest {

    private static Validator validator;

    // dummy class for validation
    static class TestUser {

        @NotNull(message = "Username is required.", groups = Basic.class)
        @Username(groups = Advanced.class)
        private String username;

        @NotNull(message = "Password is required.", groups = Basic.class)
        @Password(groups = Advanced.class)
        private String password;

        public TestUser(String username, String password) {
            this.username = username;
            this.password = password;
        }

    }

    @BeforeAll
    static void setup() {
        validator = Validation
            .buildDefaultValidatorFactory()
            .getValidator();
    }

    @Test
    void ValidationSequence_BasicCheckFailsFirst() {
        // configure object consisting of 1 invalid basic and 1 invalid advanced check
        TestUser invalidUser = new TestUser(
            null,
            "invalid-password");

        Set<ConstraintViolation<TestUser>> violations = validator.validate(invalidUser, ValidationSequence.class);
        assertEquals(1, violations.size()); // fails on null username

        ConstraintViolation<TestUser> violation = violations.iterator().next();
        assertEquals("Username is required.", violation.getMessage());
    }

    @Test
    void ValidationSequence_AdvancedCheckFailsAfterBasicChecksPass() {
        // configure object consisting of 1 invalid advanced check
        TestUser invalidUser = new TestUser(
            "valid_username",
            "invalid-password");

        Set<ConstraintViolation<TestUser>> violations = validator.validate(invalidUser, ValidationSequence.class);
        assertEquals(1, violations.size()); // fails on invalid password

        ConstraintViolation<TestUser> violation = violations.iterator().next();
        assertEquals(ValidationMessageConfig.INVALID_PASSWORD, violation.getMessage());
    }

}
