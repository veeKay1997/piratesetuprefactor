package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Account;
import nl.hva.miw.pirate_bank_setup.domain.Customer;
import nl.hva.miw.pirate_bank_setup.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CreateAccountService {
    RootRepository repository;

    @Autowired
    public CreateAccountService(RootRepository repository) {
        this.repository = repository;
    }

    public void createCustomerAccount(Customer customer) {
        Account account = new Account(customer, BigDecimal.valueOf(NumberGenerator.randomInt(150000,800000)));
        repository.createCustomerAccount(account);
    }

}
