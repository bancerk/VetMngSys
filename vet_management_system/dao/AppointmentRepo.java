package dev.patika.vet_management_system.dao;

import dev.patika.vet_management_system.entities.Appointment;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndAppointmentDate(Long doctorId, LocalDateTime appointmentDate);
    
}
