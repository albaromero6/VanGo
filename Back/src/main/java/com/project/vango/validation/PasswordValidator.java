package com.project.vango.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        // Verificar longitud mínima de 8 caracteres
        if (password.length() < 8) {
            return false;
        }

        // Verificar que contenga al menos una mayúscula
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }

        // Verificar que contenga al menos un número
        if (!password.matches(".*\\d.*")) {
            return false;
        }

        return true;
    }
}