package org.aro.drones.api.controllers;

import org.aro.drones.api.dto.CodeDTO;
import org.aro.drones.api.dto.DroneDTO;
import org.aro.drones.api.dto.DroneStateDTO;
import org.aro.drones.api.service.DroneService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DroneController.class)
public class DroneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DroneService droneService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateOrUpdateDrone() throws Exception {
        when(droneService.createOrUpdateDrone(any())).thenReturn(true);

        this.mockMvc.perform(post("/drone")
                        .content(mapper.writeValueAsString(createDroneDto(500, "LOADING")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    @Test
    public void testCreateOrUpdateDrone_validationException() throws Exception {
        when(droneService.createOrUpdateDrone(any())).thenReturn(true);

        this.mockMvc.perform(post("/drone")
                        .content(mapper.writeValueAsString(createDroneDto(600, "LOADING")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Wrong weight limit")));
    }

    @Test
    public void testCreateOrUpdateDrone_validationExceptionState() throws Exception {
        when(droneService.createOrUpdateDrone(any())).thenReturn(true);

        this.mockMvc.perform(post("/drone")
                        .content(mapper.writeValueAsString(createDroneDto(500, "LOADINK")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Unknown drone's state")));
    }

    @Test
    public void testIsBatteryLevelAllowable() throws Exception {
        when(droneService.isBatteryLevelAllowable("number_1")).thenReturn(true);

        this.mockMvc.perform(get("/drone/battery-allowable")
                        .content(mapper.writeValueAsString(new CodeDTO("number_1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    @Test
    public void testUpdateDroneState() throws Exception {
        when(droneService.updateDroneState(any())).thenReturn(true);

        this.mockMvc.perform(put("/drone/state")
                        .content(mapper.writeValueAsString(createDroneStateDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    private DroneDTO createDroneDto(int weightLimit, String state) {
        DroneDTO droneDTO = new DroneDTO();
        droneDTO.setSerialNumber("number_1");
        droneDTO.setBatteryLevel(50);
        droneDTO.setState(state);
        droneDTO.setModel("CRUISERWEIGHT");
        droneDTO.setWeightLimit(weightLimit);
        return droneDTO;
    }

    private DroneStateDTO createDroneStateDto() {
        DroneStateDTO droneDTO = new DroneStateDTO();
        droneDTO.setSerialNumber("number_1");
        droneDTO.setBatteryLevel(50);
        droneDTO.setState("LOADING");
        return droneDTO;
    }

}
