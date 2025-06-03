package com.project.vango.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefonoValidator implements ConstraintValidator<Telefono, String> {
    @Override
    public boolean isValid(String telefono, ConstraintValidatorContext context) {
        if (telefono == null || telefono.isEmpty()) {
            return false;
        }

        // Eliminamos espacios, guiones, paréntesis y el símbolo +
        String numeroLimpio = telefono.replaceAll("[\\s\\-()+]", "");

        // Verificamos que solo contenga dígitos y tenga exactamente 9 números
        return numeroLimpio.matches("^\\d{9}$");
    }
}