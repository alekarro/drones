package org.aro.drones.api.service.impl;

import lombok.RequiredArgsConstructor;
import org.aro.drones.api.repository.DroneDAO;
import org.aro.drones.api.service.AuditService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {
    private final DroneDAO droneDAO;

    @Override
    @Scheduled(cron = "${audit.cron.drone}")
    public void auditDrone() {
        droneDAO.auditDronesState();
    }
}
