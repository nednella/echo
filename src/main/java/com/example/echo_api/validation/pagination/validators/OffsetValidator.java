package com.example.echo_api.validation.pagination.validators;

import com.example.echo_api.validation.pagination.annotations.Offset;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for the {@link Offset} annotation.
 * 
 * <p>
 * This class ensures that {@code offset} arguments/fields as part of pagination
 * query parameters are valid.
 * 
 * <p>
 * The validator ensures that any specified {@code offset} are equal to or
 * greater than 0.
 * 
 * @see Offset
 * @see ConstraintValidator
 */
public class OffsetValidator implements ConstraintValidator<Offset, Integer> {

    @Override
    public boolean isValid(Integer offset, ConstraintValidatorContext context) {
        return offset >= 0;
    }

}
