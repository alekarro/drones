package org.aro.drones.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;


@Constraint(validatedBy = EnumValidatorImpl.class)
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnumValidator {
    Class<? extends Enum<?>> enumClass();

    String message() default "Enum value is not valid";

    boolean canBeEmpty() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
