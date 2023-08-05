package org.musala.drones.api.validation;

import lombok.experimental.UtilityClass;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class ValidationUtils {

    public static Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .filter(e -> e.getDefaultMessage() != null)
                .collect(Collectors.toMap(e -> ((FieldError) e).getField(), DefaultMessageSourceResolvable::getDefaultMessage));
    }
}
