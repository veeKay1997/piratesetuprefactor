package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PopulateDatabaseService {

    public static final int MIN_ASSET_AMOUNT_CUSTOMER_WALLET = 0;
    public static final int MAX_ASSET_AMOUNT_CUSTOMER_WALLET = 1000;
    private final CreateUserAndCustomerService userAndCustomerService;
    private final CreateWalletService createWalletService;
    private final CreateAccountService createAccountService;
    private final GenerateWalletHistoryService walletHistoryService;
    private final GenerateRandomOrderService randomOrderService;
    public static final int ALGORITHM_CYCLES = 3000;

    @Autowired
    public PopulateDatabaseService(CreateUserAndCustomerService userAndCustomerService,
                                   CreateWalletService createWalletService,
                                   CreateAccountService createAccountService,
                                   GenerateWalletHistoryService walletHistoryService,
                                    GenerateRandomOrderService randomOrderService) {
        this.userAndCustomerService = userAndCustomerService;
        this.createWalletService = createWalletService;
        this.createAccountService = createAccountService;
        this.walletHistoryService = walletHistoryService;
        this.randomOrderService = randomOrderService;
    }

    public void doDatabasePopulate(){
        for (int i = 0; i < ALGORITHM_CYCLES; i++) {
           Customer customer = userAndCustomerService.createUserAndCustomer();
           createWalletService.createWalletForCustomer(customer, MIN_ASSET_AMOUNT_CUSTOMER_WALLET, MAX_ASSET_AMOUNT_CUSTOMER_WALLET);
           createAccountService.createCustomerAccount(customer);
           walletHistoryService.generateAndInsertWalletHistory(customer);
           randomOrderService.generateRandomOrderByCustomer(customer);
           System.out.println("total customer generation cycles completed " + (i+1));
        }
    }
}
