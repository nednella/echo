package com.example.echo_api.validation.account.annotations;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.example.echo_api.config.ValidationMessageConfig;
import com.example.echo_api.validation.account.annotations.Password.List;
import com.example.echo_api.validation.account.validators.PasswordValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Custom Jakarta Validation annotation for application passwords.
 * 
 * <p>
 * The annotated {@code CharSequence} must match the specified regular
 * expression. The regular expression follows the Java regular expression
 * conventions see {@link java.util.regex.Pattern}.
 * 
 * <p>
 * Accepts {@code CharSequence}. {@code null} elements are considered valid.
 * 
 * <p>
 * For more information, refer to:
 * {@link https://github.com/jakartaee/validation/blob/main/src/main/java/jakarta/validation/constraints/Pattern.java}.
 * 
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Repeatable(List.class)
@Documented
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {

    /**
     * @return the error message template
     */
    String message() default ValidationMessageConfig.INVALID_PASSWORD;

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@link Password} annotations on the same element.
     *
     * @see Password
     */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Password[] value();

    }
}
