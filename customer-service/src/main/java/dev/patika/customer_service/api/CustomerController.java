package dev.patika.customer_service.api;

import dev.patika.customer_service.service.CustomerManager;

/* import org.springframework.http.HttpStatus; */
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerManager customerService;

    public CustomerController(CustomerManager customerService) {
        this.customerService = customerService;
    }

    //TODO: Implement the save method to handle customer creation
    //TODO: Implement the findById method to retrieve customer by ID
    //TODO: Implement the update method to handle customer updates
    //TODO: Implement the findByName method to retrieve customer by name
    //TODO: Implement the findAnimalsByCustomerId method to retrieve animals by customer ID

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        this.customerService.delete(id);
    }

}
