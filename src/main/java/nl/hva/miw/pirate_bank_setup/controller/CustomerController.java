package nl.hva.miw.pirate_bank_setup.controller;

import nl.hva.miw.pirate_bank_setup.service.CreateRegularCustomers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    private final CreateRegularCustomers createRegularCustomers;

    @Autowired
    public CustomerController(CreateRegularCustomers createRegularCustomers) {
        this.createRegularCustomers = createRegularCustomers;
    }

   @GetMapping(value = "/createcustomers")
    public void createCustomers() {
      createRegularCustomers.insertCustomers();
    }

}

    
    
    
    
    
    
    
    
    
    
    
    


