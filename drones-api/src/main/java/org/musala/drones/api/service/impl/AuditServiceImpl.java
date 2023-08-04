package org.musala.drones.api.service.impl;

import org.musala.drones.api.repository.DroneDAO;
import org.musala.drones.api.service.AuditService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AuditServiceImpl implements AuditService {
    private final DroneDAO droneDAO;

    public AuditServiceImpl(DroneDAO droneDAO) {
        this.droneDAO = droneDAO;
    }

    @Override
    @Scheduled(cron="${audit.cron.drone}")
    public void auditDrone() {
        droneDAO.auditDronesState();
    }
}
