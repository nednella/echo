package com.example.echo_api.validation.pagination.validators;

import com.example.echo_api.validation.pagination.annotations.Limit;

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
        return limit >= 1 && limit <= 50;
    }

}
