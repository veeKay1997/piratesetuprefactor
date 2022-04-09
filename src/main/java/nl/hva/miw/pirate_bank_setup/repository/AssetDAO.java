package nl.hva.miw.pirate_bank_setup.repository;


import nl.hva.miw.pirate_bank_setup.domain.Asset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AssetDAO {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;

    public AssetDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Asset> getAll(){
        String sql = "Select * From asset;";
        List<Asset> assets = new ArrayList<>();
        try{
            assets = jdbcTemplate.query(sql, new assetRowmapper());
        } catch (EmptyResultDataAccessException emptyResultDataAccessException){
            assets.isEmpty();
        }
        return assets;
    }

    private class assetRowmapper implements RowMapper<Asset> {

        public Asset mapRow(ResultSet rs, int rowNum) throws SQLException {
            Asset asset = new Asset();
            asset.setName(rs.getString("name"));
            asset.setAbbreviation(rs.getString("abbreviation"));
            return asset;
        }
    }

}
