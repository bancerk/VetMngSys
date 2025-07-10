package dev.patika.vet_management_system.business.concretes;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.patika.vet_management_system.business.abstracts.IAvailableDateService;
import dev.patika.vet_management_system.core.utils.Message;
import dev.patika.vet_management_system.dao.AppointmentRepo;
import dev.patika.vet_management_system.dao.AvailableDateRepo;
import dev.patika.vet_management_system.dao.DoctorRepo;
import dev.patika.vet_management_system.entities.Appointment;
import dev.patika.vet_management_system.entities.AvailableDate;

@Service // Marks this class as a Spring service component
public class AvailableDateManager implements IAvailableDateService {

    // Repository for available date data access
    private final AvailableDateRepo availableDateRepo;
    // Repository for doctor data access
    private final DoctorRepo doctorRepo;
    // Repository for appointment data access
    // This is used to check if a doctor has an appointment on the same date
    private final AppointmentRepo appointmentRepo;

    // Constructor-based dependency injection for repositories
    public AvailableDateManager(AvailableDateRepo availableDateRepo, DoctorRepo doctorRepo, AppointmentRepo appointmentRepo) {
        this.availableDateRepo = availableDateRepo;
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @Override
    @Transactional // Ensures atomicity for save operations
    public AvailableDate save(AvailableDate availableDate) {
        // Verify that the doctor exists before saving the available date
        if (availableDate.getDoctorId() != null) {
            doctorRepo.findById(availableDate.getDoctorId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Doctor not found with ID: " + availableDate.getDoctorId()));
        }

        // Check if the doctor already has an appointment for the specified date (any hour)
        LocalDate date = availableDate.getAvailableDate();
        List<Appointment> appointments = appointmentRepo.findAll();
        boolean hasAppointmentOnDay = appointments.stream()
            .anyMatch(a -> a.getDoctor().getId().equals(availableDate.getDoctorId())
                        && a.getAppointmentDate().toLocalDate().equals(date));
        if (hasAppointmentOnDay) {
            throw new IllegalArgumentException("Doctor already has an appointment on this date.");
        }

        // Check if the doctor already has an available date for the specified date
        boolean hasAvailableDate = availableDateRepo.existsByDoctorIdAndAvailableDate(
            availableDate.getDoctorId(), date);
        if (hasAvailableDate) {
            throw new IllegalArgumentException("Doctor already has an available date on this date.");
        }

        // Clear the ID to ensure a new record is created
        availableDate.setId(null);

        // Save the available date entity and return the result
        return this.availableDateRepo.save(availableDate);
    }

    @Override
    public AvailableDate get(Long id) {
        // Retrieve the available date by ID or throw an exception if not found
        return this.availableDateRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(Message.NOT_FOUND));
    }

    @Override
    public AvailableDate update(AvailableDate availableDate) {
        // Ensure the available date exists before updating
        this.get(availableDate.getId());
        // Save and return the updated available date entity
        return this.availableDateRepo.save(availableDate);
    }

    @Override
    public boolean delete(Long id) {
        // Ensure the available date exists before deleting
        this.get(id);
        // Delete the available date by ID
        this.availableDateRepo.deleteById(id);
        return true;
    }

}
