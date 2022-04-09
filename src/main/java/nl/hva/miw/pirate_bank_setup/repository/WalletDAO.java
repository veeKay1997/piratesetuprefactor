package nl.hva.miw.pirate_bank_setup.repository;

import nl.hva.miw.pirate_bank_setup.domain.Asset;
import nl.hva.miw.pirate_bank_setup.domain.Wallet;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class WalletDAO  {
    private JdbcTemplate jdbcTemplate;

    public WalletDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void create(Wallet wallet) {
        String sql = "INSERT INTO wallet VALUES (?, ?, ?);";
        List<Asset> assets = new ArrayList<>();
        List<BigDecimal> bigDecimals = new ArrayList<>();

        for (Map.Entry entry: wallet.getAssetsInWallet().entrySet()) {
            Asset asset = (Asset) entry.getKey();
            BigDecimal bigDecimal = (BigDecimal) entry.getValue();
            assets.add(asset);
            bigDecimals.add(bigDecimal);
        }

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, wallet.getCustomer().getUserId());
                ps.setString(2, assets.get(i).getName());
                ps.setBigDecimal(3, bigDecimals.get(i));
            }
            @Override
            public int getBatchSize() {
                return wallet.getAssetsInWallet().size();
            }
        });
    }

    public void addSingleAssetToWallet(int userId, String assetname, BigDecimal amount) {
        String sql = "INSERT INTO wallet VALUES (?, ?, ?);";
        jdbcTemplate.update(sql, userId, assetname, amount );
    }


    public Wallet get(Integer id) {
        String sql = "SELECT * FROM wallet WHERE user_id = ?";
        return jdbcTemplate.query(sql, new WalletResultSetExtractor(), id);
    }


    private class WalletResultSetExtractor implements ResultSetExtractor<Wallet> {
        public Wallet extractData(ResultSet rs) throws SQLException, DataAccessException {
            Wallet wallet = new Wallet();
            Map<Asset, BigDecimal> map = new HashMap<>();
            if (rs.next()) {
                map.put(new Asset(rs.getString("name")), rs.getBigDecimal("amount"));
                while (rs.next()) {
                    map.put(new Asset(rs.getString("name")), rs.getBigDecimal("amount"));
                }
                wallet.setAssetsInWallet(map);
            }
            return wallet;
        }
    }

}