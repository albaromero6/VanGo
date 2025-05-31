package com.project.vango.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FechasReservaValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface FechasReserva {
    String message() default "{validation.fechas.reserva}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}