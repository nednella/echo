package app.echo_social.shared.validation.validators;

import app.echo_social.shared.pagination.PaginationConstraints;
import app.echo_social.shared.validation.annotations.Limit;

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
