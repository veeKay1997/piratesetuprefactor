package nl.hva.miw.pirate_bank_setup.repository.account;

import nl.hva.miw.pirate_bank_setup.repository.crypto.RandomNumberGenerator;
import nl.hva.miw.pirate_bank_setup.repository.customer.Customer;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class AccountRepository {

    AccountDAO accountDAO;

    public AccountRepository(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void createAccount(Customer customer, int balance) {
        accountDAO.create(new Account(customer, BigDecimal.valueOf(balance)));
    }

}
