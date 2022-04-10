package nl.hva.miw.pirate_bank_setup.service;

import com.github.javafaker.Faker;
import nl.hva.miw.pirate_bank_setup.domain.*;
import nl.hva.miw.pirate_bank_setup.repository.UserDAO;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class GenerateUserAndCustomer {
    private final BcryptHashService bcrypt;
    Faker faker = new Faker(new Locale("nl"));
    UserDAO userDAO;

    @Autowired
    public GenerateUserAndCustomer(  BcryptHashService bcryptHashService) {
        this.bcrypt = bcryptHashService;
    }

    public Customer generateBankCustomer(int bankId, String username, String password) {
        User user = new User(username, bcrypt.hash(password));
        user.setUserId(bankId);
        return generateCustomer(user, generatePersonalDetails());
    }

    public Customer generateRegularCustomer(User user, PersonalDetails personalDetails) {
        return  generateCustomer(user, personalDetails);
    }

    public PersonalDetails generatePersonalDetails() {
        return new PersonalDetails(faker.name().firstName(), null, faker.name().lastName());
    }

   public User generateUser(PersonalDetails personalDetails) {
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
        customer.setUserId(userDAO.getByUsername(customer.getUserName()).getUserId());
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
