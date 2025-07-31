package dev.patika.vet_management_system.dao;

import dev.patika.vet_management_system.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {
}
