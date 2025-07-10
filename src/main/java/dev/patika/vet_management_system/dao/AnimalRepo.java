package dev.patika.vet_management_system.dao;

import dev.patika.vet_management_system.entities.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimalRepo extends JpaRepository<Animal, Long> {
    Animal findByName(String name); // Custom query method to find an animal by its name
}
