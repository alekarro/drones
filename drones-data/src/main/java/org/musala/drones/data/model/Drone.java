package org.musala.drones.data.model;

import lombok.Data;

@Data
public class Drone {
    private String serialNumber;
    private ModelEnum model;
    private int weightLimit;
    private int batteryCapacity;
    private StateEnum state;
}
