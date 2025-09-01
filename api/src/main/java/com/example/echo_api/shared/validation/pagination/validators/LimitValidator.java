package com.example.echo_api.shared.validation.pagination.validators;

import com.example.echo_api.config.ConstraintsConfig;
import com.example.echo_api.shared.validation.pagination.annotations.Limit;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for the {@link Limit} annotation.
 * 
 * <p>
 * This class ensures that {@code limit} arguments/fields as part of pagination
 * query parameters are valid.
 * 
 * <p>
 * The validator ensures that any specified {@code limit} are within the range
 * {@code 1} to {@code 50}.
 * 
 * @see Limit
 * @see ConstraintValidator
 */
public class LimitValidator implements ConstraintValidator<Limit, Integer> {

    @Override
    public boolean isValid(Integer limit, ConstraintValidatorContext context) {
        return limit >= ConstraintsConfig.Pagination.MIN_LIMIT
            && limit <= ConstraintsConfig.Pagination.MAX_LIMIT;
    }

}
