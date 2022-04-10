package nl.hva.miw.pirate_bank_setup.repository.crypto;

import nl.hva.miw.pirate_bank_setup.repository.customer.Customer;
import nl.hva.miw.pirate_bank_setup.repository.customer.RandomDataGenerator;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WalletRepository {
    WalletDAO walletDAO;
    AssetDAO assetDAO;

    public WalletRepository(WalletDAO walletDAO, AssetDAO assetDAO) {
        this.walletDAO = walletDAO;
        this.assetDAO = assetDAO;
    }

    public void createCustomerWallet(Customer customer, int minAmount, int maxAmount) {
        List<Asset> cryptoCurrencies = assetDAO.getAll();
        Wallet customerWallet = createCustomerWalletHelper(cryptoCurrencies, minAmount, maxAmount);
        customerWallet.setCustomer(customer);
        walletDAO.create(customerWallet);
    }

    private Wallet createCustomerWalletHelper(List<Asset> crypto, int minAmount, int maxAmount) {
        Wallet wallet = new Wallet();
        Map<Asset, BigDecimal> map = new HashMap<>();
        for (Asset asset: crypto) {
            map.put(asset, BigDecimal.valueOf(RandomDataGenerator.randomInt(minAmount,maxAmount)) );
        }
        wallet.setAssetsInWallet(map);
        return wallet;
    }
}
