package nl.hva.miw.pirate_bank_setup.repository.account;

import nl.hva.miw.pirate_bank_setup.repository.customer.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class AccountDAO  {
    private final JdbcTemplate jdbcTemplate;

    public AccountDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Account account) {
        String sql = "INSERT INTO account VALUES (?,?);";
        jdbcTemplate.update(sql, account.getCustomer().getUserId(), account.getBalance());
    }

    public Account get(Integer id) {
        String sql = "SELECT * FROM account WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, new AccountRowMapper(), id);
    }

    private class AccountRowMapper implements RowMapper<Account> {
        @Override
        public Account mapRow(ResultSet rs, int rowNum) throws SQLException {
           Customer customer = new Customer();
           customer.setUserId(rs.getInt("user_id"));
           return new Account (customer, rs.getBigDecimal("balance"));
        }
    }
}
