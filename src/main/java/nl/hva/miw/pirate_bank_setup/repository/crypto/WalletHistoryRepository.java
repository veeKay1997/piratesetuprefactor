package nl.hva.miw.pirate_bank_setup.repository.crypto;

import nl.hva.miw.pirate_bank_setup.repository.customer.Customer;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;


@Repository
public class WalletHistoryRepository {

    WalletHistoryDAO walletHistoryDAO;
    public static final int DAYS_IN_YEAR = 365;
    public static final int DAYS_IN_YEAR_OFFSET_BY_ONE = 366;


    public WalletHistoryRepository(WalletHistoryDAO walletHistoryDAO) {
        this.walletHistoryDAO = walletHistoryDAO;
    }

    public void createCustomerWalletHistory(Customer customer, int lowerBound, int upperBound) {
        walletHistoryDAO.create(generateWalletHistory(customer, lowerBound, upperBound));
    }

    /**
     generates history of total value of wallet from the prior year until yesterday
     multiplied by i to get a wallet value history that always goes up (for fun)
     */

    private WalletHistory generateWalletHistory(Customer customer, int lowerBound, int upperBound) {
        Map<Timestamp, BigDecimal> map = new TreeMap<>();
        for (int i = 0; i < DAYS_IN_YEAR; i++) {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(DAYS_IN_YEAR_OFFSET_BY_ONE).plusDays(i));
            map.put(timestamp, new BigDecimal(RandomNumberGenerator.randomInt(lowerBound, upperBound))
                    .multiply(BigDecimal.valueOf(i)));
        }
        return new WalletHistory(customer, map);
    }


}
