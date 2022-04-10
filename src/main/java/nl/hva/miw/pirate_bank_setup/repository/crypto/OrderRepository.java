package nl.hva.miw.pirate_bank_setup.repository.crypto;

import nl.hva.miw.pirate_bank_setup.repository.customer.Customer;
import nl.hva.miw.pirate_bank_setup.repository.customer.RandomDataGenerator;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.MathContext;

@Repository
public class OrderRepository {
    private BigDecimal startAmountBuy = BigDecimal.valueOf(1000);
    private BigDecimal startAmountSell = BigDecimal.valueOf(300);

    AssetDAO assetDAO;
    AssetRateDAO assetRateDAO;
    OrderDAO orderDAO;

    public OrderRepository(AssetDAO assetDAO, AssetRateDAO assetRateDAO, OrderDAO orderDAO) {
        this.assetDAO = assetDAO;
        this.assetRateDAO = assetRateDAO;
        this.orderDAO = orderDAO;
    }

    public void createRandomCustomerOrder(Customer customer, double minPercentageSell, double maxPercentageSell,
    double minPercentageBuy, double maxPercentageBuy) {
        Asset randomAsset = assetDAO.getAll().get(RandomDataGenerator.randomInt(0, 19));
        boolean buy = false;
        int buyOrSell = RandomDataGenerator.randomInt(0, 1);
        if (buyOrSell == 1) buy = true;
        if (!buy) {
            orderDAO.save(new Order(buy, customer, randomAsset, startAmountSell,
                    assetRateDAO.get(randomAsset.getName()).getValue().multiply(BigDecimal.valueOf
                            (RandomDataGenerator.randomDoubleInRange(minPercentageSell, maxPercentageSell))).
                            round(new MathContext(4))));
            startAmountSell = startAmountSell.add(BigDecimal.valueOf(RandomDataGenerator.randomInt(10, 20)));
        }
        else {
            orderDAO.save(new Order(buy, customer, randomAsset, startAmountBuy,
                    assetRateDAO.get(randomAsset.getName()).getValue().multiply(BigDecimal.valueOf
                            (RandomDataGenerator.randomDoubleInRange(minPercentageBuy, maxPercentageBuy))).
                            round(new MathContext(4))));
            startAmountBuy = startAmountBuy.add(BigDecimal.valueOf(RandomDataGenerator.randomInt(10, 20)));
        }
    }

}
