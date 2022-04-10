package nl.hva.miw.pirate_bank_setup.repository;

import nl.hva.miw.pirate_bank_setup.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RootRepository {
    AssetDAO assetDAO;
    AccountDAO accountDAO;
    AssetRateDAO assetRateDAO;
    CustomerDAO customerDAO;
    OrderDAO orderDAO;
    UserDAO userDAO;
    WalletDAO walletDAO;
    WalletHistoryDAO walletHistoryDAO;

    public RootRepository(AssetDAO assetDAO, AccountDAO accountDAO, AssetRateDAO assetRateDAO,
                          CustomerDAO customerDAO, OrderDAO orderDAO, UserDAO userDAO, WalletDAO walletDAO,
                          WalletHistoryDAO walletHistoryDAO) {
        this.assetDAO = assetDAO;
        this.accountDAO = accountDAO;
        this.assetRateDAO = assetRateDAO;
        this.customerDAO = customerDAO;
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.walletDAO = walletDAO;
        this.walletHistoryDAO = walletHistoryDAO;
    }

    public List<Asset> getCryptoCurrencyList() {
       return assetDAO.getAll();
    }

    public User getUserByUsername(String userName) {
        return userDAO.getByUsername(userName);
    }

    public void doUserInsert(User user) {
        userDAO.create(user);
    }

    public void doUserInsertWithId(User user) {
        userDAO.insertWithId(user);
    }

    public void doCustomerInsert(Customer customer) {
        customerDAO.create(customer);
    }

    public void createCustomerWallet(Wallet wallet) {
        walletDAO.create(wallet);
    }

    public void createCustomerAccount(Account account) {
        accountDAO.create(account);
    }

    public void insertWalletHistory(WalletHistory walletHistory) {
        walletHistoryDAO.create(walletHistory);
    }

    public void insertOrder(Order order) {
        orderDAO.save(order);
    }

    public AssetRate getLatestAssetRate(String coinName ) {
        return assetRateDAO.get(coinName);
    }



}
