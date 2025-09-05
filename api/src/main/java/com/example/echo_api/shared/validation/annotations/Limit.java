package com.example.echo_api.shared.validation.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.example.echo_api.shared.validation.validators.LimitValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom Jakarta Validation annotation for pagination query parameters.
 *
 * <p>
 * The annotated {@link Integer} must meet the requirements for a valid
 * {@code limit} parameter in pagination.
 *
 * <p>
 * This annotation is intended to be used on method parameters or fields of type
 * {@link Integer}. When applied, it triggers the validation logic defined in
 * the associated {@link LimitValidator} class.
 *
 * <p>
 * Example usage:
 * 
 * <pre>
 * {@code
 * GetMapping("/items")
 * public ResponseEntity<PageDTO<ItemDTO>> getItems(
 *     RequestParam int offset,
 *     RequestParam @Limit int limit
 * ) {
 *     // Method implementation
 * }
 * </pre>
 *
 * <p>
 * If the validation fails, a {@link ConstraintViolationException} will be
 * thrown.
 */
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = LimitValidator.class)
public @interface Limit {

    /**
     * @return the error message template
     */
    String message() default "Limit must be in the range 1 to 50";

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};

}
