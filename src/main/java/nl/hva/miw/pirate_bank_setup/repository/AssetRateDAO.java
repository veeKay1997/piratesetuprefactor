package nl.hva.miw.pirate_bank_setup.repository;


import nl.hva.miw.pirate_bank_setup.domain.Asset;
import nl.hva.miw.pirate_bank_setup.domain.AssetRate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class AssetRateDAO {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private JdbcTemplate jdbcTemplate;

    public AssetRateDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AssetRate get(String id) {
        String sql = "Select * from asset_rates where asset_name = ? ORDER BY timestamp DESC LIMIT 1;";
        return jdbcTemplate.queryForObject(sql, new assetRateRowmapper(),id);
    }

    public void create(AssetRate assetRate) {
        String sql = "Insert Into asset_rates(asset_name, timestamp, value) values(?, ?, ?);";
        try {
            jdbcTemplate.update(sql, assetRate.getAsset().getName(), assetRate.getTimestamp(), assetRate.getValue());
        } catch (DataAccessException exception) {
            log.debug(exception.getMessage());
        }
    }

   private class assetRateRowmapper implements RowMapper<AssetRate> {
        @Override
        public AssetRate mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp timeStamp = rs.getTimestamp("timestamp");
            BigDecimal value = rs.getBigDecimal("value");
            Asset associatedAssetName = new Asset();
            associatedAssetName.setName(rs.getString("asset_name"));
            AssetRate assetRate = new AssetRate(associatedAssetName, timeStamp, value);
            return assetRate;
        }
    }

}

