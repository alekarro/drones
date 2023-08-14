package org.aro.drones.api.repository;

import org.aro.drones.api.dto.MedicationDTO;
import org.aro.drones.api.model.LoadingItem;

import java.util.List;

public interface LoadingDAO {
    List<LoadingItem> getLoadingItems(final String droneCode);

    boolean batchCreateLoading(final String droneCode, final List<LoadingItem> loadingItems);

    boolean batchUpdateLoading(final String droneCode, final List<LoadingItem> loadingItems);

    List<MedicationDTO> getLoadedMedications(final String droneCode);

    boolean deleteDroneLoadings(final String droneCode);
}
