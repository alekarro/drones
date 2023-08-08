package org.musala.drones.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.musala.drones.api.dto.CodeDTO;
import org.musala.drones.api.dto.DroneDTO;
import org.musala.drones.api.dto.DroneStateDTO;
import org.musala.drones.api.dto.ResultDTO;
import org.musala.drones.api.service.DroneService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drone")
@RequiredArgsConstructor
public class DroneController implements BaseController {
    private final DroneService droneService;

    @PostMapping
    public ResultDTO createOrUpdateDrone(@Valid @RequestBody DroneDTO drone) {
        return serviceCall(() -> droneService.createOrUpdateDrone(drone));
    }

    @GetMapping("/battery-allowable")
    public ResultDTO isBatteryLevelAllowable(@Valid @RequestBody CodeDTO dto) {
        return serviceCall(() -> droneService.isBatteryLevelAllowable(dto.getCode()));
    }

    @PutMapping("/state")
    public ResultDTO updateDroneState(@Valid @RequestBody DroneStateDTO droneState) {
        return serviceCall(() -> droneService.updateDroneState(droneState));
    }
}
