package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Account;
import nl.hva.miw.pirate_bank_setup.domain.Customer;
import nl.hva.miw.pirate_bank_setup.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InsertAccountService {
    public static final int MINIMUM_BALANCE = 150000;
    public static final int MAX_BALANCE = 800000;
    RootRepository repository;

    @Autowired
    public InsertAccountService(RootRepository repository) {
        this.repository = repository;
    }

    public void createCustomerAccount(Customer customer) {
        Account account = new Account(customer, BigDecimal.valueOf(NumberGenerator.randomInt(MINIMUM_BALANCE, MAX_BALANCE)));
        repository.createCustomerAccount(account);
    }

}
