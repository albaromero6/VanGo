package com.project.vango.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DNIValidator implements ConstraintValidator<DNI, String> 
{

    private static final String LETRAS = "TRWAGMYFPDXBNJZSQVHLCKE";

    @Override
    public boolean isValid(String dni, ConstraintValidatorContext context) 
    {
        if (dni == null || dni.length() != 9) 
        {
            return false;
        }

        String numeros = dni.substring(0, 8);
        char letra = Character.toUpperCase(dni.charAt(8));

        try {
            int numero = Integer.parseInt(numeros);
            char letraCalculada = LETRAS.charAt(numero % 23);
            return letra == letraCalculada;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}