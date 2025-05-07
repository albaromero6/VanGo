package com.project.vango.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DNIValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DNI 
{
    String message() default "El DNI no es válido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}