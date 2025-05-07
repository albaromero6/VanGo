package com.project.vango.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import com.project.vango.models.Reserva;
import java.time.LocalDate;

public class FechasReservaValidator implements ConstraintValidator<FechasReserva, Reserva> 
{

    @Override
    public boolean isValid(Reserva reserva, ConstraintValidatorContext context) 
    {
        if (reserva.getInicio() == null || reserva.getFin() == null) 
        {
            return false;
        }

        LocalDate hoy = LocalDate.now();

        // La fecha de inicio no puede ser anterior a hoy
        if (reserva.getInicio().isBefore(hoy)) 
        {
            return false;
        }

        // La fecha de fin debe ser posterior a la fecha de inicio
        if (!reserva.getFin().isAfter(reserva.getInicio())) 
        {
            return false;
        }

        return true;
    }
}