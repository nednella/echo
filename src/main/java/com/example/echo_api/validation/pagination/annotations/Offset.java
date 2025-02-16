package com.example.echo_api.validation.pagination.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.validation.pagination.validators.OffsetValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom Jakarta Validation annotation for pagination query parameters.
 *
 * <p>
 * The annotated {@link Integer} must meet the requirements for a valid
 * {@code offset} parameter in pagination.
 *
 * <p>
 * This annotation is intended to be used on method parameters or fields of type
 * {@link Integer}. When applied, it triggers the validation logic defined in
 * the associated {@link OffsetValidator} class.
 *
 * <p>
 * Example usage:
 * 
 * <pre>
 * {@code
 * GetMapping("/items")
 * public ResponseEntity<PageDTO<ItemDTO>> getItems(
 *     RequestParam int limit,
 *     RequestParam @Offset int offset
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
@Constraint(validatedBy = OffsetValidator.class)
public @interface Offset {

    /**
     * @return the error message template
     */
    String message() default ValidationMessageConfig.INVALID_OFFSET;

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};

}
