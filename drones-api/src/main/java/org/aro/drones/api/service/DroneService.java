package org.aro.drones.api.service;

import org.aro.drones.api.dto.DroneDTO;
import org.aro.drones.api.dto.DroneStateDTO;
import org.aro.drones.api.dto.WrongStateException;

public interface DroneService {
    boolean createOrUpdateDrone(DroneDTO drone);

    boolean isBatteryLevelAllowable(final String droneSerialNumber);

    boolean updateDroneState(final DroneStateDTO dto) throws WrongStateException;
}
