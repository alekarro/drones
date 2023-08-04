package org.musala.drones.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.musala.drones.api.model.ModelEnum;
import org.musala.drones.api.model.StateEnum;
import org.musala.drones.api.validation.EnumValidator;
import org.musala.drones.api.validation.WeightLimitValidator;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
public class DroneDTO {
    @Value("${drone.weight-limit}")
    private int maxWeight;

    @NotEmpty
    @Size(min = 5, max = 100, message = "Serial number should have at least 5 characters and max 100 characters")
    private String serialNumber;

    @EnumValidator(
            enumClass = ModelEnum.class,
            message = "Unknown drone's model"
    )
    private String model;

    @WeightLimitValidator
    private int weightLimit;

    @Min(value = 0, message = "Min battery level is 0 %")
    @Max(value = 100, message = "Max battery level is 100 %")
    private int batteryLevel;

    @EnumValidator(
            enumClass = StateEnum.class,
            message = "Unknown drone's state",
            canBeEmpty = true
    )
    private String state;

}
