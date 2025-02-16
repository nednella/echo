package com.example.echo_api.unit.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.echo_api.validation.pagination.annotations.Offset;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

/**
 * Unit test class for {@link Offset} annotation.
 */
class OffsetAnnotationTest {

    private static Validator validator;

    // Dummy class for validation
    static class TestOffset {

        @Offset
        private int offset;

        public TestOffset(int offset) {
            this.offset = offset;
        }

    }

    @BeforeAll
    static void setup() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    /**
     * Test ensures that the {@link Offset} annotation does not return a constraint
     * violation for all 5 items present in {@code offsets}.
     */
    @Test
    void validOffsetsShouldPass() {
        List<Integer> offsets = List.of(0, 10, 25, 50, 10000);

        for (int offset : offsets) {
            TestOffset test = new TestOffset(offset);
            Set<ConstraintViolation<TestOffset>> violations = validator.validate(test);

            assertTrue(violations.isEmpty(), "Valid offset failed validation: " + offset);
        }
    }

    /**
     * Test ensures that the {@link Offset} annotation returns a constraint
     * violation for all 5 items present in {@code offsets}.
     */
    @Test
    void invalidOffsetsShouldFail() {
        List<Integer> offsets = List.of(-10000, -50, -25, -10, -1);

        for (int offset : offsets) {
            TestOffset test = new TestOffset(offset);
            Set<ConstraintViolation<TestOffset>> violations = validator.validate(test);

            assertFalse(violations.isEmpty(), "Invalid offset passed validation: " + offset);
        }
    }

}
