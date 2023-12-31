package org.aro.drones.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aro.drones.api.model.Medication;
import org.aro.drones.api.repository.MedicationDAO;
import org.aro.drones.api.service.MedicationService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationServiceImpl implements MedicationService {
    private final MedicationDAO medicationDAO;

    @Override
    public boolean createOrUpdateMedication(final Medication medication) {
        if (medicationDAO.isMedicationExists(medication.getCode())) {
            log.debug("createOrUpdateMedication# updated, medication = {}", medication);
            return medicationDAO.updateMedication(medication);
        }
        log.debug("createOrUpdateMedication# created, medication = {}", medication);
        return medicationDAO.createMedication(medication);
    }
}
