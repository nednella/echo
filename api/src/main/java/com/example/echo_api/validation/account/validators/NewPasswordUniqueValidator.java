package com.example.echo_api.validation.account.validators;

import com.example.echo_api.persistence.dto.request.account.UpdatePasswordDTO;
import com.example.echo_api.validation.account.annotations.NewPasswordUnique;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for the {@link NewPasswordUnique} annotation.
 * 
 * <p>
 * This class ensures that fields {@code newPassword} and
 * {@code currentPassword} do not match within a {@link UpdatePasswordDTO} form.
 * 
 * @see NewPasswordUnique
 * @see ConstraintValidator
 */
public class NewPasswordUniqueValidator implements ConstraintValidator<NewPasswordUnique, UpdatePasswordDTO> {

    @Override
    public boolean isValid(UpdatePasswordDTO form, ConstraintValidatorContext context) {
        return !form.newPassword().equals(form.currentPassword());
    }

}