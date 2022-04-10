package nl.hva.miw.pirate_bank_setup.controller;

import nl.hva.miw.pirate_bank_setup.service.CreateBank;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class BankController {

        private final CreateBank createBank;

    public BankController(CreateBank createBank) {
        this.createBank = createBank;
    }

    @GetMapping(value = "/createbank")
        public void createCustomers() {
            createBank.createBank();
        }


}
