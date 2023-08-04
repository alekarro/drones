package org.musala.drones.api.repository.impl;

import org.musala.drones.api.model.Medication;
import org.musala.drones.api.repository.MedicationDAO;
import org.musala.drones.api.repository.mappers.MedicationWeightMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class MedicationDAOImpl implements MedicationDAO {

    private final JdbcTemplate jdbcTemplate;

    public MedicationDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean createMedication(final Medication medication) {
        final String insertQuery = "insert into medication (code, name, weight, image) values (?, ?, ?, ?)";
        return jdbcTemplate.update(
                insertQuery,
                medication.getCode(),
                medication.getName(),
                medication.getWeight(),
                medication.getImage()
        ) > 0;
    }

    @Override
    public boolean updateMedication(final Medication medication) {
        MapSqlParameterSource in = new MapSqlParameterSource();
        in.addValue("code", medication.getCode());
        in.addValue("name", medication.getName());
        in.addValue("weight", medication.getWeight());
        in.addValue("image", medication.getImage());

        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        String updateQuery = "update medication set name = :name, weight = :weight, image = :image where code = :code";
        return namedTemplate.update(updateQuery, in) > 0;
    }

    @Override
    public int findLightestWeight() {
        String minWeightQuery = "select min(weight) from medication";
        Integer res = jdbcTemplate.queryForObject(minWeightQuery, Integer.class);
        return res != null ? res : 0;
    }

    @Override
    public boolean isMedicationExists(final String code) {
        String existsQuery = "select count(*) from medication where code = ?";
        Integer res = jdbcTemplate.queryForObject(existsQuery, Integer.class, code);
        return  res!= null && res == 1;
    }

    @Override
    public List<Medication> getMedications(final Set<String> codes) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("codes", codes);
        NamedParameterJdbcTemplate namedTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        String getMedicationWeightsQuery = "select code, weight from medication where code in (:codes)";
        return namedTemplate.query(getMedicationWeightsQuery, parameters, new MedicationWeightMapper());
    }

}
