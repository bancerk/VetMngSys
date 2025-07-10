package dev.patika.vet_management_system.business.concretes;

import dev.patika.vet_management_system.business.abstracts.ICustomerService;
import dev.patika.vet_management_system.core.exceptions.NotFoundException;
import dev.patika.vet_management_system.core.utils.Message;
import dev.patika.vet_management_system.dao.CustomerRepo;
import dev.patika.vet_management_system.entities.Animal;
import dev.patika.vet_management_system.entities.Customer;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service // Marks this class as a Spring service component
public class CustomerManager implements ICustomerService {

    // Repository for customer data access
    private final CustomerRepo customerRepo;

    // Constructor-based dependency injection for repository
    public CustomerManager(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public Customer save(Customer customer) {
        // Save the customer entity and return the result
        return this.customerRepo.save(customer);
    }

    @Override
    public Customer get(Long id) {
        // Retrieve the customer by ID or throw NotFoundException if not found
        return this.customerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));
    }

    @Override
    public Customer update(Customer customer) {
        // Ensure the customer exists before updating
        this.get(customer.getId()); // Will throw NotFoundException if not found
        // Save and return the updated customer entity
        return this.customerRepo.save(customer);
    }

    @Override
    public boolean delete(Long id) {
        // Delete the customer by ID
        this.get(id); // Will throw NotFoundException if not found
        // Ensure the customer exists before deleting
        this.customerRepo.deleteById(id);
        return true;
    }

    @Override
    public Optional<Customer> getName(String name) {
        // Retrieve the customer by name or throw NotFoundException if not found
        Customer customer = this.customerRepo.findByName(name);
        if (customer == null) {
            throw new NotFoundException(Message.NOT_FOUND);
        }
        return Optional.of(customer);
    }

    @Override
    public List<Animal> getAnimalsByCustomerId(Long id) {
        Customer customer = this.get(id); // Ensure the customer exists
        List<Animal> animalList = customer.getAnimalList();
        if (animalList == null || animalList.isEmpty()) {
            throw new NotFoundException(Message.NOT_FOUND);
        }
        return animalList;
    }

}
