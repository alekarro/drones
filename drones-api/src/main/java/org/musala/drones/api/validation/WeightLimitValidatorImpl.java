package org.musala.drones.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class WeightLimitValidatorImpl implements ConstraintValidator<WeightLimitValidator, Integer> {
    @Value("${drone.weight-limit}")
    private int maxVal;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return value != null && value >= 0 && value <= maxVal;
    }
}
