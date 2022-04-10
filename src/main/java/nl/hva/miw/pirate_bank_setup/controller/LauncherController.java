package nl.hva.miw.pirate_bank_setup.controller;

import nl.hva.miw.pirate_bank_setup.service.CreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LauncherController {

    CreateService createService;


    @Autowired
    public LauncherController( CreateService createService) {
        this.createService = createService;

    }

   @GetMapping(value = "/start")
    public ResponseEntity<String> startDatabaseFill() {
        boolean bankGen = createService.createBank();
        if (!bankGen) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Application failed to create the bank, " +
                    "check console for exception");
        }
       boolean customerGen = createService.createCustomers();
       if (!customerGen) {
           return ResponseEntity.status(HttpStatus.CONFLICT).body("Application failed to create all customers, " +
                   "check console for exception");
       }
       else return ResponseEntity.ok("Database fill completed");
    }
}

    
    
    
    
    
    
    
    
    
    
    
    


