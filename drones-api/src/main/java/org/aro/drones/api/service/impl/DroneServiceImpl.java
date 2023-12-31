package org.aro.drones.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aro.drones.api.dto.DroneDTO;
import org.aro.drones.api.dto.DroneStateDTO;
import org.aro.drones.api.dto.WrongStateException;
import org.aro.drones.api.repository.DroneDAO;
import org.aro.drones.api.service.DroneService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static org.aro.drones.api.model.StateEnum.IDLE;
import static org.aro.drones.api.model.StateEnum.LOADING;

@Service
@RequiredArgsConstructor
@Slf4j
public class DroneServiceImpl implements DroneService {
    private final DroneDAO droneDAO;
    @Value("${drone.battery-limit}")
    private int batteryLimit;

    @Override
    public boolean createOrUpdateDrone(final DroneDTO dto) {
        dto.setState(correctState(dto.getState(), dto.getBatteryLevel()));
        boolean updateDroneResult;
        if (droneDAO.isDroneExists(dto.getSerialNumber())) {
            updateDroneResult = droneDAO.updateDrone(dto);
            if (updateDroneResult) {
                log.debug("createOrUpdateDrone# updated, dto = {}", dto);
                return droneDAO.updateDroneState(new DroneStateDTO(dto));
            }
        } else {
            updateDroneResult = droneDAO.createDrone(dto);
            if (updateDroneResult) {
                log.debug("createOrUpdateDrone# created, dto = {}", dto);
                return droneDAO.createDroneState(new DroneStateDTO(dto));
            }
        }
        return false;
    }

    @Override
    public boolean updateDroneState(final DroneStateDTO dto) throws WrongStateException {
        DroneStateDTO existingState = droneDAO.loadDroneState(dto.getSerialNumber());
        if (existingState == null) {
            throw new WrongStateException("Drone does not exist; drone = " + dto.getSerialNumber());
        }
        int batteryLevel = dto.getBatteryLevel() != null ? dto.getBatteryLevel()
                : existingState.getBatteryLevel() != null ? existingState.getBatteryLevel() : 0;
        String state = dto.getState() != null ? dto.getState() : existingState.getState();
        dto.setState(correctState(state, batteryLevel));
        dto.setBatteryLevel(batteryLevel);
        boolean res = droneDAO.updateDroneState(dto);
        log.debug("updateDroneState# updated, dto = {}, res = {}", dto, res);
        return res;
    }

    @Override
    public boolean isBatteryLevelAllowable(final String droneSerialNumber) {
        log.debug("isBatteryLevelAllowable# ,droneSerialNumber = {}", droneSerialNumber);
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
