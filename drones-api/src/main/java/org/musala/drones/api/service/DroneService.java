package org.musala.drones.api.service;

import org.musala.drones.api.dto.DroneDTO;
import org.musala.drones.api.dto.DroneStateDTO;
import org.musala.drones.api.dto.WrongStateException;

public interface DroneService {
    boolean createOrUpdateDrone(DroneDTO drone);
    boolean isBatteryLevelAllowable(final String droneSerialNumber);
    boolean updateDroneState(final DroneStateDTO dto) throws WrongStateException;
}
