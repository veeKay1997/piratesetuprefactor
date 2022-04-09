package nl.hva.miw.pirate_bank_setup.service;

import com.github.rkumsher.date.RandomDateUtils;

import java.time.LocalDate;
import java.util.Random;

public class NumberGenerator {

    public static LocalDate randomBirthday(){
        LocalDate today = LocalDate.now();
        return RandomDateUtils.randomLocalDateBefore(today.minusYears(30));
    }

    public static int randomInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static double randomInRange(double min, double max) {
        Random random = new Random();
        double range = max - min;
        double scaled = random.nextDouble() * range;
        double shifted = scaled + min;
        return shifted; // == (rand.nextDouble() * (max-min)) + min;
    }


}
