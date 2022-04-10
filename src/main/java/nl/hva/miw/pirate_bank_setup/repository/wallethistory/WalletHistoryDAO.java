package nl.hva.miw.pirate_bank_setup.repository.wallethistory;


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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Repository
public class WalletHistoryDAO {

    private final JdbcTemplate jdbcTemplate;

    public WalletHistoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public void create(WalletHistory walletHistory) {
        String sql = "INSERT INTO `wallethistory` VALUES (?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatement(walletHistory));
    }


    public WalletHistory get(Integer id) {
        String sql = "SELECT * FROM wallethistory WHERE customer_user_id = ? LIMIT 365";
        return jdbcTemplate.query(sql, new WalletHistoryResultSetExtractor(), id);
    }


    private static class WalletHistoryResultSetExtractor implements ResultSetExtractor<WalletHistory> {
        @Override
        public WalletHistory extractData(ResultSet rs) throws SQLException, DataAccessException {
            WalletHistory walletHistory = new WalletHistory();
            Map<Timestamp, BigDecimal> map = new TreeMap<>();
            if (rs.next()) {
                map.put(rs.getTimestamp("timestamp"), rs.getBigDecimal("value"));
            }
            while (rs.next()) {
                map.put(rs.getTimestamp("timestamp"), rs.getBigDecimal("value"));
            }
            walletHistory.setWalletValueHistory(map);
            return walletHistory;
        }
    }

    private static class BatchPreparedStatement implements BatchPreparedStatementSetter {
        List<Timestamp> timestamps = new ArrayList<>();
        List<BigDecimal> bigDecimals = new ArrayList<>();
        WalletHistory walletHistory;

        public BatchPreparedStatement(WalletHistory walletHistory) {
             this.walletHistory = walletHistory;
             historyMapToLists();
        }

        public void historyMapToLists() {
            for (Map.Entry<Timestamp, BigDecimal> entry : walletHistory.getWalletValueHistory().entrySet()) {
                timestamps.add(entry.getKey());
                bigDecimals.add(entry.getValue());
            }
        }
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, walletHistory.getCustomer().getUserId());
                ps.setTimestamp(2, timestamps.get(i));
                ps.setBigDecimal(3, bigDecimals.get(i));
            }
            @Override
            public int getBatchSize() {
                return walletHistory.getWalletValueHistory().size();
            }
    }
}
