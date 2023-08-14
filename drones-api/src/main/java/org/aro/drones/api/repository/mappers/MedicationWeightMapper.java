package org.aro.drones.api.repository.mappers;

import org.aro.drones.api.model.Medication;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicationWeightMapper implements RowMapper<Medication> {

    @Override
    public Medication mapRow(ResultSet rs, int rowNum) throws SQLException {
        Medication entity = new Medication();
        entity.setCode(rs.getString("code"));
        entity.setWeight(rs.getInt("weight"));
        return entity;
    }
}
