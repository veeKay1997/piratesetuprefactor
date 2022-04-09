package nl.hva.miw.pirate_bank_setup.service;

import com.github.javafaker.Faker;
import nl.hva.miw.pirate_bank_setup.domain.*;
import nl.hva.miw.pirate_bank_setup.domain.Address;
import nl.hva.miw.pirate_bank_setup.domain.IdentifyingInformation;
import nl.hva.miw.pirate_bank_setup.domain.PersonalDetails;
import nl.hva.miw.pirate_bank_setup.repository.RootRepository;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class CreateUserAndCustomerService {
    private final RootRepository repository;
    private final BcryptHashService bcrypt;
    Faker faker = new Faker(new Locale("nl"));

    @Autowired
    public CreateUserAndCustomerService(RootRepository repository, BcryptHashService bcryptHashService) {
        this.repository = repository;
        this.bcrypt = bcryptHashService;
    }

    public Customer createBankUserAndCustomer(int bankId, String username, String password) {
        User user = new User(username, bcrypt.hash(password));
        user.setUserId(bankId);
        Customer customer = generateCustomer(user, generatePersonalDetails());
        repository.doUserAndCustomerInsert(user, customer);
        return customer;
    }

    public Customer createUserAndCustomer() {
        PersonalDetails personalDetails = generatePersonalDetails();
        User user = generateUser(personalDetails);
        Customer customer = generateCustomer(user, personalDetails);
        repository.doUserAndCustomerInsert(user, customer);
        return customer;
    }

    private PersonalDetails generatePersonalDetails() {
        return new PersonalDetails(faker.name().firstName(), null, faker.name().lastName());

    }

   private User generateUser(PersonalDetails personalDetails) {
        String email = personalDetails.getFirstName().toLowerCase()+ "."+ personalDetails.getLastName().toLowerCase()+
                NumberGenerator.randomInt(0,9) + NumberGenerator.randomInt(0,9)+ NumberGenerator.randomInt(0,9)+ NumberGenerator. randomInt(0,9)+ NumberGenerator.randomInt(0,9)+
                NumberGenerator.randomInt(0,9)+ NumberGenerator.randomInt(0,9)+ NumberGenerator.randomInt(0,9) + NumberGenerator.randomInt(0,9)+
                NumberGenerator.randomInt(0,9)+ NumberGenerator.randomInt(0,9)+ NumberGenerator.randomInt(0,9)+ "@" + "piratebank.com";
        return new User(email, bcrypt.hash("123456789"));
    }

    private Customer generateCustomer(User user, PersonalDetails personalDetails) {
        String postcode = faker.address().zipCode();
        String spaceReplaced = postcode.replace(" ", "");
        Address address =  new Address(faker.address().streetName(), faker.address().streetAddressNumber(), ""
                , spaceReplaced, faker.address().cityName());
        IdentifyingInformation identifyingInformation = new IdentifyingInformation(
                generateFakeBSNNumber(), Iban.random(CountryCode.NL).toString(), NumberGenerator.randomBirthday());
        Customer customer = new Customer(personalDetails, address, identifyingInformation);
        customer.setUserName(user.getUserName());
        customer.setUserId(repository.getUserByUsername(customer.getUserName()).getUserId());
        return customer;
        }

   private int generateFakeBSNNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            stringBuilder.append(faker.number().numberBetween(0,9));
        }
        return Integer.parseInt(stringBuilder.toString());
    }






}
