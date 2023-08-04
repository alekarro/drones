package org.musala.drones.api.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoadingItem {

    private String droneCode;

    @NotEmpty
    @Pattern(regexp = "[A-Z0-9_]+")
    private String medicationCode;

    @Min(value = 1, message = "Min weight limit is 1")
    private int quantity;
}
