package dev.patika.vet_management_system.business.concretes;

import dev.patika.repo.CustomerRepo;
import dev.patika.vet_management_system.business.abstracts.IAnimalService;
import dev.patika.vet_management_system.core.exceptions.NotFoundException;
import dev.patika.vet_management_system.core.utils.Message;
import dev.patika.vet_management_system.dao.AnimalRepo;
import dev.patika.vet_management_system.entities.Animal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service // Marks this class as a Spring service component
public class AnimalManager implements IAnimalService {

    // Repository for animal data access
    private final AnimalRepo animalRepo;
    // Repository for customer data access
    private final CustomerRepo customerRepo;

    // Constructor-based dependency injection for repositories
    public AnimalManager(AnimalRepo animalRepo, CustomerRepo customerRepo) {
        this.animalRepo = animalRepo;
        this.customerRepo = customerRepo;
    }

    @Override
    @Transactional // Ensures atomicity for save operations
    public Animal save(Animal animal) {
        // Initialize collections to prevent null pointer exceptions
        animal.setVaccineList(new ArrayList<>());
        animal.setAppointmentList(new ArrayList<>());

        // Ensure we have a valid customer
        if (animal.getCustomer() != null && animal.getCustomer().getId() != null) {
            animal.setCustomer(customerRepo.findById(animal.getCustomer().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found")));
        }

        // Clear the ID to ensure a new record is created
        animal.setId(null);

        // Save the animal entity and return the result
        return this.animalRepo.save(animal);
    }

    @Override
    public Animal get(Long id) {
        // Retrieve the animal by ID or throw an exception if not found
        return this.animalRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Animal not found"));
    }

    @Override
    public Animal update(Animal animal) {
        // Ensure the animal exists before updating
        this.get(animal.getId());
        // Save and return the updated animal entity
        return this.animalRepo.save(animal);
    }

    @Override
    public boolean delete(Long id) {
        // Ensure the animal exists before deleting
        this.get(id);
        // Delete the animal by ID
        this.animalRepo.deleteById(id);
        return true;
    }

    @Override
    public Optional<Animal> getName(String name) {
        // Retrieve the animal by name or throw NotFoundException if not found
        Animal animal = this.animalRepo.findByName(name);
        if (animal == null) {
            throw new NotFoundException(Message.NOT_FOUND);
        }
        return Optional.of(animal);
    }

}
