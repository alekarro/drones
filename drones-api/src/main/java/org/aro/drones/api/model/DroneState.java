package org.aro.drones.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DroneState {
    private String serialNumber;
    private int batteryLevel;
    private StateEnum state;
    private int loadedWeight;
}
