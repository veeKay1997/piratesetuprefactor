package nl.hva.miw.pirate_bank_setup.controller;

import nl.hva.miw.pirate_bank_setup.service.GenerateBankService;
import nl.hva.miw.pirate_bank_setup.service.GenerateOtherCustomersService;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpStatus;
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
        boolean bankGen = generateBankService.createBank();
        if (!bankGen) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Application failed to create the bank, " +
                    "check console for exception");
        }
       boolean customerGen = generateOtherCustomersService.generateCustomers();
       if (!customerGen) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Application failed to create all customers, " +
                   "check console for exception");
       }
       else return ResponseEntity.ok("Database fill completed");
    }
}

    
    
    
    
    
    
    
    
    
    
    
    


