package com.project.vango.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FechaActualValidator implements ConstraintValidator<FechaActual, LocalDate> 
{
    @Override
    public boolean isValid(LocalDate fecha, ConstraintValidatorContext context) 
    {
        if (fecha == null) 
        {
            return false;
        }

        LocalDate hoy = LocalDate.now();
        return fecha.equals(hoy);
    }
}