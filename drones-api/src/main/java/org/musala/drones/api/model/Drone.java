package org.musala.drones.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Drone {
    private String serialNumber;
    private ModelEnum model;
    private int weightLimit;
}
