package com.me_social.MeSocial.validator;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DobValidator implements  ConstraintValidator<DobConstraint, Instant> {

    private int min;

    @Override
    public boolean isValid(Instant value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true; 
        }

        long years = ChronoUnit.YEARS.between(value.atOffset(ZoneOffset.UTC), Instant.now().atOffset(ZoneOffset.UTC));

        return years >= min;
    }
    
    @Override
    public void initialize(DobConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }
}
