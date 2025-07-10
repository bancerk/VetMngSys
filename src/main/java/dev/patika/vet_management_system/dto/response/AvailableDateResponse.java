package dev.patika.vet_management_system.dto.response;

import java.time.LocalDate;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableDateResponse {

    @NotNull(message = "ID cannot be null")
    private Long id;

    @NotNull(message = "Available date cannot be null")
    @FutureOrPresent(message = "Available date must be in the present or future")
    private LocalDate availableDate;

    @NotNull(message = "Doctor ID cannot be null")
    private Long doctorId;
}
