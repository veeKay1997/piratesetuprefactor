package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenerateOtherCustomersService {
    public static final int MIN_ASSET_AMOUNT_CUSTOMER_WALLET = 0;
    public static final int MAX_ASSET_AMOUNT_CUSTOMER_WALLET = 1000;
    public static final int ALGORITHM_CYCLES = 3000;
    private final InsertUserAndCustomerService insertUserAndCustomerService;
    private final InsertWalletService insertWalletService;
    private final InsertAccountService insertAccountService;
    private final InsertWalletHistoryService insertWalletHistoryService;
    private final InsertRandomOrderService insertRandomOrderService;


    @Autowired
    public GenerateOtherCustomersService(InsertUserAndCustomerService insertUserAndCustomerService,
                                         InsertWalletService insertWalletService,
                                         InsertAccountService insertAccountService,
                                         InsertWalletHistoryService insertWalletHistoryService,
                                         InsertRandomOrderService insertRandomOrderService) {
        this.insertUserAndCustomerService = insertUserAndCustomerService;
        this.insertWalletService = insertWalletService;
        this.insertAccountService = insertAccountService;
        this.insertWalletHistoryService = insertWalletHistoryService;
        this.insertRandomOrderService = insertRandomOrderService;
    }

    public boolean generateCustomers(){
        try {
            for (int i = 0; i < ALGORITHM_CYCLES; i++) {
                Customer customer = insertUserAndCustomerService.createRegularUserAndCustomer();
                insertWalletService.createCustomerWallet(customer, MIN_ASSET_AMOUNT_CUSTOMER_WALLET,
                        MAX_ASSET_AMOUNT_CUSTOMER_WALLET);
                insertAccountService.createCustomerAccount(customer);
                insertWalletHistoryService.createCustomerWalletHistory(customer);
                insertRandomOrderService.createRandomCustomerOrder(customer);
                System.out.println("total customer generation cycles completed " + (i + 1));
            }
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        return true;
    }
}
