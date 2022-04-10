package nl.hva.miw.pirate_bank_setup.repository.crypto;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class OrderDAO {

    private final JdbcTemplate jdbcTemplate;

    public OrderDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Order order) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> insertOrderStatement(order,connection),keyHolder);
    }

    private PreparedStatement insertOrderStatement(Order order, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO `order`(buy,userId,assetName,amount,limitPrice,timeOrderPlaced) values (?,?,?,?,?,?);"
                , Statement.RETURN_GENERATED_KEYS);
        ps.setBoolean(1,order.isBuy());
        ps.setInt(2,order.getUser().getUserId());
        ps.setString(3,order.getAsset().getName());
        ps.setBigDecimal(4,order.getAmount());
        ps.setBigDecimal(5,order.getLimitPrice());
        ps.setTimestamp(6,order.getTimeOrderPlaced());
        return ps;
    }


}
