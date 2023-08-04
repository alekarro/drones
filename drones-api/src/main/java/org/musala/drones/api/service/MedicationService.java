package org.musala.drones.api.service;

import org.musala.drones.api.model.Medication;

public interface MedicationService {
    boolean createOrUpdateMedication(Medication medication);
}
