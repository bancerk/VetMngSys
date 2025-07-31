package dev.patika.vet_management_system.dto.response;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {

    @Id
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Appointment list cannot be null")
    private String appointmentList;

    @NotNull(message = "Animal list cannot be null")
    private String animalList;

    @NotNull(message = "Available date list cannot be null")
    private String availableDateList;

}
