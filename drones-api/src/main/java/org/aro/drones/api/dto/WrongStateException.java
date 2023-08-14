package org.aro.drones.api.dto;

public class WrongStateException extends RuntimeException {
    public WrongStateException(String message) {
        super(message);
    }
}
