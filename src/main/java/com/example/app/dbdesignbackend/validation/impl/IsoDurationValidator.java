package com.example.app.dbdesignbackend.validation.impl;

import com.example.app.dbdesignbackend.validation.annotation.IsoDuration;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;
import java.time.format.DateTimeParseException;

public class IsoDurationValidator implements ConstraintValidator<IsoDuration, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            Duration.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
