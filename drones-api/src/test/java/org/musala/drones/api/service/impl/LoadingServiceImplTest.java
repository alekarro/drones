package org.musala.drones.api.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.musala.drones.api.dto.DroneStateDTO;
import org.musala.drones.api.dto.LoadingDTO;
import org.musala.drones.api.dto.LoadingException;
import org.musala.drones.api.model.LoadingItem;
import org.musala.drones.api.model.Medication;
import org.musala.drones.api.model.StateEnum;
import org.musala.drones.api.repository.DroneDAO;
import org.musala.drones.api.repository.LoadingDAO;
import org.musala.drones.api.repository.MedicationDAO;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoadingServiceImplTest {

    @InjectMocks
    private LoadingServiceImpl testedService;

    @Mock
    private DroneDAO droneDAO;
    @Mock
    private LoadingDAO loadingDAO;
    @Mock
    private MedicationDAO medicationDAO;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(testedService, "batteryLimit", 25);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "500, LOADING",
            "400, LOADED"
    })
    void testLoadDrone_createLoadingItems(int weightLimit, String resultState) {
        String droneSerialNumber = "number_1";
        List<Medication> medications = createMedications();
        Set<String> medCodes = Set.of(medications.get(0).getCode(), medications.get(1).getCode());
        List<LoadingItem> items = createLoadingItems(droneSerialNumber, medications, 3, 5);
        DroneStateDTO stateDTO = createStateDTO(weightLimit, 0);
        when(droneDAO.loadDroneState(droneSerialNumber)).thenReturn(stateDTO);
        when(medicationDAO.getMedications(medCodes)).thenReturn(medications);
        when(loadingDAO.batchCreateLoading(anyString(), any())).thenReturn(true);
        when(medicationDAO.findLightestWeight()).thenReturn(20);
        when(droneDAO.updateDroneState(stateDTO)).thenReturn(true);

        boolean res = testedService.loadDrone(createLoadingDTO(droneSerialNumber, items));

        assertTrue(res);
        assertEquals(50, stateDTO.getBatteryLevel());
        assertEquals(resultState, stateDTO.getState());
        assertEquals(400, stateDTO.getLoadedWeight());
        assertEquals(weightLimit, stateDTO.getWeightLimit());
        verify(droneDAO, times(1)).loadDroneState(droneSerialNumber);
        verify(loadingDAO, times(1)).getLoadingItems(droneSerialNumber);
        verify(medicationDAO, times(1)).getMedications(medCodes);
        verify(loadingDAO, times(1)).batchCreateLoading(any(), any());
        verify(medicationDAO, times(1)).findLightestWeight();
        verify(droneDAO, times(1)).updateDroneState(stateDTO);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "500, LOADING",
            "330, LOADED"
    })
    void testLoadDrone_updateLoadingItems(int weightLimit, String resultState) {
        String droneSerialNumber = "number_1";
        List<Medication> medications = createMedications();
        Set<String> medCodes = Set.of(medications.get(0).getCode(), medications.get(1).getCode());
        List<LoadingItem> items = createLoadingItems(droneSerialNumber, medications, 1, 3);
        DroneStateDTO stateDTO = createStateDTO(weightLimit, 160);
        when(droneDAO.loadDroneState(droneSerialNumber)).thenReturn(stateDTO);
        when(loadingDAO.getLoadingItems(droneSerialNumber)).thenReturn(items);
        when(medicationDAO.getMedications(medCodes)).thenReturn(medications);
        when(loadingDAO.batchUpdateLoading(anyString(), any())).thenReturn(true);
        when(medicationDAO.findLightestWeight()).thenReturn(20);
        when(droneDAO.updateDroneState(stateDTO)).thenReturn(true);

        boolean res = testedService.loadDrone(createLoadingDTO(droneSerialNumber, items));

        assertTrue(res);
        assertEquals(50, stateDTO.getBatteryLevel());
        assertEquals(resultState, stateDTO.getState());
        assertEquals(320, stateDTO.getLoadedWeight());
        assertEquals(weightLimit, stateDTO.getWeightLimit());
        verify(droneDAO, times(1)).loadDroneState(droneSerialNumber);
        verify(loadingDAO, times(1)).getLoadingItems(droneSerialNumber);
        verify(medicationDAO, times(1)).getMedications(medCodes);
        verify(loadingDAO, times(1)).batchUpdateLoading(any(), any());
        verify(medicationDAO, times(1)).findLightestWeight();
        verify(droneDAO, times(1)).updateDroneState(stateDTO);
    }

    @Test
    void testLoadDrone_LoadingException() {
        String droneSerialNumber = "number_1";
        DroneStateDTO stateDTO = createStateDTO(500, 160);
        stateDTO.setBatteryLevel(24);
        when(droneDAO.loadDroneState(droneSerialNumber)).thenReturn(stateDTO);
        Exception exception = assertThrows(LoadingException.class, ()
                -> testedService.loadDrone(createLoadingDTO(droneSerialNumber, null)));

        assertTrue(exception.getMessage().startsWith("Drone is not ready for loading; drone"));
    }

    @Test
    void testLoadDrone_LoadingExceptionWeightExceeds() {
        String droneSerialNumber = "number_1";
        List<Medication> medications = createMedications();
        Set<String> medCodes = Set.of(medications.get(0).getCode(), medications.get(1).getCode());
        List<LoadingItem> items = createLoadingItems(droneSerialNumber, medications, 3, 5);
        DroneStateDTO stateDTO = createStateDTO(50, 0);
        when(droneDAO.loadDroneState(droneSerialNumber)).thenReturn(stateDTO);
        when(medicationDAO.getMedications(medCodes)).thenReturn(medications);

        Exception exception = assertThrows(LoadingException.class, ()
                -> testedService.loadDrone(createLoadingDTO(droneSerialNumber, items)));

        assertTrue(exception.getMessage().startsWith("Weight limit exceeded; limit"));
    }

    @Test
    void testLoadLoadingDroneState() {
        String droneSerialNumber = "number_1";
        DroneStateDTO stateDTO = createStateDTO(500, 160);
        when(droneDAO.loadDroneState(droneSerialNumber)).thenReturn(stateDTO);

        DroneStateDTO resultState = testedService.loadLoadingDroneState(droneSerialNumber);

        assertEquals(stateDTO, resultState);
        verify(droneDAO, times(1)).loadDroneState(droneSerialNumber);
        verify(loadingDAO, times(1)).getLoadedMedications(droneSerialNumber);
    }

    @Test
    void testLoadLoadingDroneState_Exception() {
        String droneSerialNumber = "number_1";
        when(droneDAO.loadDroneState(droneSerialNumber)).thenReturn(null);

        Exception exception = assertThrows(LoadingException.class, ()
                -> testedService.loadLoadingDroneState(droneSerialNumber));

        assertTrue(exception.getMessage().startsWith("Drone is absent, drone"));
    }

    @Test
    void testDelivered() {
        String droneSerialNumber = "number_1";

        boolean res = testedService.delivered(droneSerialNumber);
        assertTrue(res);
        verify(loadingDAO, times(1)).deleteDroneLoadings(droneSerialNumber);
        verify(droneDAO, times(1)).updateDroneState(any());
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

    private DroneStateDTO createStateDTO(int weightLimit, int loadedWeight) {
        DroneStateDTO stateDTO = new DroneStateDTO();
        stateDTO.setState(StateEnum.LOADING.name());
        stateDTO.setBatteryLevel(50);
        stateDTO.setWeightLimit(weightLimit);
        stateDTO.setLoadedWeight(loadedWeight);
        return stateDTO;
    }

    private LoadingDTO createLoadingDTO(String droneSerialNumber, List<LoadingItem> items) {
        LoadingDTO loading = new LoadingDTO();
        loading.setDroneSerialNumber(droneSerialNumber);
        loading.setLoadingItems(items);
        return loading;
    }
}
