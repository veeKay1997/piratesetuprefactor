package nl.hva.miw.pirate_bank_setup.service;

import nl.hva.miw.pirate_bank_setup.domain.Customer;
import nl.hva.miw.pirate_bank_setup.domain.PersonalDetails;
import nl.hva.miw.pirate_bank_setup.domain.User;
import nl.hva.miw.pirate_bank_setup.repository.RootRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CreateBank {
    public static final int BANK_USER_ID = 1000;
    private BcryptHashService bcrypt;
    private static final int MIN_ASSET_NUMBER =1000;
    private static final int MAX_ASSET_NUMBER =5000;
    private final GenerateUserAndCustomer generateUserAndCustomer;
    private final GenerateWallet generateWallet;
    private final GenerateAccount generateAccount;
    private final RootRepository repository;

    @Autowired
    public CreateBank(BcryptHashService bcrypt, GenerateWallet createWalletService,
                      GenerateAccount createAccountService,
                      GenerateUserAndCustomer createUserAndCustomerService, RootRepository repository) {
        this.bcrypt = bcrypt;
        this.generateWallet = createWalletService;
        this.generateAccount = createAccountService;
        this.generateUserAndCustomer = createUserAndCustomerService;
        this.repository = repository;
    }

    public void createBank() {
        Customer customer = insertUserAndCustomer();
        insertWallet(customer);
        insertAccount(customer);
    }

    private Customer insertUserAndCustomer() {
        PersonalDetails personalDetails = new PersonalDetails("The", "", "Piratebank");
        User user = generateUserAndCustomer.generateUser(personalDetails);
        user.setUserId(BANK_USER_ID);
        Customer customer = generateUserAndCustomer.generateBankCustomer(BANK_USER_ID,
                "piratebank@piratebank.com",
                "welcomepirates01");
        repository.doUserAndCustomerInsert(user, customer);
        return customer;
    }

    private void insertWallet(Customer customer) {
        repository.createCustomerWallet(generateWallet.createWalletForCustomer(customer,
                MIN_ASSET_NUMBER, MAX_ASSET_NUMBER));
    }

    private void insertAccount(Customer customer) {
        repository.createCustomerAccount(generateAccount.createCustomerAccount(customer));
    }


}


