package dev.patika.customer_service.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.patika.customer_service.entities.Customer;
import dev.patika.customer_service.repo.CustomerRepo;
import jakarta.ws.rs.NotFoundException;

@Service
public class CustomerManager implements ICustomerService {

    private final CustomerRepo customerRepo;

    public CustomerManager(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    @Override
    public Customer save(Customer customer) {
        return this.customerRepo.save(customer);
    }

    @Override
    public Customer get(Long id) {
        return this.customerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with ID: " + id));
    }

    @Override
    public Customer update(Customer customer) {
        this.get(customer.getId());
        return this.customerRepo.save(customer);
    }

    @Override
    public boolean delete(Long id) {
        this.get(id);
        this.customerRepo.deleteById(id);
        return true;
    }

    @Override
    public Optional<Customer> getName(String name) {
        Customer customer = this.customerRepo.findByName(name);
        if (customer == null) {
            throw new NotFoundException("Customer not found with name: " + name);
        }
        return Optional.of(customer);
    }

    //TODO: Implement this method to retrieve animals by customer ID
/*     @Override
    public List<Animal> getAnimalsByCustomerId(Long id) {
         Customer customer = this.get(id);
        List<Animal> animalList = customer.getAnimalList();
        if (animalList == null || animalList.isEmpty()) {
            throw new NotFoundException("No animals found for customer with ID: " + id);
        }
         return animalList;
    } */

}
