package dev.patika.vet_management_system.business.abstracts;

import java.time.LocalDateTime;
import java.util.List;

import dev.patika.vet_management_system.entities.Appointment;

public interface IAppointmentService {

    Appointment save(Appointment appointment);

    Appointment get(Long id);

    Appointment update(Appointment appointment);

    boolean delete(Long id);

    List<Appointment> findByAppointmentDate(LocalDateTime appointmentDate);

    List<Appointment> findByAnimalName(String animalName);

    List<Appointment> findByDoctorName(String doctorName);

}
