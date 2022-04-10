package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Asset;
import nl.hva.miw.pirate_bank_setup.domain.Customer;
import nl.hva.miw.pirate_bank_setup.domain.Wallet;
import nl.hva.miw.pirate_bank_setup.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CreateWalletService {

    RootRepository repository;

    @Autowired
    public CreateWalletService(RootRepository repository) {
        this.repository = repository;
    }

    public void createWalletForCustomer(Customer customer, int minAmount, int maxAmount) {
        List<Asset> cryptoCurrencies = repository.getCryptoCurrencyList();
        Wallet customerWallet = createCustomerWallet(cryptoCurrencies, minAmount, maxAmount);
        customerWallet.setCustomer(customer);
        repository.createCustomerWallet(customerWallet);
    }

    private Wallet createCustomerWallet (List<Asset> crypto, int minAmount, int maxAmount) {
        Wallet wallet = new Wallet();
        Map<Asset, BigDecimal> map = new HashMap<>();
        for (Asset asset: crypto) {
            map.put(asset, BigDecimal.valueOf(NumberGenerator.randomInt(minAmount,maxAmount)) );
        }
        wallet.setAssetsInWallet(map);
        return wallet;
    }
}
