package org.musala.drones.api.service;

import org.musala.drones.api.dto.DroneStateDTO;
import org.musala.drones.api.dto.LoadingDTO;
import org.musala.drones.api.dto.LoadingException;

import java.util.List;

public interface LoadingService {
    boolean loadDrone(LoadingDTO loading) throws LoadingException;
    DroneStateDTO loadLoadingDroneState(final String droneCode);
    List<DroneStateDTO> getAvailableDrones();
    boolean delivered(final String droneCode) throws LoadingException;
}