package com.example.echo_api.validation.account.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.validation.account.validators.ConfirmationPasswordMatchValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom Jakarta Validation annotation for {@link UpdatePasswordDTO} forms.
 * 
 * <p>
 * The annotated {@link UpdatePasswordDTO} record fields
 * {@code confirmationPassword} and {@code newPassword} must match.
 * 
 * <p>
 * Accepts type annotations for {@link UpdatePasswordDTO}.
 */
@Target({ TYPE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = ConfirmationPasswordMatchValidator.class)
public @interface ConfirmationPasswordMatch {

    /**
     * @return the error message template
     */
    String message() default ValidationMessageConfig.CONFIRMATION_PASSWORD_MISMATCH;

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};

}
