package com.example.echo_api.validation.account.validators;

import com.example.echo_api.config.RegexConfig;
import com.example.echo_api.validation.account.annotations.Username;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for the {@link Username} annotation.
 * 
 * <p>
 * This class ensures that a {@code CharSequence} value meets the criteria
 * specified by the regular expression defined in the {@code @Username}
 * annotation. Null values are considered valid by default.
 * 
 * @see Username
 * @see ConstraintValidator
 */
public class UsernameValidator implements ConstraintValidator<Username, String> {

    private static final String REGEXP = RegexConfig.USERNAME;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values are valid
        }

        return value.matches(REGEXP);
    }

}
