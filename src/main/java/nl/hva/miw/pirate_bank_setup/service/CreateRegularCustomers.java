package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Customer;
import nl.hva.miw.pirate_bank_setup.domain.PersonalDetails;
import nl.hva.miw.pirate_bank_setup.domain.User;
import nl.hva.miw.pirate_bank_setup.domain.Wallet;
import nl.hva.miw.pirate_bank_setup.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateRegularCustomers {
    public static final int ALGORITHM_CYCLES = 3000;
    public static final int MIN_ASSET_AMOUNT_CUSTOMER_WALLET = 0;
    public static final int MAX_ASSET_AMOUNT_CUSTOMER_WALLET = 1000;
    private final GenerateUserAndCustomer generateUserAndCustomer;
    private final GenerateWallet generateWallet;
    private final GenerateAccount generateAccount;
    private final GenerateWalletHistory generateWalletHistory;
    private final GenerateRandomOrder generateRandomOrder;
    public final RootRepository rootRepository;

    @Autowired
    public CreateRegularCustomers(GenerateUserAndCustomer userAndCustomerService,
                                  GenerateWallet generateWallet,
                                  GenerateAccount generateAccount,
                                  GenerateWalletHistory walletHistoryService,
                                  GenerateRandomOrder randomOrderService, RootRepository rootRepository) {
        this.generateUserAndCustomer = userAndCustomerService;
        this.generateWallet = generateWallet;
        this.generateAccount = generateAccount;
        this.generateWalletHistory = walletHistoryService;
        this.generateRandomOrder = randomOrderService;
        this.rootRepository = rootRepository;
    }

    public void insertCustomers() {
        for (int i = 0; i < ALGORITHM_CYCLES; i++) {
           Customer customer = insertCustomerAndUser();
           insertWallet(customer);
           insertAccount(customer);
           insertWalletHistory(customer);
           insertRandomOrder(customer);
           System.out.println("total customer generation cycles completed " + (i+1));
        }
    }

    private Customer insertCustomerAndUser() {
        PersonalDetails personalDetails = generateUserAndCustomer.generatePersonalDetails();
        User user = generateUserAndCustomer.generateUser(personalDetails);
        Customer customer = generateUserAndCustomer.generateRegularCustomer(user, personalDetails);
        rootRepository.doUserAndCustomerInsert(user, customer);
        return customer;
    }

    private void insertWallet(Customer customer) {
        Wallet wallet = generateWallet.createWalletForCustomer(customer, MIN_ASSET_AMOUNT_CUSTOMER_WALLET,
                MAX_ASSET_AMOUNT_CUSTOMER_WALLET);
        rootRepository.createCustomerWallet(wallet);
    }

    private void insertAccount(Customer customer) {
         rootRepository.createCustomerAccount(generateAccount.createCustomerAccount(customer));
    }

    private void insertWalletHistory(Customer customer) {
       rootRepository.insertWalletHistory(generateWalletHistory.generateWalletHistory(customer));
    }

    private void insertRandomOrder(Customer customer) {
        rootRepository.insertOrder(generateRandomOrder.generateRandomOrderByCustomer(customer));
    }


}
