package nl.hva.miw.pirate_bank_setup.repository.customer;

import com.github.javafaker.Faker;
import com.github.rkumsher.date.RandomDateUtils;
import nl.hva.miw.pirate_bank_setup.repository.RootRepository;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

public class RandomDataGenerator {
    RootRepository repository;
    private static final Faker faker = new Faker(new Locale("nl"));

    public RandomDataGenerator(RootRepository repository, BcryptHashService bcryptHashService) {
        this.repository = repository;
    }

    public static final int OFFSET_YEARS = 30;

    public static LocalDate randomBirthday(){
        LocalDate today = LocalDate.now();
        return RandomDateUtils.randomLocalDateBefore(today.minusYears(OFFSET_YEARS));
    }

    public static int randomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static double randomDoubleInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        double shifted = scaled + min;
        return shifted;
    }

    public static int generateFakeBSNNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            stringBuilder.append(faker.number().numberBetween(0,9));
        }
        return Integer.parseInt(stringBuilder.toString());
    }
}
