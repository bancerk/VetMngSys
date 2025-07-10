package dev.patika.vet_management_system.api;

// Import necessary classes for controller functionality, data transfer, validation, and HTTP handling
import dev.patika.vet_management_system.core.modelMapper.ModelMapperService;
import dev.patika.vet_management_system.business.concretes.CustomerManager;
import dev.patika.vet_management_system.core.utils.Result;
import dev.patika.vet_management_system.core.utils.ResultData;
import dev.patika.vet_management_system.core.utils.ResultHelper;
import dev.patika.vet_management_system.dto.request.CustomerSaveRequest;
import dev.patika.vet_management_system.dto.request.CustomerUpdateRequest;
import dev.patika.vet_management_system.dto.response.AnimalResponse;
import dev.patika.vet_management_system.dto.response.CustomerResponse;
import dev.patika.vet_management_system.entities.Animal;
import dev.patika.vet_management_system.entities.Customer;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// Mark this class as a REST controller to handle HTTP requests
@RestController
// Set the base URL path for all endpoints in this controller
@RequestMapping("/customer")
public class CustomerController {

    // Service for customer-related business logic
    private final CustomerManager customerService;
    // Service for mapping between DTOs and entities
    private final ModelMapperService modelMapper;

    // Constructor-based dependency injection for services
    public CustomerController(CustomerManager customerService, ModelMapperService modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    // Endpoint to create a new customer
    // Accepts a POST request at /customer/save
    // Expects a valid CustomerSaveRequest in the request body
    // Returns the created customer data with HTTP 201 status
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<CustomerResponse> save(@Valid @RequestBody CustomerSaveRequest customerSaveRequest) {
        // Map the incoming DTO to a Customer entity
        Customer saveCustomer = this.modelMapper.forRequest().map(customerSaveRequest, Customer.class);
        // Save the customer using the service
        this.customerService.save(saveCustomer);
        // Map the saved entity to a response DTO
        CustomerResponse customerResponse = this.modelMapper.forResponse().map(saveCustomer, CustomerResponse.class);
        // Return a standardized response
        return ResultHelper.created(customerResponse);
    }

    // Endpoint to retrieve a customer by ID
    // Accepts a GET request at /customer/{id}
    // Returns the customer data with HTTP 200 status
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CustomerResponse> findById(@PathVariable Long id) {
        // Retrieve the customer entity by ID
        Customer customer = this.customerService.get(id);
        // Map the entity to a response DTO
        CustomerResponse customerResponse = this.modelMapper.forResponse().map(customer, CustomerResponse.class);
        // Return a standardized response
        return ResultHelper.successData(customerResponse);
    }

    // Endpoint to update an existing customer
    // Accepts a PUT request at /customer/update
    // Expects a valid CustomerUpdateRequest in the request body
    // Returns the updated customer data with HTTP 200 status
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CustomerResponse> update(@Valid @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        // Map the incoming DTO to a Customer entity
        Customer updateCustomer = this.modelMapper.forRequest().map(customerUpdateRequest, Customer.class);
        // Update the customer using the service
        this.customerService.update(updateCustomer);
        // Map the updated entity to a response DTO
        CustomerResponse customerResponse = this.modelMapper.forResponse().map(updateCustomer, CustomerResponse.class);
        // Return a standardized response
        return ResultHelper.successData(customerResponse);
    }

    // Endpoint to delete a customer by ID
    // Accepts a DELETE request at /customer/delete/{id}
    // Returns a success response with HTTP 200 status
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") Long id) {
        // Delete the customer using the service
        this.customerService.delete(id);
        // Return a standardized success response
        return ResultHelper.success();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<CustomerResponse> findByName(@RequestParam String name) {
        // Retrieve the customer entity by name
        Optional<Customer> customer = this.customerService.getName(name);
        // Map the entity to a response DTO if present
        return customer.map(c -> this.modelMapper.forResponse().map(c, CustomerResponse.class));
    }

    @GetMapping("/{id}/animals")
    @ResponseStatus(HttpStatus.OK)
    public List<AnimalResponse> findAnimalsByCustomerId(@PathVariable Long id) {
        // Retrieve the list of animals associated with the customer by ID
        List<Animal> animalList = this.customerService.getAnimalsByCustomerId(id);
        // Map the list of animals to a list of response DTOs
        return animalList.stream()
            .map(animal -> this.modelMapper.forResponse().map(animal, AnimalResponse.class))
            .toList();
    }

}
