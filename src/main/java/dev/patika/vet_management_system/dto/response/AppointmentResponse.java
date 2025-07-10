package dev.patika.vet_management_system.dto.response;

import java.time.LocalDateTime;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    @Id
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Appointment date cannot be null")
    @Future(message = "Appointment date must be in the future")
    private LocalDateTime appointmentDate;

    @NotNull(message = "Animal ID cannot be null")
    private Long animalId;

    @NotNull(message = "Animal name cannot be null")
    private String animalName;

    @NotNull(message = "Doctor ID cannot be null")
    private Long doctorId;

    @NotNull(message = "Doctor name cannot be null")
    private String doctorName;
}
