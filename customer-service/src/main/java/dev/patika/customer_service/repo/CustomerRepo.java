package dev.patika.customer_service.repo;

import dev.patika.customer_service.entities.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Customer findByName(String name);
}
