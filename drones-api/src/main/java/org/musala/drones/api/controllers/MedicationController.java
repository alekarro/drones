package org.musala.drones.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.musala.drones.api.dto.ResultDTO;
import org.musala.drones.api.model.Medication;
import org.musala.drones.api.service.MedicationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/medication")
@RequiredArgsConstructor
public class MedicationController implements BaseController {

    private final MedicationService medicationService;
    @PostMapping
    public ResultDTO createOrUpdateDrone(@Valid @RequestBody Medication medication) {
        return serviceCall(() -> medicationService.createOrUpdateMedication(medication));
    }
}
