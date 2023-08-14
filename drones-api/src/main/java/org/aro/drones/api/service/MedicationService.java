package org.aro.drones.api.service;

import org.aro.drones.api.model.Medication;

public interface MedicationService {
    boolean createOrUpdateMedication(Medication medication);
}
