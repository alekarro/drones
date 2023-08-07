package org.musala.drones.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.musala.drones.api.dto.CodeDTO;
import org.musala.drones.api.dto.LoadingDTO;
import org.musala.drones.api.dto.LoadingException;
import org.musala.drones.api.dto.ResultDTO;
import org.musala.drones.api.service.LoadingService;
import org.musala.drones.api.validation.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/loading")
@RequiredArgsConstructor
public class LoadingController {
    private final LoadingService loadingService;

    @PostMapping
    public ResultDTO loading(@Valid @RequestBody LoadingDTO loading) {
        try {
            return new ResultDTO(loadingService.loadDrone(loading));
        } catch (LoadingException e) {
            return new ResultDTO(false, e.getMessage());
        }
    }

    @GetMapping
    public ResultDTO getLoadedItems(@Valid @RequestBody CodeDTO codeDTO) {
        try {
            return new ResultDTO(loadingService.loadLoadingDroneState(codeDTO.getCode()));
        } catch (LoadingException e) {
            return new ResultDTO(false, e.getMessage());
        }
    }

    @GetMapping("/available-drones")
    public ResultDTO getAvailableDrones() {
        return new ResultDTO(loadingService.getAvailableDrones());
    }

    @PostMapping("/delivered")
    public ResultDTO delivered(@Valid @RequestBody CodeDTO codeDTO) {
        try {
            return new ResultDTO(loadingService.delivered(codeDTO.getCode()));
        } catch (LoadingException e) {
            return new ResultDTO(false, e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ValidationUtils.handleValidationExceptions(ex);
    }
}
