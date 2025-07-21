package com.example.echo_api.validation.account.validators;

import com.example.echo_api.config.RegexConfig;
import com.example.echo_api.validation.account.annotations.Password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for the {@link Password} annotation.
 * 
 * <p>
 * This class ensures that a {@code CharSequence} value meets the criteria
 * specified by the regular expression defined as {@code REGEXP} within this
 * class. Null values are considered valid by default.
 * 
 * @see Password
 * @see ConstraintValidator
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {

    private static final String REGEXP = RegexConfig.PASSWORD;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are valid
        }

        return value.matches(REGEXP);
    }

}
