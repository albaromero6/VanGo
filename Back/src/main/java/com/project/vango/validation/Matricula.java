package com.project.vango.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MatriculaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Matricula 
{
    String message() default "El formato de la matrícula no es válido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}