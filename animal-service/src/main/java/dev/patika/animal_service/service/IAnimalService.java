package dev.patika.animal_service.service;

import dev.patika.animal_service.entities.Animal;

import java.util.Optional;

public interface IAnimalService {

    Animal save(Animal animal);

    Animal get(Long id);

    Animal update(Animal animal);

    boolean delete(Long id);

    Optional<Animal> getName(String name);
}
