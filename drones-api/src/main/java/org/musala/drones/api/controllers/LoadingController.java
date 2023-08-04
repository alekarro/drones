package org.musala.drones.api.controllers;

import jakarta.validation.Valid;
import org.musala.drones.api.dto.CodeDTO;
import org.musala.drones.api.dto.LoadingDTO;
import org.musala.drones.api.dto.LoadingException;
import org.musala.drones.api.dto.SingleResultDTO;
import org.musala.drones.api.service.LoadingService;
import org.musala.drones.api.validation.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/loading")
public class LoadingController {
    private final LoadingService loadingService;

    public LoadingController(LoadingService loadingService) {
        this.loadingService = loadingService;
    }

    @PostMapping("")
    public SingleResultDTO loading(@Valid @RequestBody LoadingDTO loading) {
        try {
            return new SingleResultDTO(loadingService.loadDrone(loading));
        } catch (LoadingException e) {
            return new SingleResultDTO(false, e.getMessage());
        }
    }

    @GetMapping("")
    public SingleResultDTO getLoadedItems(@Valid @RequestBody CodeDTO codeDTO) {
        return new SingleResultDTO(loadingService.loadLoadingDroneState(codeDTO.getCode()));
    }

    @GetMapping ("/available-drones")
    public SingleResultDTO getAvailableDrones() {
        return new SingleResultDTO(loadingService.getAvailableDrones());
    }

    @PostMapping("/delivered")
    public SingleResultDTO delivered(@Valid @RequestBody CodeDTO codeDTO) {
        try {
            return new SingleResultDTO(loadingService.delivered(codeDTO.getCode()));
        } catch (LoadingException e) {
            return new SingleResultDTO(false, e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ValidationUtils.handleValidationExceptions(ex);
    }
}
