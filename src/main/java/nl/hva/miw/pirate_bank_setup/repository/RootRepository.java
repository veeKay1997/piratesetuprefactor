package nl.hva.miw.pirate_bank_setup.repository;

import nl.hva.miw.pirate_bank_setup.repository.account.AccountRepository;
import nl.hva.miw.pirate_bank_setup.repository.crypto.OrderDAO;
import nl.hva.miw.pirate_bank_setup.repository.crypto.OrderRepository;
import nl.hva.miw.pirate_bank_setup.repository.customer.Customer;
import nl.hva.miw.pirate_bank_setup.repository.customer.CustomerRepository;
import nl.hva.miw.pirate_bank_setup.repository.crypto.WalletRepository;
import nl.hva.miw.pirate_bank_setup.repository.wallethistory.WalletHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RootRepository {
    private final CustomerRepository customerRepository;
    private final WalletRepository walletRepository;
    private final OrderRepository orderRepository;
    private final WalletHistoryRepository walletHistoryRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public RootRepository(CustomerRepository customerRepository, OrderDAO orderDAO,
                          WalletRepository walletRepository1, OrderRepository orderRepository, WalletHistoryRepository walletHistoryRepository, AccountRepository accountRepository) {
        this.customerRepository = customerRepository;
        this.walletRepository = walletRepository1;
        this.orderRepository = orderRepository;
        this.walletHistoryRepository = walletHistoryRepository;
        this.accountRepository = accountRepository;
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
        accountRepository.createAccount(customer,minBalance, maxBalance);
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
