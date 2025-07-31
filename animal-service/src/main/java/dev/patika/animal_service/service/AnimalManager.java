package dev.patika.animal_service.service;

import dev.patika.animal_service.entities.Animal;
import dev.patika.animal_service.repo.AnimalRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class AnimalManager implements IAnimalService {

    private final AnimalRepo animalRepo;

    public AnimalManager(AnimalRepo animalRepo) {
        this.animalRepo = animalRepo;
    }

    @Override
    @Transactional
    public Animal save(Animal animal) {
        animal.setVaccineList(new ArrayList<>());
        animal.setAppointmentList(new ArrayList<>());

        if (animal.getCustomer() != null && animal.getCustomer().getId() != null) {
            animal.setCustomer(customerRepo.findById(animal.getCustomer().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found")));
        }

        animal.setId(null);

        return this.animalRepo.save(animal);
    }

    @Override
    public Animal get(Long id) {
        return this.animalRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Animal not found"));
    }

    @Override
    public Animal update(Animal animal) {
        this.get(animal.getId());
        return this.animalRepo.save(animal);
    }

    @Override
    public boolean delete(Long id) {
        this.get(id);
        this.animalRepo.deleteById(id);
        return true;
    }

    @Override
    public Optional<Animal> getName(String name) {
        Animal animal = this.animalRepo.findByName(name);
        if (animal == null) {
            throw new NotFoundException(Message.NOT_FOUND);
        }
        return Optional.of(animal);
    }

}
