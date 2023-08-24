package org.aro.drones.api.controllers;

import org.aro.drones.api.model.Medication;
import org.aro.drones.api.service.MedicationService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedicationController.class)
public class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicationService medicationService;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testCreateOrUpdateMedication() throws Exception {
        when(medicationService.createOrUpdateMedication(any())).thenReturn(true);

        this.mockMvc.perform(post("/medication")
                        .content(mapper.writeValueAsString(createMedication()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
    }

    private Medication createMedication() {
        Medication medication = new Medication();
        medication.setCode("MED_1");
        medication.setName("Med-1");
        medication.setWeight(99);
        return medication;
    }
}
