package org.musala.drones.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;


@Constraint(validatedBy = WeightLimitValidatorImpl.class)
@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WeightLimitValidator {
    String message() default "Wrong weight limit";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
