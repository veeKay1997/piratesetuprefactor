package nl.hva.miw.pirate_bank_setup.repository.account;

import nl.hva.miw.pirate_bank_setup.repository.customer.Customer;
import nl.hva.miw.pirate_bank_setup.repository.customer.RandomDataGenerator;

import java.math.BigDecimal;

public class AccountRepository {

    AccountDAO accountDAO;

    public AccountRepository(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public void createAccount(Customer customer,int minBalance, int maxBalance) {
        accountDAO.create(new Account(customer, BigDecimal.valueOf(RandomDataGenerator.randomInt(minBalance, maxBalance))));
    }

}
