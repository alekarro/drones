package org.aro.drones.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicationDTO {
    private String code;
    private String name;
    private int weight;
    private int loadedQuantity;
}
