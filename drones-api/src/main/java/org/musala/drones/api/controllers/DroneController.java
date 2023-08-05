package org.musala.drones.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.musala.drones.api.dto.*;
import org.musala.drones.api.service.DroneService;
import org.musala.drones.api.validation.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/drone")
@RequiredArgsConstructor
public class DroneController {
    private final DroneService droneService;

    @PostMapping
    public ResultDTO createOrUpdateDrone(@Valid @RequestBody DroneDTO drone) {
        return new ResultDTO(droneService.createOrUpdateDrone(drone));
    }

    @GetMapping("/battery-allowable")
    public ResultDTO isBatteryLevelAllowable(@Valid @RequestBody CodeDTO dto) {
        return new ResultDTO(droneService.isBatteryLevelAllowable(dto.getCode()));
    }

    @PutMapping("/state")
    public ResultDTO updateDroneState(@Valid @RequestBody DroneStateDTO droneState) {
        try {
            return new ResultDTO(droneService.updateDroneState(droneState));
        } catch (WrongStateException e) {
            return new ResultDTO(false, e.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ValidationUtils.handleValidationExceptions(ex);
    }
}
