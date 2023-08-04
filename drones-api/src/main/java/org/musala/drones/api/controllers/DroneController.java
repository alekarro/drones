package org.musala.drones.api.controllers;

import jakarta.validation.Valid;
import org.musala.drones.api.dto.*;
import org.musala.drones.api.service.DroneService;
import org.musala.drones.api.validation.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/drone")
public class DroneController {
    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping ("")
    public SingleResultDTO createOrUpdateDrone(@Valid @RequestBody DroneDTO drone) {
        return new SingleResultDTO(droneService.createOrUpdateDrone(drone));
    }

    @GetMapping ("/battery-allowable")
    public SingleResultDTO isBatteryLevelAllowable(@Valid @RequestBody CodeDTO dto) {
        return new SingleResultDTO(droneService.isBatteryLevelAllowable(dto.getCode()));
    }

    @PutMapping ("/state")
    public SingleResultDTO updateDroneState(@Valid @RequestBody DroneStateDTO droneState) {
        try {
            return new SingleResultDTO(droneService.updateDroneState(droneState));
        } catch (WrongStateException e) {
            return new SingleResultDTO(false, e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ValidationUtils.handleValidationExceptions(ex);
    }
}
