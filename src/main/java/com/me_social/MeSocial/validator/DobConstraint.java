package com.me_social.MeSocial.validator;

import static java.lang.annotation.ElementType.FIELD;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import org.springframework.messaging.handler.annotation.Payload;

import jakarta.validation.Constraint;

@Target(value={FIELD})
@Retention(value=RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
    String message() default "Invalid date of birth";

    int min();

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}
