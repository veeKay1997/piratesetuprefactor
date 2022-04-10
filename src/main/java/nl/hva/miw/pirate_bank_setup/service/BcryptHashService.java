package nl.hva.miw.pirate_bank_setup.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BcryptHashService {
    private final PasswordEncoder passwordEncoder;
    private final String pepper = "WalkThePlankMaggot";

    public BcryptHashService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String hash (String password) {
        return  passwordEncoder.encode(password + pepper);
    }
}
