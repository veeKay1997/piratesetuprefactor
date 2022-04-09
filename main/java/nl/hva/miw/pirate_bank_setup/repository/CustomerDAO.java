package nl.hva.miw.pirate_bank_setup.repository;


import nl.hva.miw.pirate_bank_setup.domain.*;
import nl.hva.miw.pirate_bank_setup.domain.Address;
import nl.hva.miw.pirate_bank_setup.domain.IdentifyingInformation;
import nl.hva.miw.pirate_bank_setup.domain.PersonalDetails;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


@Repository
public class CustomerDAO  {

    private JdbcTemplate jdbcTemplate;
    private UserDAO userDAO;

    public CustomerDAO(JdbcTemplate jbdcTemplate, UserDAO userDAO) {
        super();
        this.jdbcTemplate = jbdcTemplate;
        this.userDAO = userDAO;
    }

    //ToDo use different method to create a customer that is not dependent on a different DAO, see dictaat domainmapping

    public void create(Customer customer) {
    String sql = "INSERT INTO customer VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
    User user = userDAO.getByUsername(customer.getUserName());
    jdbcTemplate.update(sql, user.getUserId(), customer.getPersonalDetails().getFirstName(),customer.getPersonalDetails().getInFix(),
            customer.getPersonalDetails().getLastName(), customer.getIdentifyingInformation().getDateOfBirth(),
            customer.getIdentifyingInformation().getBsnNumber(), customer.getAddress().getPostalCode(),
            customer.getAddress().getHouseNumber(), customer.getAddress().getHouseNumberAddition(),
            customer.getAddress().getStreet(), customer.getAddress().getCity(),
            customer.getIdentifyingInformation().getIbanNumber());
    }

    public Customer get(Integer id) {
        String sql ="SELECT * FROM customer WHERE user_id = ?";
            Customer customer;
            try {
                customer = jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), id);
            } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
                customer = null;
            }
            return customer;
        }


public class CustomerRowMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        Address address = new Address (rs.getString("street"), rs.getString("house_number"),
                rs.getString("house_number_addition"), rs.getString("postal_code"),
                rs.getString("city"));
        PersonalDetails personalDetails = new PersonalDetails(rs.getString("first_name"),
                rs.getString("infix"), rs.getString("last_name") );
        IdentifyingInformation identifyingInformation = new IdentifyingInformation(rs.getInt("bsn_number"),
                rs.getString("iban_number"), LocalDate.parse(rs.getDate("date_of_birth").toString()));
        Customer customer = new Customer (personalDetails, address, identifyingInformation);
        customer.setUserId(rs.getInt("user_id"));
        return customer;
    }
}

}
