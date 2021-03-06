package nl.hva.miw.pirate_bank_setup.repository;

import nl.hva.miw.pirate_bank_setup.repository.account.AccountRepository;
import nl.hva.miw.pirate_bank_setup.repository.crypto.OrderRepository;
import nl.hva.miw.pirate_bank_setup.repository.crypto.RandomNumberGenerator;
import nl.hva.miw.pirate_bank_setup.repository.crypto.WalletRepository;
import nl.hva.miw.pirate_bank_setup.repository.customer.Customer;
import nl.hva.miw.pirate_bank_setup.repository.customer.CustomerRepository;
import nl.hva.miw.pirate_bank_setup.repository.crypto.WalletHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;


@Component
public class Creator {
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
    public static final int MIN_AMOUNT_CUSTOMER_WALLET = 0;
    public static final int MAX_AMOUNT_CUSTOMER_WALLET = 100;


    private final CustomerRepository customerRepository;
    private final WalletRepository walletRepository;
    private final OrderRepository orderRepository;
    private final WalletHistoryRepository walletHistoryRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public Creator(CustomerRepository customerRepository, WalletRepository walletRepository, OrderRepository orderRepository,
                   WalletHistoryRepository walletHistoryRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.walletRepository = walletRepository;
        this.orderRepository = orderRepository;
        this.walletHistoryRepository = walletHistoryRepository;
        this.accountRepository = accountRepository;
    }

    public boolean createCustomers (){
        try {
            for (int i = 0; i < ALGORITHM_CYCLES; i++) {
                Customer customer = customerRepository.createRegularUserAndCustomer();
                walletRepository.createCustomerWallet(customer, MIN_AMOUNT_CUSTOMER_WALLET, MAX_AMOUNT_CUSTOMER_WALLET);
                accountRepository.createAccount(customer, RandomNumberGenerator.randomInt(MINIMUM_BALANCE, MAX_BALANCE));
                walletHistoryRepository.createCustomerWalletHistory(customer, WALLET_VALUE_LOWER_BOUND, WALLET_VALUE_UPPER_BOUND);
                orderRepository.createRandomCustomerOrder(customer, MIN_PERCENTAGE_ABOVE_MARKET,
                        MAX_PERCENTAGE_ABOVE_MARKET, MIN_PERCENTAGE_BELOW_MARKET, MAX_PERCENTAGE_BELOW_MARKET);
                System.out.println("total customer generation cycles completed " + (i + 1));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createBank() {
        try {
            Customer customer = customerRepository.createBankUserAndCustomer(BANK_ID, "piratebank@piratebank.nl",
                    "welcomepirates01");
            walletRepository.createCustomerWallet(customer, MIN_ASSET_NUMBER, MAX_ASSET_NUMBER);
            accountRepository.createAccount(customer, BANK_BALANCE);
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        System.out.println("The Pirate Bank was created");
        return true;
    }
}
