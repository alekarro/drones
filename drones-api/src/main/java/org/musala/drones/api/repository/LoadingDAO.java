package org.musala.drones.api.repository;

import org.musala.drones.api.dto.MedicationDTO;
import org.musala.drones.api.model.LoadingItem;

import java.util.List;

public interface LoadingDAO {
    List<LoadingItem> getLoadingItems(final String droneCode);
    boolean batchCreateLoading(final String droneCode, final List<LoadingItem> loadingItems);
    boolean batchUpdateLoading(final String droneCode, final List<LoadingItem> loadingItems);
    List<MedicationDTO> getLoadedMedications(final String droneCode);
    boolean deleteDroneLoadings(final String droneCode);
}
