package org.musala.drones.api.controllers;

import jakarta.validation.Valid;
import org.musala.drones.api.model.Medication;
import org.musala.drones.api.service.MedicationService;
import org.musala.drones.api.validation.ValidationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/medication")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @PostMapping ("")
    public boolean createOrUpdateDrone(@Valid @RequestBody Medication medication) {
        return medicationService.createOrUpdateMedication(medication);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ValidationUtils.handleValidationExceptions(ex);
    }
}
