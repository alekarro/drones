package org.musala.drones.api.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.musala.drones.api.dto.DroneDTO;
import org.musala.drones.api.dto.DroneStateDTO;
import org.musala.drones.api.dto.LoadingDTO;
import org.musala.drones.api.model.LoadingItem;
import org.musala.drones.api.model.Medication;
import org.musala.drones.api.model.ModelEnum;
import org.musala.drones.api.model.StateEnum;
import org.musala.drones.api.service.DroneService;
import org.musala.drones.api.service.LoadingService;
import org.musala.drones.api.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class LoadingIntegrationTest extends AbstractServiceIntegrationTest {

    @Autowired
    LoadingService loadingService;

    @Autowired
    DroneService droneService;

    @Autowired
    MedicationService medicationService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private DroneDTO drone;
    private List<Medication> medications;

    @BeforeEach
    void init() {
        jdbcTemplate.update("delete from loading");
        jdbcTemplate.update("delete from drone_state");
        jdbcTemplate.update("delete from medication");
        jdbcTemplate.update("delete from drone");
        drone = createDrone();
        medications = createMedications();
        insertData(drone, medications);
    }

    @Test
    void testDroneFullCycle() {
        loadDroneAndCheck(1, 3, "LOADING", 160);
        loadDroneAndCheck(1, 4, "LOADING", 340);
        loadDroneAndCheck(1, 3, "LOADED", 500);

        boolean res = loadingService.delivered(drone.getSerialNumber());
        Assertions.assertTrue(res);
        checkDroneState("DELIVERED", 0, 0);
    }

    @Test
    void testGetAvailableDrones() {
        List<DroneStateDTO> drones = loadingService.getAvailableDrones();
        Assertions.assertEquals(1, drones.size());
        DroneStateDTO availableDrone = drones.get(0);
        Assertions.assertEquals(drone.getBatteryLevel(), availableDrone.getBatteryLevel());
        Assertions.assertEquals("LOADING", availableDrone.getState());
        Assertions.assertEquals(0, availableDrone.getLoadedWeight());
        Assertions.assertEquals(drone.getWeightLimit(), availableDrone.getWeightLimit());
    }

    private void loadDroneAndCheck(int med1Quantity, int med2quantity, String resState, int resWeight){
        List<LoadingItem> items = createLoadingItems(drone.getSerialNumber(), medications, med1Quantity, med2quantity);
        boolean res = loadingService.loadDrone(createLoadingDTO(drone.getSerialNumber(), items));
        Assertions.assertTrue(res);
        checkDroneState(resState, resWeight, 2);
    }

    private void checkDroneState(String resState, int resWeight, int resItemsNumber) {
        DroneStateDTO stateDTO = loadingService.getLoading(drone.getSerialNumber());
        Assertions.assertEquals(drone.getBatteryLevel(), stateDTO.getBatteryLevel());
        Assertions.assertEquals(resState, stateDTO.getState());
        Assertions.assertEquals(resWeight, stateDTO.getLoadedWeight());
        Assertions.assertEquals(drone.getWeightLimit(), stateDTO.getWeightLimit());
        Assertions.assertEquals(resItemsNumber, stateDTO.getLoadedMedications().size());

    }
    private List<Medication> createMedications() {
        Medication medication1 = new Medication("MED_1", "drug-1", 100, null);
        Medication medication2 = new Medication("MED_2", "drug-2", 20, null);
        return List.of(medication1, medication2);
    }

    private List<LoadingItem> createLoadingItems(
            String droneSerialNumber,
            List<Medication> medications,
            int quantity1,
            int quantity2) {
        return List.of(
                new LoadingItem(droneSerialNumber, medications.get(0).getCode(), quantity1),
                new LoadingItem(droneSerialNumber, medications.get(1).getCode(), quantity2)
        );
    }

    private LoadingDTO createLoadingDTO(String droneSerialNumber, List<LoadingItem> items) {
        LoadingDTO loading = new LoadingDTO();
        loading.setDroneSerialNumber(droneSerialNumber);
        loading.setLoadingItems(items);
        return loading;
    }

    private void insertData(DroneDTO droneDTO, List<Medication> medications) {
        droneService.createOrUpdateDrone(droneDTO);
        medications.forEach(m -> medicationService.createOrUpdateMedication(m));
    }

    private DroneDTO createDrone() {
        DroneDTO dto = new DroneDTO();
        dto.setSerialNumber("number_1");
        dto.setState(StateEnum.LOADING.name());
        dto.setBatteryLevel(90);
        dto.setWeightLimit(500);
        dto.setModel(ModelEnum.CRUISERWEIGHT.name());
        return dto;
    }
}