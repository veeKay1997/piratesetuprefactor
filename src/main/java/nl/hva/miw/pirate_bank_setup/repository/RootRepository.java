package nl.hva.miw.pirate_bank_setup.repository;

import com.github.javafaker.Faker;
import nl.hva.miw.pirate_bank_setup.domain.*;
import nl.hva.miw.pirate_bank_setup.service.utility.BcryptHashService;
import nl.hva.miw.pirate_bank_setup.service.utility.RandomDataGenerator;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class RootRepository {
    public static final int MINIMUM_BALANCE = 150000;
    public static final int MAX_BALANCE = 800000;
    public static final int DAYS_IN_YEAR = 365;
    public static final int DAYS_IN_YEAR_OFFSET_BY_ONE = 366;
    public static final int WALLET_VALUE_LOWER_BOUND = 100000;
    public static final int WALLET_VALUE_UPPER_BOUND = 300000;
    public static final double MIN_PERCENTAGE_ABOVE_MARKET = 1.02;
    public static final double MAX_PERCENTAGE_ABOVE_MARKET = 1.30;
    public static final double MIN_PERCENTAGE_BELOW_MARKET = 0.98;
    public static final double MAX_PERCENTAGE_BELOW_MARKET = 0.80;
    private BigDecimal startAmountBuy = BigDecimal.valueOf(1000);
    private BigDecimal startAmountSell = BigDecimal.valueOf(300);




    private static final Faker faker = new Faker(new Locale("nl"));

    private final AssetDAO assetDAO;
    private final AccountDAO accountDAO;
    private final AssetRateDAO assetRateDAO;
    private final CustomerDAO customerDAO;
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final WalletDAO walletDAO;
    private final WalletHistoryDAO walletHistoryDAO;
    private final BcryptHashService bcrypt;

    public RootRepository(AssetDAO assetDAO, AccountDAO accountDAO, AssetRateDAO assetRateDAO,
                          CustomerDAO customerDAO, OrderDAO orderDAO, UserDAO userDAO, WalletDAO walletDAO,
                          WalletHistoryDAO walletHistoryDAO, BcryptHashService bcryptHashService) {
        this.assetDAO = assetDAO;
        this.accountDAO = accountDAO;
        this.assetRateDAO = assetRateDAO;
        this.customerDAO = customerDAO;
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
        this.walletDAO = walletDAO;
        this.walletHistoryDAO = walletHistoryDAO;
        this.bcrypt = bcryptHashService;
    }

    private List<Asset> getCryptoCurrencyList() {
       return assetDAO.getAll();
    }

    private User getUserByUsername(String userName) {
        return userDAO.getByUsername(userName);
    }

    private void doUserInsert(User user) {
        userDAO.create(user);
    }

    private void doUserInsertWithId(User user) {
        userDAO.insertWithId(user);
    }

    private void doCustomerInsert(Customer customer) {
        customerDAO.create(customer);
    }

    private void createCustomerWalletHelper(Wallet wallet) {
        walletDAO.create(wallet);
    }

    private void createCustomerAccount(Account account) {
        accountDAO.create(account);
    }

    private void insertWalletHistory(WalletHistory walletHistory) {
        walletHistoryDAO.create(walletHistory);
    }

    private void insertOrder(Order order) {
        orderDAO.save(order);
    }

    private AssetRate getLatestAssetRate(String coinName ) {
        return assetRateDAO.get(coinName);
    }

    public void createCustomerAccount(Customer customer) {
        Account account = new Account(customer, BigDecimal.valueOf(RandomDataGenerator.randomInt(MINIMUM_BALANCE, MAX_BALANCE)));
        createCustomerAccount(account);
    }

    public Customer createBankUserAndCustomer(int bankId, String username, String password) {
        User bank = new User(username, bcrypt.hash(password));
        bank.setUserId(bankId);
       doUserInsertWithId(bank);
        Customer customer = generateCustomer(bank, generatePersonalDetails());
        doCustomerInsert(customer);
        return customer;
    }

    public Customer createRegularUserAndCustomer() {
        PersonalDetails personalDetails = generatePersonalDetails();
        User user = generateUser(personalDetails);
        doUserInsert(user);
        Customer customer = generateCustomer(user, personalDetails);
        doCustomerInsert(customer);
        return customer;
    }

    private PersonalDetails generatePersonalDetails() {
        return new PersonalDetails(faker.name().firstName(), null, faker.name().lastName());
    }

    private Customer generateCustomer(User user, PersonalDetails personalDetails) {
        String postcode = faker.address().zipCode();
        String spaceReplaced = postcode.replace(" ", "");
        Address address =  new Address(faker.address().streetName(), faker.address().streetAddressNumber(), ""
                , spaceReplaced, faker.address().cityName());
        IdentifyingInformation identifyingInformation = new IdentifyingInformation(
                RandomDataGenerator.generateFakeBSNNumber(), Iban.random(CountryCode.NL).toString(), RandomDataGenerator.randomBirthday());
        Customer customer = new Customer(personalDetails, address, identifyingInformation);
        customer.setUserName(user.getUserName());
        customer.setUserId(getUserByUsername(customer.getUserName()).getUserId());
        return customer;
    }

    private User generateUser(PersonalDetails personalDetails) {
        String email = personalDetails.getFirstName().toLowerCase()+ "."+ personalDetails.getLastName().toLowerCase()+
                RandomDataGenerator.randomInt(0,9) + RandomDataGenerator.randomInt(0,9)+ RandomDataGenerator.randomInt(0,9)+ RandomDataGenerator. randomInt(0,9)+
                RandomDataGenerator.randomInt(0,9)+
                RandomDataGenerator.randomInt(0,9)+ RandomDataGenerator.randomInt(0,9)+ RandomDataGenerator.randomInt(0,9) + RandomDataGenerator.randomInt(0,9)+
                RandomDataGenerator.randomInt(0,9)+ RandomDataGenerator.randomInt(0,9)+ RandomDataGenerator.randomInt(0,9)+ "@" + "piratebank.com";
        return new User(email, bcrypt.hash("123456789"));
    }

    public void createCustomerWallet(Customer customer, int minAmount, int maxAmount) {
        List<Asset> cryptoCurrencies = getCryptoCurrencyList();
        Wallet customerWallet = createCustomerWalletHelper(cryptoCurrencies, minAmount, maxAmount);
        customerWallet.setCustomer(customer);
        createCustomerWalletHelper(customerWallet);
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

    public void createCustomerWalletHistory(Customer customer) {
        insertWalletHistory(generateWalletHistory(customer));
    }

    /**
     generates history of total value of wallet from the prior year until yesterday
     multiplied by i to get a wallet value history that always goes up (for fun)
     */


    private WalletHistory generateWalletHistory(Customer customer) {
        Map<Timestamp, BigDecimal> map = new TreeMap<>();
        for (int i = 0; i < DAYS_IN_YEAR; i++) {
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(DAYS_IN_YEAR_OFFSET_BY_ONE).plusDays(i));
            map.put(timestamp, new BigDecimal(RandomDataGenerator.randomInt(WALLET_VALUE_LOWER_BOUND, WALLET_VALUE_UPPER_BOUND))
                    .multiply(BigDecimal.valueOf(i)));
        }
        return new WalletHistory(customer, map);
    }

    public void createRandomCustomerOrder(Customer customer) {
        Asset randomAsset = getCryptoCurrencyList().get(RandomDataGenerator.randomInt(0, 19));
        boolean buy = false;
        int buyOrSell = RandomDataGenerator.randomInt(0, 1);
        if (buyOrSell == 1) buy = true;
        if (!buy) {
            insertOrder(new Order(buy, customer, randomAsset, startAmountSell,
                    assetRateDAO.get(randomAsset.getName()).getValue().multiply(BigDecimal.valueOf
                            (RandomDataGenerator.randomDoubleInRange(MIN_PERCENTAGE_ABOVE_MARKET, MAX_PERCENTAGE_ABOVE_MARKET))).round(new MathContext(4))));
            startAmountSell = startAmountSell.add(BigDecimal.valueOf(RandomDataGenerator.randomInt(10, 20)));
        }
        else {
          insertOrder(new Order(buy, customer, randomAsset, startAmountBuy,
                  assetRateDAO.get(randomAsset.getName()).getValue().multiply(BigDecimal.valueOf
                            (RandomDataGenerator.randomDoubleInRange(MIN_PERCENTAGE_BELOW_MARKET, MAX_PERCENTAGE_BELOW_MARKET))).round(new MathContext(4))));
            startAmountBuy = startAmountBuy.add(BigDecimal.valueOf(RandomDataGenerator.randomInt(10, 20)));
        }
    }

}
