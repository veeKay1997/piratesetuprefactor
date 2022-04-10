package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Customer;
import nl.hva.miw.pirate_bank_setup.domain.WalletHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.TreeMap;

@Service
public class GenerateWalletHistory {

    @Autowired
    public GenerateWalletHistory() {
    }

    public WalletHistory generateWalletHistory(Customer customer) {
        Map<Timestamp, BigDecimal> map = new TreeMap<>();
        for (int i = 0; i < 365; i++) {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(366).plusDays(i));
            map.put(timestamp, new BigDecimal(NumberGenerator.randomInt(100000, 300000)).multiply(BigDecimal.valueOf(i)));
        }
        WalletHistory walletHistory = new WalletHistory(customer, map);
        return walletHistory;
    }





}