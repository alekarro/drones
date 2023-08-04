package org.musala.drones.api.service.impl;

import org.musala.drones.api.model.Medication;
import org.musala.drones.api.repository.MedicationDAO;
import org.musala.drones.api.service.MedicationService;
import org.springframework.stereotype.Service;

@Service
public class MedicationServiceImpl implements MedicationService {
    private final MedicationDAO medicationDAO;

    public MedicationServiceImpl(MedicationDAO medicationDAO) {
        this.medicationDAO = medicationDAO;
    }

    @Override
    public boolean createOrUpdateMedication(final Medication medication) {
        if(medicationDAO.isMedicationExists(medication.getCode())) {
            return medicationDAO.updateMedication(medication);
        }
        return medicationDAO.createMedication(medication);
    }
}
