package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Customer;
import nl.hva.miw.pirate_bank_setup.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateOtherCustomersService {
    public static final int MIN_ASSET_AMOUNT_CUSTOMER_WALLET = 0;
    public static final int MAX_ASSET_AMOUNT_CUSTOMER_WALLET = 1000;
    public static final int ALGORITHM_CYCLES = 3000;
    private final RootRepository repository;

    @Autowired
    public GenerateOtherCustomersService(RootRepository repository) {
        this.repository = repository;
    }

    public boolean generateCustomers(){
        try {
            for (int i = 0; i < ALGORITHM_CYCLES; i++) {
                Customer customer = repository.createRegularUserAndCustomer();
                repository.createCustomerWallet(customer, MIN_ASSET_AMOUNT_CUSTOMER_WALLET,
                        MAX_ASSET_AMOUNT_CUSTOMER_WALLET);
                repository.createCustomerAccount(customer);
                repository.createCustomerWalletHistory(customer);
                repository.createRandomCustomerOrder(customer);
                System.out.println("total customer generation cycles completed " + (i + 1));
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        return true;
    }
}
