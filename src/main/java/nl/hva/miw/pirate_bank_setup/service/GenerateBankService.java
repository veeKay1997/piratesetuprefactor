package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GenerateBankService {
    private BcryptHashService bcrypt;
    private static final int MIN_ASSET_NUMBER =1000;
    private static final int MAX_ASSET_NUMBER =5000;
    private InsertUserAndCustomerService insertUserAndCustomerService;

    private final InsertWalletService createWalletService;
    private final InsertAccountService insertAccountService;


    @Autowired
    public GenerateBankService(BcryptHashService bcrypt, InsertWalletService createWalletService,
                               InsertAccountService insertAccountService,
                               InsertUserAndCustomerService insertUserAndCustomerService) {
        this.bcrypt = bcrypt;
        this.createWalletService = createWalletService;
        this.insertAccountService = insertAccountService;
        this.insertUserAndCustomerService = insertUserAndCustomerService;
    }

    public void createBank() {
        Customer customer = insertUserAndCustomerService.createBankUserAndCustomer(1000, "piratebank@piratebank.nl",
                "welcomepirates01");
        createWalletService.createCustomerWallet(customer, MIN_ASSET_NUMBER, MAX_ASSET_NUMBER);
        insertAccountService.createCustomerAccount(customer);
        System.out.println("The Pirate Bank was created");
    }

}


