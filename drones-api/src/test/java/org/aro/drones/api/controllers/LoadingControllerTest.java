package org.aro.drones.api.controllers;

import org.aro.drones.api.dto.CodeDTO;
import org.aro.drones.api.dto.DroneStateDTO;
import org.aro.drones.api.dto.LoadingDTO;
import org.aro.drones.api.model.LoadingItem;
import org.aro.drones.api.service.LoadingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoadingController.class)
public class LoadingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoadingService loadingService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testLoading() throws Exception {
        when(loadingService.loadDrone(any())).thenReturn(true);

        this.mockMvc.perform(post("/loading")
                        .content(mapper.writeValueAsString(createLoadingDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    @Test
    public void testGetLoading() throws Exception {
        when(loadingService.getLoading("number_1")).thenReturn(createDroneStateDto());

        this.mockMvc.perform(get("/loading")
                        .content(mapper.writeValueAsString(new CodeDTO("number_1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"result\":{\"serialNumber\":\"number_1\"")));
    }

    @Test
    public void testGetAvailableDrones() throws Exception {
        this.mockMvc.perform(get("/loading/available-drones")

                        .content(mapper.writeValueAsString(createDroneStateDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"result\":[]")));
        verify(loadingService, times(1)).getAvailableDrones();
    }

    @Test
    public void testDelivery() throws Exception {
        when(loadingService.delivered("number_1")).thenReturn(true);

        this.mockMvc.perform(post("/loading/delivered")
                        .content(mapper.writeValueAsString(new CodeDTO("number_1")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    private LoadingDTO createLoadingDto() {
        LoadingDTO dto = new LoadingDTO();
        dto.setDroneSerialNumber("number_1");
        dto.setLoadingItems(List.of(new LoadingItem("number_1", "med_1", 3)));
        return dto;
    }

    private DroneStateDTO createDroneStateDto() {
        DroneStateDTO droneDTO = new DroneStateDTO();
        droneDTO.setSerialNumber("number_1");
        droneDTO.setBatteryLevel(50);
        droneDTO.setState("LOADING");
        return droneDTO;
    }
}
