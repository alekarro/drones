package org.musala.drones.api.dto;

public class WrongStateException extends Exception {
    public WrongStateException(String message) {
        super(message);
    }
}
