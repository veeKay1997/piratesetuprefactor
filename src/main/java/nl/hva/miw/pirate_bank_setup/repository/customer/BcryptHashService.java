package nl.hva.miw.pirate_bank_setup.repository.customer;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptHashService {
    private final PasswordEncoder passwordEncoder;
    private static final String PEPPER = "WalkThePlankMaggot";

    public BcryptHashService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String hash (String password) {
        return passwordEncoder.encode(password + PEPPER);
    }
}
