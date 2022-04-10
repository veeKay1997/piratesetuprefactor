package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Asset;
import nl.hva.miw.pirate_bank_setup.domain.AssetRate;
import nl.hva.miw.pirate_bank_setup.domain.Customer;
import nl.hva.miw.pirate_bank_setup.domain.Order;
import nl.hva.miw.pirate_bank_setup.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class InsertRandomOrderService {
    RootRepository repository;
    private BigDecimal startAmountBuy = BigDecimal.valueOf(1000);
    private BigDecimal startAmountSell = BigDecimal.valueOf(300);


    @Autowired
    public InsertRandomOrderService(RootRepository repository) {
        this.repository = repository;
    }

    public void createRandomCustomerOrder(Customer customer) {
        Asset randomAsset = repository.getCryptoCurrencyList().get(NumberGenerator.randomInt(0, 19));
        boolean buy = false;
        int buyOrSell = NumberGenerator.randomInt(0, 1);
        if (buyOrSell == 1) buy = true;
        if (!buy) {
            repository.insertOrder(new Order(buy, customer, randomAsset, startAmountSell,
                    getCurrentRate(randomAsset.getName()).multiply(BigDecimal.valueOf
                            (NumberGenerator.randomDoubleInRange(1.02, 1.30))).round(new MathContext(4))));
            startAmountSell = startAmountSell.add(BigDecimal.valueOf(NumberGenerator.randomInt(10, 20)));
        }
        else {
            repository.insertOrder(new Order(buy, customer, randomAsset, startAmountBuy,
                    getCurrentRate(randomAsset.getName()).multiply(BigDecimal.valueOf
                            (NumberGenerator.randomDoubleInRange(0.98, 0.80))).round(new MathContext(4))));
            startAmountBuy = startAmountBuy.add(BigDecimal.valueOf(NumberGenerator.randomInt(10, 20)));
        }
    }

    private BigDecimal getCurrentRate(String coinname){
        AssetRate assetRate = repository.getLatestAssetRate(coinname);
        return assetRate.getValue();
    }

}
