package com.project.vango.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefonoValidator implements ConstraintValidator<Telefono, String> {
    @Override
    public boolean isValid(String telefono, ConstraintValidatorContext context) {
        if (telefono == null || telefono.isEmpty()) {
            return false; // El teléfono es obligatorio
        }

        // Eliminamos espacios, guiones, paréntesis y el símbolo +
        String numeroLimpio = telefono.replaceAll("[\\s\\-()+]", "");

        // Verificamos que solo contenga dígitos
        if (!numeroLimpio.matches("\\d+")) {
            return false;
        }

        // Verificamos la longitud mínima y máxima
        // Mínimo: 7 dígitos (números cortos)
        // Máximo: 15 dígitos (números internacionales largos)
        return numeroLimpio.length() >= 7 && numeroLimpio.length() <= 15;
    }
}