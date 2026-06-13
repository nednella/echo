package app.echo_social.shared.validation.validators;

import app.echo_social.shared.pagination.PaginationConstraints;
import app.echo_social.shared.validation.annotations.Offset;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for the {@link Offset} annotation.
 * 
 * <p>
 * This class ensures that {@code offset} arguments/fields as part of pagination
 * query parameters are valid.
 * 
 * @see Offset
 * @see ConstraintValidator
 */
public class OffsetValidator implements ConstraintValidator<Offset, Integer> {

    @Override
    public boolean isValid(Integer offset, ConstraintValidatorContext context) {
        return offset >= PaginationConstraints.MIN_OFFSET;
    }

}
