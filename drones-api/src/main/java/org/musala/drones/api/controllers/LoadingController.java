package org.musala.drones.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.musala.drones.api.dto.CodeDTO;
import org.musala.drones.api.dto.LoadingDTO;
import org.musala.drones.api.dto.ResultDTO;
import org.musala.drones.api.service.LoadingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/loading")
@RequiredArgsConstructor
public class LoadingController implements BaseController {
    private final LoadingService loadingService;

    @PostMapping
    public ResultDTO loading(@Valid @RequestBody LoadingDTO loading) {
        return serviceCall(() -> loadingService.loadDrone(loading));
    }

    @GetMapping
    public ResultDTO getLoading(@Valid @RequestBody CodeDTO codeDTO) {
        return serviceCall(() -> loadingService.getLoading(codeDTO.getCode()));
    }

    @GetMapping("/available-drones")
    public ResultDTO getAvailableDrones() {
        return serviceCall(loadingService::getAvailableDrones);
    }

    @PostMapping("/delivered")
    public ResultDTO delivered(@Valid @RequestBody CodeDTO codeDTO) {
        return serviceCall(() -> loadingService.delivered(codeDTO.getCode()));
    }
}
