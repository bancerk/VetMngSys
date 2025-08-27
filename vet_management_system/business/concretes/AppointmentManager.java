package dev.patika.vet_management_system.business.concretes;

import dev.patika.vet_management_system.business.abstracts.IAppointmentService;
import dev.patika.vet_management_system.core.exceptions.NotFoundException;
import dev.patika.vet_management_system.core.utils.Message;
import dev.patika.vet_management_system.dao.AppointmentRepo;
import dev.patika.vet_management_system.dao.AvailableDateRepo;
import dev.patika.vet_management_system.dao.DoctorRepo;
import dev.patika.vet_management_system.dao.AnimalRepo;
import dev.patika.vet_management_system.entities.Appointment;
import dev.patika.vet_management_system.entities.Doctor;
import dev.patika.vet_management_system.entities.Animal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentManager implements IAppointmentService {

    private final AppointmentRepo appointmentRepo;
    private final AvailableDateRepo availableDateRepo;
    private final DoctorRepo doctorRepo;
    private final AnimalRepo animalRepo;

    public AppointmentManager(AppointmentRepo appointmentRepo, AvailableDateRepo availableDateRepo, DoctorRepo doctorRepo, AnimalRepo animalRepo) {
        this.appointmentRepo = appointmentRepo;
        this.availableDateRepo = availableDateRepo;
        this.doctorRepo = doctorRepo;
        this.animalRepo = animalRepo;
    }

    @Override
    @Transactional
    public Appointment save(Appointment appointment) {
        if (appointment.getAppointmentDate().getMinute() != 0 || appointment.getAppointmentDate().getSecond() != 0) {
            throw new IllegalArgumentException("Appointments must be scheduled exactly on the hour (e.g., 10:00, 14:00).");
        }
        Long doctorId = appointment.getDoctor().getId();
        if (availableDateRepo.existsByDoctorIdAndAvailableDate(doctorId, appointment.getAppointmentDate().toLocalDate())) {
            throw new IllegalArgumentException("Doctor already has an available date at this time.");
        }
        if (appointmentRepo.existsByDoctorIdAndAppointmentDate(doctorId, appointment.getAppointmentDate())) {
            throw new IllegalArgumentException("Doctor already has an appointment at this hour.");
        }
        Doctor doctor = doctorRepo.findById(doctorId)
                .orElseThrow(() -> new NotFoundException("Doctor not found"));
        Animal animal = animalRepo.findById(appointment.getAnimal().getId())
                .orElseThrow(() -> new NotFoundException("Animal not found"));
        appointment.setDoctor(doctor);
        appointment.setAnimal(animal);

        Appointment savedAppointment = appointmentRepo.save(appointment);
        return appointmentRepo.findById(savedAppointment.getId())
                .orElseThrow(() -> new NotFoundException("Appointment not found after save"));
    }

    @Override
    public Appointment get(Long id) {
        return appointmentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(Message.NOT_FOUND));
    }

    @Override
    @Transactional
    public Appointment update(Appointment appointment) {
        if (appointment.getAppointmentDate().getMinute() != 0 || appointment.getAppointmentDate().getSecond() != 0) {
            throw new IllegalArgumentException("Appointments must be scheduled exactly on the hour (e.g., 10:00, 14:00).");
        }
        Appointment existing = this.get(appointment.getId());
        existing.setAppointmentDate(appointment.getAppointmentDate());
        existing.setDoctor(appointment.getDoctor());
        existing.setAnimal(appointment.getAnimal());
        Appointment updatedAppointment = appointmentRepo.save(existing);
        return appointmentRepo.findById(updatedAppointment.getId())
                .orElseThrow(() -> new NotFoundException("Appointment not found after update"));
    }

    @Override
    public boolean delete(Long id) {
    this.get(id);
        appointmentRepo.deleteById(id);
        return true;
    }

    @Override
    public List<Appointment> findByAppointmentDate(LocalDateTime appointmentDate) {
        LocalDate date = appointmentDate.toLocalDate();
        return appointmentRepo.findAll().stream()
            .filter(a -> a.getAppointmentDate().toLocalDate().equals(date)).toList();
    }

    @Override
    public List<Appointment> findByAnimalName(String animalName) {
        return appointmentRepo.findAll().stream()
            .filter(a -> a.getAnimal() != null && a.getAnimal().getName().equalsIgnoreCase(animalName)).toList();
    }

    @Override
    public List<Appointment> findByDoctorName(String doctorName) {
        return appointmentRepo.findAll().stream()
            .filter(a -> a.getDoctor() != null && a.getDoctor().getName().equalsIgnoreCase(doctorName)).toList();
    }
}
