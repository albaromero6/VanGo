package com.project.vango.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatriculaValidator implements ConstraintValidator<Matricula, String> 
{
    @Override
    public boolean isValid(String matricula, ConstraintValidatorContext context) 
    {
        if (matricula == null || matricula.isEmpty()) 
        {
            return false;
        }

        // Eliminamos espacios y convertimos a mayúsculas
        String matriculaLimpia = matricula.replaceAll("\\s", "").toUpperCase();

        // Validamos el formato de matrícula española (4 números + 3 letras)
        return matriculaLimpia.matches("^[0-9]{4}[A-Z]{3}$");
    }
}