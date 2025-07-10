package dev.patika.vet_management_system.business.abstracts;

import java.util.List;
import java.util.Optional;

import dev.patika.vet_management_system.entities.Animal;
import dev.patika.vet_management_system.entities.Customer;

public interface ICustomerService {

    Customer save(Customer customer);

    Customer get(Long id);

    Customer update(Customer customer);

    boolean delete(Long id);

    Optional<Customer> getName(String name);

    List<Animal> getAnimalsByCustomerId(Long id);
    
}
