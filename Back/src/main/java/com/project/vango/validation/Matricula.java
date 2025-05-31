package com.project.vango.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MatriculaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Matricula {
    String message() default "{validation.matricula}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}