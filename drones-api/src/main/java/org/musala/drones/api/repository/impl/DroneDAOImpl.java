package org.musala.drones.api.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musala.drones.api.dto.DroneDTO;
import org.musala.drones.api.dto.DroneStateDTO;
import org.musala.drones.api.repository.DroneDAO;
import org.musala.drones.api.repository.mappers.DroneStateDTOMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DroneDAOImpl implements DroneDAO {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean createDrone(final DroneDTO dto) {
        log.debug("createDrone# dto = {}", dto);
        String insertQuery = "insert into drone (serial_number, model, weight_limit) values (?, ?, ?)";
        return jdbcTemplate.update(
                insertQuery,
                dto.getSerialNumber(),
                dto.getModel(),
                dto.getWeightLimit()
        ) > 0;
    }

    @Override
    public boolean createDroneState(final DroneStateDTO dto) {
        log.debug("createDroneState# dto = {}", dto);
        String insertDroneStateQuery = "insert into drone_state (serial_number, battery_level, state, loaded_weight) values (?, ?, ?, ?)";
        return jdbcTemplate.update(
                insertDroneStateQuery,
                dto.getSerialNumber(),
                dto.getBatteryLevel(),
                dto.getState(),
                dto.getLoadedWeight()
        ) > 0;
    }

    @Override
    public boolean updateDrone(final DroneDTO dto) {
        log.debug("updateDrone# dto = {}", dto);
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("serialNumber", dto.getSerialNumber());
        in.addValue("model", dto.getModel());
        in.addValue("weightLimit", dto.getWeightLimit());

        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        String updateQuery = "update drone set model = :model, weight_limit = :weightLimit where serial_number = :serialNumber";
        return namedTemplate.update(updateQuery, in) > 0;
    }

    @Override
    public boolean updateDroneState(final DroneStateDTO dto) {
        log.debug("updateDroneState# dto = {}", dto);
        StringBuilder query = new StringBuilder("update drone_state set ");
        MapSqlParameterSource in = new MapSqlParameterSource();
        boolean isFirstField = true;
        isFirstField = addChangedField(query, in, dto.getState(), "state", isFirstField);
        isFirstField = addChangedField(query, in, dto.getBatteryLevel(), "battery_level", isFirstField);
        isFirstField = addChangedField(query, in, dto.getLoadedWeight(), "loaded_weight", isFirstField);
        if (!isFirstField) {
            in.addValue("serial_number", dto.getSerialNumber());
            query.append(" where serial_number = :serial_number");
            String queryString = query.toString();
            NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            int res = namedTemplate.update(queryString, in);
            log.debug("updateDroneState# query = {}, res = {}", queryString, res);
            return res > 0;
        }
        return false;
    }

    @Override
    public DroneStateDTO loadDroneState(final String serialNumber) {
        String loadDroneStateQuery = "select s.*, d.weight_limit from drone_state s inner join drone d on d.serial_number = s.serial_number where s.serial_number = ?";
        return jdbcTemplate.queryForObject(loadDroneStateQuery, new DroneStateDTOMapper(), serialNumber);
    }

    @Override
    public boolean isDroneExists(final String serialNumber) {
        String existsQuery = "select count(*) from drone where serial_number = ?";
        Integer res = jdbcTemplate.queryForObject(existsQuery, Integer.class, serialNumber);
        log.debug("isDroneExists# serialNumber = {}, res = {}", serialNumber, res);
        return res != null && res == 1;
    }

    @Override
    public int findBatteryLevel(final String serialNumber) {
        String findBatteryLevelQuery = "select battery_level from drone_state where serial_number = ?";
        Integer res = jdbcTemplate.queryForObject(findBatteryLevelQuery, Integer.class, serialNumber);
        log.debug("findBatteryLevel# serialNumber = {}, res = {}", serialNumber, res);
        return res != null ? res : 0;
    }

    @Override
    public List<DroneStateDTO> getAvailableDrones(final int minBatteryLevel, final Set<String> availableStates) {
        log.debug("getAvailableDrones# started");
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("states", availableStates);
        parameters.addValue("battery_level", minBatteryLevel);
        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        String loadAvailableDrones = "select s.*, d.weight_limit from drone_state s inner join drone d on d.serial_number = s.serial_number " +
                " where s.state in (:states) and s.battery_level >= :battery_level";
        return namedTemplate.query(loadAvailableDrones, parameters, new DroneStateDTOMapper());
    }

    @Override
    public void auditDronesState() {
        String query = "insert into audit_drone (serial_number, battery_level) select serial_number, battery_level from drone_state";
        jdbcTemplate.update(query);
    }

    private boolean addChangedField(
            final StringBuilder query,
            final MapSqlParameterSource in,
            final Object value,
            final String fieldName,
            boolean isFirstField) {
        if (value != null) {
            if (!isFirstField) {
                query.append(",");
            } else {
                isFirstField = false;
            }
            in.addValue(fieldName, value);
            query.append(" ");
            query.append(fieldName).append(" = :").append(fieldName);
        }
        return isFirstField;
    }
}
