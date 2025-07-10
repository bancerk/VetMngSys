package dev.patika.vet_management_system.dto.response;

import java.time.LocalDate;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VaccineResponse {

    @Id
    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Code cannot be null")
    private String code;

    @NotNull(message = "Protection start date cannot be null")
    private LocalDate protectionStartDate;

    @NotNull(message = "Protection finish date cannot be null")
    @Future(message = "Protection finish date must be in the future")
    private LocalDate protectionFinishDate;
    private boolean registrationAllowed;

    @NotNull(message = "Animal ID cannot be null")
    private Long animalId;

    private String animalName;

}
