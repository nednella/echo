package com.example.echo_api.shared.validation.pagination.validators;

import com.example.echo_api.shared.constants.PaginationConstraints;
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
 * @see Limit
 * @see ConstraintValidator
 */
public class LimitValidator implements ConstraintValidator<Limit, Integer> {

    @Override
    public boolean isValid(Integer limit, ConstraintValidatorContext context) {
        return limit >= PaginationConstraints.MIN_LIMIT
            && limit <= PaginationConstraints.MAX_LIMIT;
    }

}
