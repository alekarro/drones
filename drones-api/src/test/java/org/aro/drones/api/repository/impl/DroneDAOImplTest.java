package org.aro.drones.api.repository.impl;

import org.aro.drones.api.dto.DroneDTO;
import org.aro.drones.api.dto.DroneStateDTO;
import org.aro.drones.api.model.StateEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DroneDAOImplTest {
    @InjectMocks
    private DroneDAOImpl droneDAO;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Captor
    ArgumentCaptor<PreparedStatementCreator> queryCaptor;

    @Test
    void testUpdateDroneState() {
        DroneStateDTO stateDTO = createStateDTO();
        when(jdbcTemplate.update(any(PreparedStatementCreator.class))).thenReturn(1);

        boolean res = droneDAO.updateDroneState(stateDTO);

        assertTrue(res);
        verify(jdbcTemplate, times(1)).update(queryCaptor.capture());
        String queryString = queryCaptor.getValue().toString();
        assertTrue(queryString.contains("update drone_state set  state = ?, battery_level = ?, loaded_weight = ? where serial_number = ?"));
        assertTrue(queryString.contains("LOADING, 50, 100, number_1"));
    }

    @Test
    void testCreateDrone() {
        DroneDTO droneDTO = createDroneDTO();
        when(jdbcTemplate.update(any(), any(), any(), any())).thenReturn(1);
        boolean res = droneDAO.createDrone(droneDTO);
        assertTrue(res);
    }

    @Test
    void testCreateDroneState() {
        DroneStateDTO stateDTO = createStateDTO();
        when(jdbcTemplate.update(any(), any(), any(), any(), any())).thenReturn(1);
        boolean res = droneDAO.createDroneState(stateDTO);
        assertTrue(res);
    }

    @Test
    void testUpdateDrone() {
        DroneDTO droneDTO = createDroneDTO();
        when(jdbcTemplate.update(any(PreparedStatementCreator.class))).thenReturn(1);
        boolean res = droneDAO.updateDrone(droneDTO);
        assertTrue(res);
    }

    private DroneStateDTO createStateDTO() {
        DroneStateDTO stateDTO = new DroneStateDTO();
        stateDTO.setSerialNumber("number_1");
        stateDTO.setState(StateEnum.LOADING.name());
        stateDTO.setBatteryLevel(50);
        stateDTO.setWeightLimit(500);
        stateDTO.setLoadedWeight(100);
        return stateDTO;
    }

    private DroneDTO createDroneDTO() {
        DroneDTO stateDTO = new DroneDTO();
        stateDTO.setSerialNumber("number_1");
        stateDTO.setState(StateEnum.LOADING.name());
        stateDTO.setBatteryLevel(50);
        stateDTO.setWeightLimit(500);
        return stateDTO;
    }
}
