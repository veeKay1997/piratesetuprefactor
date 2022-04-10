package nl.hva.miw.pirate_bank_setup.repository.crypto;

import java.util.Random;

public class RandomNumberGenerator {

    public RandomNumberGenerator() {
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
}
