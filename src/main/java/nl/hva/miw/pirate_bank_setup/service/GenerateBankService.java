package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Customer;
import nl.hva.miw.pirate_bank_setup.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class GenerateBankService {


    private static final int MIN_ASSET_NUMBER =1000;
    private static final int MAX_ASSET_NUMBER =5000;
    public static final int BANK_ID = 1000;
    private final RootRepository rootRepository;

    @Autowired
    public GenerateBankService( RootRepository rootRepository) {
        this.rootRepository = rootRepository;
    }

    public boolean createBank() {
        try {
        Customer customer = rootRepository.createBankUserAndCustomer(BANK_ID, "piratebank@piratebank.nl",
                "welcomepirates01");
        rootRepository.createCustomerWallet(customer, MIN_ASSET_NUMBER, MAX_ASSET_NUMBER);
        rootRepository.createCustomerAccount(customer);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return false;
        }
        System.out.println("The Pirate Bank was created");
        return true;
    }

}


