package org.musala.drones.api.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Medication {

    @NotEmpty
    @Pattern(regexp = "[A-Z0-9_]+")
    private String code;

    @NotEmpty
    @Pattern(regexp = "[A-Za-z0-9\\-_]+")
    private String name;

    @Min(value = 0, message = "Weight cannot be less than 0")
    private int weight;

    private byte[] image;
}
