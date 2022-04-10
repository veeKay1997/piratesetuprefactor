package nl.hva.miw.pirate_bank_setup.repository.user;

import com.github.javafaker.Faker;
import nl.hva.miw.pirate_bank_setup.service.utility.BcryptHashService;
import nl.hva.miw.pirate_bank_setup.service.utility.RandomDataGenerator;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Repository;

import java.util.Locale;

@Repository
public class CustomerRepository {

    private UserDAO userDAO;
    private CustomerDAO customerDAO;
    private BcryptHashService bcrypt;
    private static final Faker faker = new Faker(new Locale("nl"));


    public CustomerRepository(UserDAO userDAO, CustomerDAO customerDAO, BcryptHashService bcryptHashService) {
        this.userDAO = userDAO;
        this.customerDAO = customerDAO;
        this.bcrypt = bcryptHashService;
    }

    public Customer createBankUserAndCustomer(int bankId, String username, String password) {
        User bank = new User(username, bcrypt.hash(password));
        bank.setUserId(bankId);
        userDAO.insertWithId(bank);
        Customer customer = generateCustomer(bank, generatePersonalDetails());
        customerDAO.create(customer);
        return customer;
    }
    public Customer createRegularUserAndCustomer() {
        PersonalDetails personalDetails = generatePersonalDetails();
        User user = generateUser(personalDetails);
        userDAO.create(user);
        Customer customer = generateCustomer(user, personalDetails);
        customerDAO.create(customer);
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
        customer.setUserId(userDAO.getByUsername(customer.getUserName()).getUserId());
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

}
