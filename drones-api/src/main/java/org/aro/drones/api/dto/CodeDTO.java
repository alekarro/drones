package org.aro.drones.api.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeDTO {
    @NotEmpty
    private String code;
}
