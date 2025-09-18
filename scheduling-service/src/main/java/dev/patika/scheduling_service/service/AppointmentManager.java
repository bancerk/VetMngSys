package dev.patika.scheduling_service.service;

import dev.patika.scheduling_service.service.IAppointmentService;
import dev.patika.scheduling_service.dao.AppointmentRepo;
import dev.patika.scheduling_service.dao.DoctorRepo;
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
    private final DoctorRepo doctorRepo;
    private final WebClient.Builder webClientBuilder;

    public AppointmentManager(AppointmentRepo appointmentRepo, DoctorRepo doctorRepo,
            WebClient.Builder webClientBuilder) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Appointment save(Appointment appointment) {
        // 1. Check if doctor exists
        Doctor doctor = doctorRepo.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

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
}
