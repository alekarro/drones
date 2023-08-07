package org.musala.drones.api.repository.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.musala.drones.api.dto.DroneStateDTO;
import org.musala.drones.api.model.StateEnum;
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
        verify(jdbcTemplate, times(1)).update(queryCaptor.capture());
        String queryString = queryCaptor.getValue().toString();
        assertTrue(queryString.contains("update drone_state set  state = ?, battery_level = ?, loaded_weight = ? where serial_number = ?"));
        assertTrue(queryString.contains("LOADING, 50, 100, number_1"));

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
}
