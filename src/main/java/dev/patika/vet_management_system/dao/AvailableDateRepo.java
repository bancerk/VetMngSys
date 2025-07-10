package dev.patika.vet_management_system.dao;

import dev.patika.vet_management_system.entities.AvailableDate;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableDateRepo extends JpaRepository<AvailableDate, Long> {

boolean existsByDoctorIdAndAvailableDate(Long doctorId, LocalDate availableDate);
    
}
