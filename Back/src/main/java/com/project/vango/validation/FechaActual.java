package com.project.vango.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FechaActualValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FechaActual 
{
    String message() default "La fecha debe ser la fecha actual";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}