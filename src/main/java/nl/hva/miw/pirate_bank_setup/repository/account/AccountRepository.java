package nl.hva.miw.pirate_bank_setup.repository.account;

import nl.hva.miw.pirate_bank_setup.repository.crypto.RandomNumberGenerator;
import nl.hva.miw.pirate_bank_setup.repository.customer.Customer;

import java.math.BigDecimal;

public class AccountRepository {

    AccountDAO accountDAO;

    public AccountRepository(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void createAccount(Customer customer,int minBalance, int maxBalance) {
        accountDAO.create(new Account(customer, BigDecimal.valueOf(RandomNumberGenerator.randomInt(minBalance, maxBalance))));
    }

}
