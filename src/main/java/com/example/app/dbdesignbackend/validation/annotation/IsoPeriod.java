package com.example.app.dbdesignbackend.validation.annotation;

import com.example.app.dbdesignbackend.validation.impl.IsoPeriodValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = IsoPeriodValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsoPeriod {
    String message() default "Invalid ISO 8601 duration format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
