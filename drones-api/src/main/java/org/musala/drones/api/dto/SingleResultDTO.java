package org.musala.drones.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResultDTO {
    Object result;
    String message;

    public SingleResultDTO(Object result) {
        this.result = result;
    }

    public SingleResultDTO(Object result, String message) {
        this.result = result;
        this.message = message;
    }
}
