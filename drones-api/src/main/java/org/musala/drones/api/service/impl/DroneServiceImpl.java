package org.musala.drones.api.service.impl;

import org.musala.drones.api.dto.DroneDTO;
import org.musala.drones.api.dto.DroneStateDTO;
import org.musala.drones.api.dto.WrongStateException;
import org.musala.drones.api.repository.DroneDAO;
import org.musala.drones.api.service.DroneService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.musala.drones.api.model.StateEnum.IDLE;
import static org.musala.drones.api.model.StateEnum.LOADING;

@Service
public class DroneServiceImpl implements DroneService {
    private final DroneDAO droneDAO;

    @Value("${drone.battery-limit}")
    private int batteryLimit;

    public DroneServiceImpl(DroneDAO droneDAO) {
        this.droneDAO = droneDAO;
    }

    @Override
    public boolean createOrUpdateDrone(final DroneDTO dto) {
        dto.setState(correctState(dto.getState(), dto.getBatteryLevel()));
        boolean updateDroneResult;
        if(droneDAO.isDroneExists(dto.getSerialNumber())) {
            updateDroneResult = droneDAO.updateDrone(dto);
            if (updateDroneResult) {
                return droneDAO.updateDroneState(new DroneStateDTO(dto));
            }
        } else {
            updateDroneResult = droneDAO.createDrone(dto);
            if (updateDroneResult) {
                return droneDAO.createDroneState(new DroneStateDTO(dto));
            }
        }
        return false;
    }

    @Override
    public boolean updateDroneState(final DroneStateDTO dto) throws WrongStateException {
        DroneStateDTO existingState = droneDAO.loadDroneState(dto.getSerialNumber());
        if (existingState == null) {
            throw new WrongStateException("Drone does not exist");
        }
        int batteryLevel = dto.getBatteryLevel() != null ? dto.getBatteryLevel()
                :existingState.getBatteryLevel() != null ? existingState.getBatteryLevel() : 0 ;
        String state = dto.getState() != null ? dto.getState() : existingState.getState();
        dto.setState(correctState(state, batteryLevel));
        dto.setBatteryLevel(batteryLevel);
        return droneDAO.updateDroneState(dto);
    }

    @Override
    public boolean isBatteryLevelAllowable(final String droneSerialNumber) {
        return droneDAO.findBatteryLevel(droneSerialNumber) >= batteryLimit;
    }

    private String correctState(final String initState, final int batteryLevel) {
        String resultState = initState;
        if (resultState == null) {
            resultState = LOADING.name();
        }

        if (LOADING.name().equals(resultState) && batteryLevel < batteryLimit) {
            resultState = IDLE.name();
        } else if (IDLE.name().equals(resultState) && batteryLevel >= batteryLimit) {
            resultState = LOADING.name();
        }

        return resultState;
    }
}
