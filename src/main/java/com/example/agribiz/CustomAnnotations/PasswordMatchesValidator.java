package com.example.agribiz.CustomAnnotations;

import com.example.agribiz.Dto.Request.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {}

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        RegisterRequest user = (RegisterRequest) obj;

        if (user.getPassword() == null || user.getConfirmPassword() == null) {
            return false;
        }

        return user.getPassword().equals(user.getConfirmPassword());
    }
}