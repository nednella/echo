package com.example.echo_api.shared.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.echo_api.shared.validation.pagination.annotations.Limit;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Unit test class for {@link Limit} annotation.
 */
class LimitValidationTest {

    private static Validator validator;

    // Dummy class for validation
    static class TestLimit {

        @Limit
        private int limit;

        public TestLimit(int limit) {
            this.limit = limit;
        }

    }

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * Test ensures that the {@link Limit} annotation does not return a constraint
     * violation for all 5 items present in {@code limits}.
     */
    @Test
    void validLimitsShouldPass() {
        List<Integer> limits = List.of(1, 5, 10, 25, 50);

        for (int limit : limits) {
            TestLimit test = new TestLimit(limit);
            Set<ConstraintViolation<TestLimit>> violations = validator.validate(test);

            assertTrue(violations.isEmpty(), "Valid limit failed validation: " + limit);
        }
    }

    /**
     * Test ensures that the {@link Limit} annotation returns a constraint violation
     * for all 5 items present in {@code limits}.
     */
    @Test
    void invalidLimitsShouldFail() {
        List<Integer> limits = List.of(-10000, -50, 0, 51, 10000);

        for (int limit : limits) {
            TestLimit test = new TestLimit(limit);
            Set<ConstraintViolation<TestLimit>> violations = validator.validate(test);

            assertFalse(violations.isEmpty(), "Invalid limit passed validation: " + limit);
        }
    }

}
