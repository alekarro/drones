package org.aro.drones.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, String> {

    Set<String> enumNames = null;
    boolean canBeEmpty = false;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasLength(value)) {
            return canBeEmpty;
        }
        return enumNames.contains(value.toUpperCase());
    }

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        canBeEmpty = constraintAnnotation.canBeEmpty();

        enumNames = new HashSet<>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();

        @SuppressWarnings("rawtypes")
        Enum[] enumValArr = enumClass.getEnumConstants();

        for (@SuppressWarnings("rawtypes") Enum enumVal : enumValArr) {
            enumNames.add(enumVal.name().toUpperCase());
        }
    }
}
