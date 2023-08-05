package org.musala.drones.api.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.musala.drones.api.dto.MedicationDTO;
import org.musala.drones.api.model.LoadingItem;
import org.musala.drones.api.repository.LoadingDAO;
import org.musala.drones.api.repository.mappers.LoadingItemMapper;
import org.musala.drones.api.repository.mappers.MedicationDTOMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LoadingDAOImpl implements LoadingDAO {

    private final JdbcTemplate jdbcTemplate;

    @Value("${spring.sql.db-batch-size}")
    private int batchSize;

    @Override
    public List<LoadingItem> getLoadingItems(final String droneCode) {
        log.debug("getLoadingItems# droneCode = {}", droneCode);
        String getDroneLoadingItemsQuery = "select * from loading where drone_code = ?";
        return jdbcTemplate.query(getDroneLoadingItemsQuery, new LoadingItemMapper(), droneCode);
    }

    @Override
    public boolean batchCreateLoading(final String droneCode, final List<LoadingItem> loadingItems) {
        log.debug("batchCreateLoading# droneCode = {}", droneCode);
        String insertLoadingItemQuery = "insert into loading (quantity, drone_code, medication_code) values (?, ?, ?)";
        return saveLoadingInBatch(droneCode, loadingItems, insertLoadingItemQuery);
    }

    @Override
    public boolean batchUpdateLoading(final String droneCode, final List<LoadingItem> loadingItems) {
        log.debug("batchUpdateLoading# droneCode = {}", droneCode);
        String updateLoadingItemQuery = "update loading set quantity = ? where drone_code = ? and medication_code = ?";
        return saveLoadingInBatch(droneCode, loadingItems, updateLoadingItemQuery);
    }

    @Override
    public List<MedicationDTO> getLoadedMedications(final String droneCode) {
        log.debug("getLoadedMedications# droneCode = {}", droneCode);
        String getDroneLoadedMedicationsInfoQuery = "select m.code, m.name, m.weight, l.quantity from loading l " +
                "inner join medication m on l.medication_code = m.code where l.drone_code = ?";
        return jdbcTemplate.query(getDroneLoadedMedicationsInfoQuery, new MedicationDTOMapper(), droneCode);
    }

    @Override
    public boolean deleteDroneLoadings(final String droneCode) {
        log.debug("deleteDroneLoadings# droneCode = {}", droneCode);
        String deleteLoadingItemsQuery = "delete from loading where drone_code = ?";
        return jdbcTemplate.update(deleteLoadingItemsQuery, droneCode) >= 0;
    }

    private boolean saveLoadingInBatch(final String droneCode, final List<LoadingItem> loadingItems, final String query) {
        return jdbcTemplate.batchUpdate(query,
                loadingItems,
                batchSize,
                (PreparedStatement ps, LoadingItem loadingItem) -> {
                    ps.setInt(1, loadingItem.getQuantity());
                    ps.setString(2, droneCode);
                    ps.setString(3, loadingItem.getMedicationCode());
                }
        )[0][0] > 0;
    }
}
