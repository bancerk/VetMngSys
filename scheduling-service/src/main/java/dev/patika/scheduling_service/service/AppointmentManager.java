package dev.patika.scheduling_service.service;

import dev.patika.scheduling_service.repo.AppointmentRepo;
import dev.patika.scheduling_service.entities.Appointment;
import dev.patika.scheduling_service.entities.Doctor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentManager implements IAppointmentService {

    private final AppointmentRepo appointmentRepo;
    private final WebClient.Builder webClientBuilder;

    public AppointmentManager(AppointmentRepo appointmentRepo, WebClient.Builder webClientBuilder) {
        this.appointmentRepo = appointmentRepo;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Appointment save(Appointment appointment) {
        // 1. Check if doctor exists
        Doctor doctor = webClientBuilder.build()
                .get()
                .uri("http://doctor-service/api/v1/doctors/{id}", appointment.getDoctor().getId())
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        clientResponse -> Mono.error(new RuntimeException("Doctor not found")))
                .bodyToMono(Doctor.class)
                .block(); // Using block() for simplicity, async is better
        if (doctor == null) {
            throw new RuntimeException("Doctor with ID " + appointment.getDoctor().getId() + " could not be verified.");
        }
        appointment.setDoctor(doctor); // Ensure the appointment has the managed doctor entity

        // 2. Check if doctor is available (simplified logic)
        LocalDateTime start = appointment.getAppointmentDate().withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusHours(1);
        if (!appointmentRepo.findByDoctorIdAndAppointmentDateBetween(doctor.getId(), start, end).isEmpty()) {
            throw new RuntimeException("Doctor is not available at this time.");
        }

        // 3. Call animal-service to validate animalId
        // The URL uses the service name 'animal-service' which Eureka will resolve to
        // an actual host and port.
        Boolean animalExists = webClientBuilder.build()
                .get()
                .uri("http://animal-service/api/v1/animals/exists/{id}", appointment.getAnimalId())
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        clientResponse -> Mono.error(new RuntimeException("Animal not found")))
                .bodyToMono(Boolean.class)
                .block(); // Using block() for simplicity, async is better

        if (animalExists == null || !animalExists) {
            throw new RuntimeException("Animal with ID " + appointment.getAnimalId() + " could not be verified.");
        }

        // 4. Save the appointment
        return this.appointmentRepo.save(appointment);
    }

    // ... other methods like get, update, delete
    @Override
    public List<Appointment> getAll() {
        return this.appointmentRepo.findAll();
    }

    @Override
    public Appointment getById(Long id) {
        return this.appointmentRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    @Override
    public Appointment update(Appointment appointment) {
        // Check if appointment exists
        Appointment existingAppointment = getById(appointment.getId());

        // Verify the doctor exists
        Doctor doctor = webClientBuilder.build()
                .get()
                .uri("http://doctor-service/api/v1/doctors/{id}", appointment.getDoctor().getId())
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        clientResponse -> Mono.error(new RuntimeException("Doctor not found")))
                .bodyToMono(Doctor.class)
                .block(); // Using block() for simplicity, async is better
        if (doctor == null) {
            throw new RuntimeException("Doctor with ID " + appointment.getDoctor().getId() + " could not be verified.");
        }

        // Update appointment details
        existingAppointment.setAppointmentDate(appointment.getAppointmentDate());
        existingAppointment.setAnimalId(appointment.getAnimalId());
        existingAppointment.setDoctor(doctor);

        return this.appointmentRepo.save(existingAppointment);
    }

    @Override
    public void delete(Long id) {
        // Check if appointment exists
        Appointment existingAppointment = getById(id);

        this.appointmentRepo.delete(existingAppointment);
    }
}
