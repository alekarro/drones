package org.musala.drones.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDTO {
    Object result;
    String message;

    public ResultDTO(Object result) {
        this.result = result;
    }

    public ResultDTO(Object result, String message) {
        this.result = result;
        this.message = message;
    }
}
