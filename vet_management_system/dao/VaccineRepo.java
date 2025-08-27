package dev.patika.vet_management_system.dao;

import dev.patika.vet_management_system.entities.Animal;
import dev.patika.vet_management_system.entities.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccineRepo extends JpaRepository<Vaccine, Long> {
    List<Vaccine> findByProtectionFinishDateBetween(LocalDate start, LocalDate end);

    List<Vaccine> findByProtectionFinishDateBefore(LocalDate date);

    List<Vaccine> findByAnimal(Animal animal);
}
