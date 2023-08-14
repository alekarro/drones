package org.aro.drones.api.repository.mappers;

import org.aro.drones.api.dto.DroneStateDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DroneStateDTOMapper implements RowMapper<DroneStateDTO> {

    @Override
    public DroneStateDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        DroneStateDTO entity = new DroneStateDTO();
        entity.setSerialNumber(rs.getString("serial_number"));
        entity.setState(rs.getString("state"));
        entity.setBatteryLevel(rs.getInt("battery_level"));
        entity.setWeightLimit(rs.getInt("weight_limit"));
        entity.setLoadedWeight(rs.getInt("loaded_weight"));
        return entity;
    }
}
