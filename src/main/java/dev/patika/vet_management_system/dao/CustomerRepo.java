package dev.patika.vet_management_system.dao;

import dev.patika.vet_management_system.entities.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Customer findByName(String name);
}
