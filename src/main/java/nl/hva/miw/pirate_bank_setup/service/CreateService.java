package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.repository.RootRepository;
import nl.hva.miw.pirate_bank_setup.repository.user.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateService {
    public static final int MIN_ASSET_AMOUNT_CUSTOMER_WALLET = 0;
    public static final int MAX_ASSET_AMOUNT_CUSTOMER_WALLET = 1000;
    public static final int ALGORITHM_CYCLES = 3000;
    public static final int BANK_ID = 1000;
    private static final int MIN_ASSET_NUMBER =1000;
    private static final int MAX_ASSET_NUMBER =5000;
    public static final int MINIMUM_BALANCE = 150000;
    public static final int MAX_BALANCE = 800000;
    public static final int WALLET_VALUE_LOWER_BOUND = 100000;
    public static final int WALLET_VALUE_UPPER_BOUND = 300000;
    public static final int BANK_BALANCE = 5000000;
    public static final double MIN_PERCENTAGE_ABOVE_MARKET = 1.02;
    public static final double MAX_PERCENTAGE_ABOVE_MARKET = 1.30;
    public static final double MIN_PERCENTAGE_BELOW_MARKET = 0.98;
    public static final double MAX_PERCENTAGE_BELOW_MARKET = 0.80;
    private final RootRepository repository;

    @Autowired
    public CreateService(RootRepository repository) {
        this.repository = repository;
    }

    public boolean createCustomers(){
        try {
            for (int i = 0; i < ALGORITHM_CYCLES; i++) {
                Customer customer = repository.createRegularCustomer();
                repository.createCustomerWallet(customer, MIN_ASSET_AMOUNT_CUSTOMER_WALLET,
                        MAX_ASSET_AMOUNT_CUSTOMER_WALLET);
                repository.createCustomerAccount(customer, MINIMUM_BALANCE, MAX_BALANCE);
                repository.createWalletHistory(customer, WALLET_VALUE_LOWER_BOUND, WALLET_VALUE_UPPER_BOUND);
                repository.createRandomOrder(customer, MIN_PERCENTAGE_ABOVE_MARKET, MAX_PERCENTAGE_ABOVE_MARKET,
                        MIN_PERCENTAGE_BELOW_MARKET, MAX_PERCENTAGE_BELOW_MARKET);
                System.out.println("total customer generation cycles completed " + (i + 1));
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        return true;
    }

    public boolean createBank() {
        try {
            Customer customer = repository.createBank(BANK_ID, "piratebank@piratebank.nl",
                    "welcomepirates01");
            repository.createCustomerWallet(customer, MIN_ASSET_NUMBER, MAX_ASSET_NUMBER);
            repository.createCustomerAccount(customer, BANK_BALANCE, BANK_BALANCE);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        System.out.println("The Pirate Bank was created");
        return true;
    }




}
