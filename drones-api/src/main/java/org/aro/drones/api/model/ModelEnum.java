package org.aro.drones.api.model;

public enum ModelEnum {
    LIGHTWEIGHT("Lightweight"),
    MIDDLEWEIGHT("Middleweight"),
    CRUISERWEIGHT("Cruiserweight"),
    HEAVYWEIGHT("Heavyweight");

    ModelEnum(String readableName) {
        this.readableName = readableName;
    }
    private final String readableName;
}
