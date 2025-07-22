package dev.patika.customer_service.service;

import java.util.Optional;

import dev.patika.customer_service.entities.Customer;

public interface ICustomerService {

    Customer save(Customer customer);

    Customer get(Long id);

    Customer update(Customer customer);

    boolean delete(Long id);

    Optional<Customer> getName(String name);

}
