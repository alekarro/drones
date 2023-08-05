package org.musala.drones.api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musala.drones.api.dto.DroneStateDTO;
import org.musala.drones.api.dto.LoadingDTO;
import org.musala.drones.api.dto.LoadingException;
import org.musala.drones.api.model.LoadingItem;
import org.musala.drones.api.model.Medication;
import org.musala.drones.api.model.StateEnum;
import org.musala.drones.api.repository.DroneDAO;
import org.musala.drones.api.repository.LoadingDAO;
import org.musala.drones.api.repository.MedicationDAO;
import org.musala.drones.api.service.LoadingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.musala.drones.api.model.StateEnum.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoadingServiceImpl implements LoadingService {
    private final DroneDAO droneDAO;
    private final LoadingDAO loadingDAO;
    private final MedicationDAO medicationDAO;

    @Value("${drone.battery-limit}")
    private int batteryLimit;

    private final Set<String> availableStates = Set.of(LOADING.name());

    @Override
    @Transactional
    public boolean loadDrone(LoadingDTO loading) throws LoadingException {
        log.debug("loadDrone# started, loading = {}", loading);
        String droneCode = loading.getDroneSerialNumber();
        DroneStateDTO stateDTO = droneDAO.loadDroneState(droneCode);
        checkIfDroneReadyForLoading(stateDTO);

        Map<String, LoadingItem> loadingMedicationItems = loadingDAO.getLoadingItems(droneCode).stream()
                .collect(Collectors.toMap(LoadingItem::getMedicationCode, Function.identity()));

        Set<String> loadingMedicationCodes = loading.getLoadingItems().stream()
                .map(LoadingItem::getMedicationCode)
                .collect(Collectors.toSet());

        Map<String, Medication> medications = medicationDAO.getMedications(loadingMedicationCodes).stream()
                .collect(Collectors.toMap(Medication::getCode, Function.identity()));

        List<LoadingItem> newItems = new ArrayList<>();
        List<LoadingItem> updatedItems = new ArrayList<>();
        int loadedWeight = stateDTO.getLoadedWeight() != null ? stateDTO.getLoadedWeight() : 0;
        for (LoadingItem item : loading.getLoadingItems()) {
            loadedWeight = loadItem(stateDTO, loadingMedicationItems, medications, newItems, updatedItems, loadedWeight, item);
        }
        log.debug("loadDrone# newItems = {}, updatedItems = {}", newItems, updatedItems);
        saveData(droneCode, stateDTO, newItems, updatedItems, loadedWeight);
        return true;
    }

    @Override
    public DroneStateDTO loadLoadingDroneState(final String droneCode) {
        DroneStateDTO dto = droneDAO.loadDroneState(droneCode);
        dto.setLoadedMedications(loadingDAO.getLoadedMedications(droneCode));
        log.debug("loadLoadingDroneState# dto = {}", dto);
        return dto;
    }

    @Override
    public List<DroneStateDTO> getAvailableDrones() {
        return droneDAO.getAvailableDrones(batteryLimit, availableStates);
    }

    @Override
    @Transactional
    public boolean delivered(String droneCode) throws LoadingException {
        try {
            //I would add here the code for saving already completed deliveries in some table
            //...

            loadingDAO.deleteDroneLoadings(droneCode);
            DroneStateDTO dto = new DroneStateDTO();
            dto.setSerialNumber(droneCode);
            dto.setLoadedWeight(0);
            dto.setState(DELIVERED.name());
            droneDAO.updateDroneState(dto);
            log.debug("delivered# state = {}", dto);
        } catch (Exception e) {
            throw new LoadingException("Some error happened during delivery");
        }
        return true;
    }

    private void saveData(
            final String droneCode,
            final DroneStateDTO stateDTO,
            final List<LoadingItem> newItems,
            final List<LoadingItem> updatedItems,
            final int loadedWeight) throws LoadingException {
        if (!updatedItems.isEmpty()) {
            checkSavingResult(loadingDAO.batchUpdateLoading(droneCode, updatedItems));
        }

        if (!newItems.isEmpty()) {
            checkSavingResult(loadingDAO.batchCreateLoading(droneCode, newItems));
        }

        stateDTO.setLoadedWeight(loadedWeight);

        int lightestMedication = medicationDAO.findLightestWeight();
        if (stateDTO.getWeightLimit() - loadedWeight >= lightestMedication) {
            stateDTO.setState(LOADING.name());
        } else {
            stateDTO.setState(LOADED.name());
        }
        log.debug("loadDrone# stateDTO updated = {}", stateDTO);
        checkSavingResult(droneDAO.updateDroneState(stateDTO));
    }

    private void checkIfDroneReadyForLoading(final DroneStateDTO stateDTO) throws LoadingException {
        StateEnum state = StateEnum.valueOf(stateDTO.getState());
        if (state != LOADING || stateDTO.getBatteryLevel() < batteryLimit) {
            throw new LoadingException("Drone is not ready for loading");
        }
    }

    private void checkSavingResult(boolean res) throws LoadingException {
        if (!res) {
            throw new LoadingException("Loading failed, database error");
        }
    }

    private int loadItem(
            DroneStateDTO stateDTO,
            Map<String, LoadingItem> loadingMedicationItems,
            Map<String, Medication> medications,
            List<LoadingItem> newItems,
            List<LoadingItem> updatedItems,
            int loadedWeight,
            LoadingItem item
    ) throws LoadingException {
        Medication medication = medications.get(item.getMedicationCode());
        if (medication == null) {
            throw new LoadingException("Unknown medication");
        }
        loadedWeight += (item.getQuantity() * medication.getWeight());
        if (loadedWeight > stateDTO.getWeightLimit()) {
            throw new LoadingException("Weight limit exceeded; limit = " + stateDTO.getWeightLimit());
        }
        LoadingItem existedItem = loadingMedicationItems.get(item.getMedicationCode());
        if (existedItem == null) {
            newItems.add(item);
        } else {
            existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
            updatedItems.add(existedItem);
        }
        return loadedWeight;
    }
}
