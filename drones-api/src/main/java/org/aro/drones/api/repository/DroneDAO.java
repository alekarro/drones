package org.aro.drones.api.repository;

import org.aro.drones.api.dto.DroneDTO;
import org.aro.drones.api.dto.DroneStateDTO;

import java.util.List;
import java.util.Set;

public interface DroneDAO {
    boolean createDrone(final DroneDTO dto);

    boolean updateDrone(final DroneDTO dto);

    boolean createDroneState(final DroneStateDTO dto);

    boolean updateDroneState(final DroneStateDTO dto);

    DroneStateDTO loadDroneState(final String serialNumber);

    boolean isDroneExists(final String serialNumber);

    int findBatteryLevel(final String serialNumber);

    List<DroneStateDTO> getAvailableDrones(final int minBatteryLevel, final Set<String> availableStates);

    void auditDronesState();
}
