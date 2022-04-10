package nl.hva.miw.pirate_bank_setup.repository.crypto;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class AssetRateDAO {
    private final JdbcTemplate jdbcTemplate;

    public AssetRateDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AssetRate get(String id) {
        String sql = "Select * from asset_rates where asset_name = ? ORDER BY timestamp DESC LIMIT 1;";
        return jdbcTemplate.queryForObject(sql, new AssetRateRowMapper(),id);
    }

   private static class AssetRateRowMapper implements RowMapper<AssetRate> {
        @Override
        public AssetRate mapRow(ResultSet rs, int rowNum) throws SQLException {
            Timestamp timeStamp = rs.getTimestamp("timestamp");
            BigDecimal value = rs.getBigDecimal("value");
            Asset associatedAssetName = new Asset();
            associatedAssetName.setName(rs.getString("asset_name"));
            return new AssetRate(associatedAssetName, timeStamp, value);
        }
    }

}

