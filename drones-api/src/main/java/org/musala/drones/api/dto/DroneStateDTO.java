package org.musala.drones.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.musala.drones.api.model.StateEnum;
import org.musala.drones.api.validation.EnumValidator;

import java.util.List;

@Getter
@Setter
public class DroneStateDTO {
    @NotEmpty
    @Size(min = 5, max = 100, message = "Serial number should have at least 5 characters and max 100 characters")
    private String serialNumber;

    @Min(value = 0, message = "Min battery level is 0 %")
    @Max(value = 100, message = "Max battery level is 100 %")
    private Integer batteryLevel;

    @EnumValidator(
            enumClass = StateEnum.class,
            message = "Unknown drone's model",
            canBeEmpty = true
    )
    private String state;

    private Integer loadedWeight;

    private int weightLimit;
    private List<MedicationDTO> loadedMedications;

    public DroneStateDTO() {}

    public DroneStateDTO(DroneDTO droneDTO) {
        this.serialNumber = droneDTO.getSerialNumber();
        this.batteryLevel = droneDTO.getBatteryLevel();
        this.state = droneDTO.getState();
    }

}
