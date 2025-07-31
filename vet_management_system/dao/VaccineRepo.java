package dev.patika.vet_management_system.dao;

import dev.patika.vet_management_system.entities.Animal;
import dev.patika.vet_management_system.entities.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccineRepo extends JpaRepository<Vaccine, Long> {
    // Find all vaccines expiring within a date range
    List<Vaccine> findByProtectionFinishDateBetween(LocalDate start, LocalDate end);

    // Find all vaccines expiring before a certain date
    List<Vaccine> findByProtectionFinishDateBefore(LocalDate date);

    // Find all vaccines for a given animal
    List<Vaccine> findByAnimal(Animal animal);
}
