package dev.patika.vet_management_system.business.abstracts;

import java.util.Optional;

import dev.patika.vet_management_system.entities.Animal;

public interface IAnimalService {

    Animal save(Animal animal);

    Animal get(Long id);

    Animal update(Animal animal);

    boolean delete(Long id);

    Optional<Animal> getName(String name);
}
