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

@Service
public class AvailableDateManager implements IAvailableDateService {
    private final AvailableDateRepo availableDateRepo;
    private final DoctorRepo doctorRepo;
    private final AppointmentRepo appointmentRepo;
    public AvailableDateManager(AvailableDateRepo availableDateRepo, DoctorRepo doctorRepo, AppointmentRepo appointmentRepo) {
        this.availableDateRepo = availableDateRepo;
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @Override
    @Transactional
    public AvailableDate save(AvailableDate availableDate) {
        if (availableDate.getDoctorId() != null) {
            doctorRepo.findById(availableDate.getDoctorId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Doctor not found with ID: " + availableDate.getDoctorId()));
        }

        LocalDate date = availableDate.getAvailableDate();
        List<Appointment> appointments = appointmentRepo.findAll();
        boolean hasAppointmentOnDay = appointments.stream()
            .anyMatch(a -> a.getDoctor().getId().equals(availableDate.getDoctorId())
                        && a.getAppointmentDate().toLocalDate().equals(date));
        if (hasAppointmentOnDay) {
            throw new IllegalArgumentException("Doctor already has an appointment on this date.");
        }

        boolean hasAvailableDate = availableDateRepo.existsByDoctorIdAndAvailableDate(
            availableDate.getDoctorId(), date);
        if (hasAvailableDate) {
            throw new IllegalArgumentException("Doctor already has an available date on this date.");
        }

        availableDate.setId(null);

        return this.availableDateRepo.save(availableDate);
    }

    @Override
    public AvailableDate get(Long id) {
        return this.availableDateRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(Message.NOT_FOUND));
    }

    @Override
    public AvailableDate update(AvailableDate availableDate) {
        this.get(availableDate.getId());
        return this.availableDateRepo.save(availableDate);
    }

    @Override
    public boolean delete(Long id) {
        this.get(id);
        this.availableDateRepo.deleteById(id);
        return true;
    }

}
