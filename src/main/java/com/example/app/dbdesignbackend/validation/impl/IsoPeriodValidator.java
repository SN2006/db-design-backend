package com.example.app.dbdesignbackend.validation.impl;

import com.example.app.dbdesignbackend.validation.annotation.IsoPeriod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Period;
import java.time.format.DateTimeParseException;

public class IsoPeriodValidator implements ConstraintValidator<IsoPeriod, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            Period.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
