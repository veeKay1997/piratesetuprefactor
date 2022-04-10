package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Account;
import nl.hva.miw.pirate_bank_setup.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class GenerateAccount {

    @Autowired
    public GenerateAccount() {
    }

    public Account createCustomerAccount(Customer customer) {
        return new Account(customer, BigDecimal.valueOf(NumberGenerator.randomInt(150000,800000)));
    }

}
