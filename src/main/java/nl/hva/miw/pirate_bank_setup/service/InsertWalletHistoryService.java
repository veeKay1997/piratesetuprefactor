package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Customer;
import nl.hva.miw.pirate_bank_setup.domain.WalletHistory;
import nl.hva.miw.pirate_bank_setup.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

@Service
public class InsertWalletHistoryService {

    public static final int DAYS_IN_YEAR = 365;
    public static final int DAYS_IN_YEAR_OFFSET_BY_ONE = 366;
    public static final int WALLET_VALUE_LOWER_BOUND = 100000;
    public static final int WALLET_VALUE_UPPER_BOUND = 300000;
    RootRepository repository;

    @Autowired
    public InsertWalletHistoryService(RootRepository repository) {
        this.repository = repository;
    }

    public void createCustomerWalletHistory(Customer customer) {
        repository.insertWalletHistory(generateWalletHistory(customer));
    }


    /**
    generates history of total value of wallet from the prior year until yesterday
    multiplied by i to get a wallet value history that always goes up (for fun)
    */

    private WalletHistory generateWalletHistory(Customer customer) {
        Map<Timestamp, BigDecimal> map = new TreeMap<>();
        for (int i = 0; i < DAYS_IN_YEAR; i++) {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(DAYS_IN_YEAR_OFFSET_BY_ONE).plusDays(i));
            map.put(timestamp, new BigDecimal(NumberGenerator.randomInt(WALLET_VALUE_LOWER_BOUND, WALLET_VALUE_UPPER_BOUND))
                    .multiply(BigDecimal.valueOf(i)));
        }
        WalletHistory walletHistory = new WalletHistory(customer, map);
        return walletHistory;
    }

}
