package nl.hva.miw.pirate_bank_setup.repository;

import nl.hva.miw.pirate_bank_setup.repository.account.Account;
import nl.hva.miw.pirate_bank_setup.repository.account.AccountDAO;
import nl.hva.miw.pirate_bank_setup.repository.order.OrderDAO;
import nl.hva.miw.pirate_bank_setup.repository.order.OrderRepository;
import nl.hva.miw.pirate_bank_setup.repository.user.Customer;
import nl.hva.miw.pirate_bank_setup.repository.user.CustomerRepository;
import nl.hva.miw.pirate_bank_setup.repository.wallet.WalletRepository;
import nl.hva.miw.pirate_bank_setup.repository.wallethistory.WalletHistoryRepository;
import nl.hva.miw.pirate_bank_setup.service.utility.RandomDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class RootRepository {
    private final CustomerRepository customerRepository;
    private final WalletRepository walletRepository;
    private final AccountDAO accountDAO;
    private final OrderRepository orderRepository;
    private final WalletHistoryRepository walletHistoryRepository;


    @Autowired
    public RootRepository(AccountDAO accountDAO, CustomerRepository customerRepository, OrderDAO orderDAO,
                          WalletRepository walletRepository1, OrderRepository orderRepository, WalletHistoryRepository walletHistoryRepository) {
        this.accountDAO = accountDAO;
        this.customerRepository = customerRepository;
        this.walletRepository = walletRepository1;
        this.orderRepository = orderRepository;
        this.walletHistoryRepository = walletHistoryRepository;
    }

    public Customer createBank(int id, String username, String password) {
        return customerRepository.createBankUserAndCustomer(id, username, password);
    }

    public Customer createRegularCustomer() {
        return customerRepository.createRegularUserAndCustomer();
    }

    public void createCustomerWallet(Customer customer, int minAmount, int maxAmount) {
        walletRepository.createCustomerWallet(customer, minAmount, maxAmount);
    }

    public void createCustomerAccount(Customer customer, int minBalance, int maxBalance) {
        accountDAO.create(new Account(customer, BigDecimal.valueOf(RandomDataGenerator.randomInt(minBalance, maxBalance))));
    }

    public void createWalletHistory(Customer customer, int lowerBound, int upperBound) {
        walletHistoryRepository.createCustomerWalletHistory(customer, lowerBound, upperBound);
    }

    public void createRandomOrder(Customer customer, double minPercentageSell, double maxPercentageSell,
                                  double minPercentageBuy, double maxPercentageBuy) {
        orderRepository.createRandomCustomerOrder(customer, minPercentageSell, maxPercentageSell,
                minPercentageBuy, maxPercentageBuy);
    }




}
