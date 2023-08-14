package org.aro.drones.api.repository.mappers;

import org.aro.drones.api.dto.MedicationDTO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MedicationDTOMapper implements RowMapper<MedicationDTO> {

    @Override
    public MedicationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        MedicationDTO entity = new MedicationDTO();
        entity.setCode(rs.getString("code"));
        entity.setName(rs.getString("name"));
        entity.setWeight(rs.getInt("weight"));
        entity.setLoadedQuantity(rs.getInt("quantity"));
        return entity;
    }
}
