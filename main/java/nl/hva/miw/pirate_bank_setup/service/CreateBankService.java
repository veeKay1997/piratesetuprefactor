package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreateBankService {

    private BcryptHashService bcrypt;
    private static final int MIN_ASSET_NUMBER =1000;
    private static final int MAX_ASSET_NUMBER =5000;
    private CreateUserAndCustomerService createUserAndCustomerService;

    private final CreateWalletService createWalletService;
    private final CreateAccountService createAccountService;


    @Autowired
    public CreateBankService(BcryptHashService bcrypt, CreateWalletService createWalletService,
                                   CreateAccountService createAccountService) {
        this.bcrypt = bcrypt;
        this.createWalletService = createWalletService;
        this.createAccountService = createAccountService;

    }

    public void createBank() {
        Customer customer = createUserAndCustomerService.createBankUserAndCustomer(1000, "piratebank@piratebank.nl",
                "welcomepirates01");
        createWalletService.createWalletForCustomer(customer, MIN_ASSET_NUMBER, MAX_ASSET_NUMBER);
        createAccountService.createCustomerAccount(customer);
    }

}


