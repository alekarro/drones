package org.aro.drones.api.service;

import org.aro.drones.api.dto.DroneStateDTO;
import org.aro.drones.api.dto.LoadingDTO;
import org.aro.drones.api.dto.LoadingException;

import java.util.List;

public interface LoadingService {
    boolean loadDrone(LoadingDTO loading) throws LoadingException;

    DroneStateDTO getLoading(final String droneCode) throws LoadingException;

    List<DroneStateDTO> getAvailableDrones();

    boolean delivered(final String droneCode) throws LoadingException;
}
