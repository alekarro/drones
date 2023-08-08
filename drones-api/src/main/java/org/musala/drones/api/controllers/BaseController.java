package org.musala.drones.api.controllers;

import org.musala.drones.api.dto.LoadingException;
import org.musala.drones.api.dto.ResultDTO;
import org.musala.drones.api.dto.WrongStateException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public interface BaseController {

    default ResultDTO serviceCall(final Supplier<?> supplier) {
        try {
            return new ResultDTO(supplier.get());
        } catch (LoadingException | WrongStateException e) {
            return new ResultDTO(false, e.getMessage());
        } catch (Exception e) {
            return new ResultDTO(false, "Error occurred");
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    default Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getAllErrors().stream()
                .filter(e -> e.getDefaultMessage() != null)
                .collect(Collectors.toMap(e -> ((FieldError) e).getField(), DefaultMessageSourceResolvable::getDefaultMessage));
    }

}
