package org.musala.drones.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.musala.drones.api.model.LoadingItem;

import java.util.List;

@Getter
@Setter
public class LoadingDTO {

    @NotEmpty
    @Size(min = 5, max = 100, message = "Serial number should have at least 5 characters and max 100 characters")
    private String droneSerialNumber;

    @NotEmpty
    private List<LoadingItem> loadingItems;
}
