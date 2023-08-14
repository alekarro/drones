package org.aro.drones.api.repository;

import org.aro.drones.api.model.Medication;

import java.util.List;
import java.util.Set;

public interface MedicationDAO {
    boolean createMedication(Medication medication);

    boolean updateMedication(Medication medication);

    int findLightestWeight();

    boolean isMedicationExists(final String code);

    List<Medication> getMedications(final Set<String> codes);
}
