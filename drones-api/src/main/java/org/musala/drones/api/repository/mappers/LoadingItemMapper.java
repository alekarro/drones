package org.musala.drones.api.repository.mappers;

import org.musala.drones.api.model.LoadingItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadingItemMapper implements RowMapper<LoadingItem> {

    @Override
    public LoadingItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        LoadingItem entity = new LoadingItem();
        entity.setMedicationCode(rs.getString("medication_code"));
        entity.setDroneCode(rs.getString("drone_code"));
        entity.setQuantity(rs.getInt("quantity"));
        return entity;
    }
}
