package nl.hva.miw.pirate_bank_setup.controller;

import nl.hva.miw.pirate_bank_setup.service.GenerateBankService;
import nl.hva.miw.pirate_bank_setup.service.GenerateOtherCustomersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LauncherController {

    GenerateBankService generateBankService;
    GenerateOtherCustomersService generateOtherCustomersService;


    @Autowired
    public LauncherController(GenerateBankService generateBankService, GenerateOtherCustomersService createUsersAndCustomersService) {
        this.generateBankService = generateBankService;
        this.generateOtherCustomersService = createUsersAndCustomersService;
    }

   @GetMapping(value = "/start")
    public ResponseEntity<String> startDatabaseFill() {
        generateBankService.createBank();
        generateOtherCustomersService.generateCustomers();
        return ResponseEntity.ok("Database fill completed");
    }
}

    
    
    
    
    
    
    
    
    
    
    
    


